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

package org.jsonschema2pojo.resolver;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;

public interface SchemeResolver {

    /**
     * Resolve a given URI to read its contents and parse the result as JSON.
     *
     * @param uri
     *            the URI to read schema content from
     * @return the JSON tree found at the given URI
     */
    JsonNode resolve(URI uri);

}
