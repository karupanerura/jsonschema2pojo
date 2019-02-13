/**
 * Copyright © 2010-2017 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsonschema2pojo;

import static java.util.Arrays.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsonschema2pojo.resolver.SchemeResolver;
import org.jsonschema2pojo.resolver.ClassPathResolver;

/**
 * Reads URI contents for various protocols.
 */
public class ContentResolver {

    private static final List<String> CLASSPATH_SCHEMES = asList("classpath", "resource", "java");

    private final Map<String, SchemeResolver> schemeResolverMap = new HashMap<>();
    private final ObjectMapper objectMapper;

    public ContentResolver() {
    	this(null);
	}

    public ContentResolver(JsonFactory jsonFactory) {
    	this.objectMapper = new ObjectMapper(jsonFactory)
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

       SchemeResolver resolver = new ClassPathResolver(this.objectMapper);
       for (String scheme : CLASSPATH_SCHEMES) {
           this.setSchemeResolver(scheme, resolver);
       }
	}

    /**
     * Set SchemeResolver to resolve custom scheme.
     * When this class resolve URI of the registered scheme, this class delegates URI resolving to the registered SchemeResolver corresponding to the scheme.
     *
     * @param scheme
     *               URI scheme
     * @param resolver
     */
    public void setSchemeResolver(String scheme, SchemeResolver resolver) {
        schemeResolverMap.put(scheme, resolver);
    }

    /**
     * Resolve a given URI to read its contents and parse the result as JSON.
     * <p>
     * Supported protocols:
     * <ul>
     * <li>http/https
     * <li>file
     * <li>classpath/resource/java (all synonymous, used to resolve a schema
     * from the classpath)
     * </ul>
     *
     * @param uri
     *            the URI to read schema content from
     * @return the JSON tree found at the given URI
     */
    public JsonNode resolve(URI uri) {

        SchemeResolver schemeResolver = schemeResolverMap.get(uri.getScheme());
        if (schemeResolver != null) {
            return schemeResolver.resolve(uri);
        }

        try {
            return objectMapper.readTree(uri.toURL());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing document: " + uri, e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unrecognised URI, can't resolve this: " + uri, e);
        }

    }

}
