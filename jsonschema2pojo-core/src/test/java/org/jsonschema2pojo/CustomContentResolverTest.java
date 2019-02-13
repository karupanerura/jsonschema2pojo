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

import com.fasterxml.jackson.databind.JsonNode;
import org.jsonschema2pojo.resolver.SchemeResolver;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CustomContentResolverTest {

    private ContentResolver resolver = new ContentResolver();

    {
        URI schemaFile = URI.create("classpath:schema/address.json");
        JsonNode node = resolver.resolve(schemaFile);
        resolver.setSchemeResolver("test", new StaticResolver(node));
    }

    private static class StaticResolver implements SchemeResolver {

        private final JsonNode node;

        public StaticResolver(JsonNode node) {
            this.node = node;
        }

        @Override
        public JsonNode resolve(URI uri) {
            return this.node;
        }

    }

    @Test
    public void customResolver() {
        URI testURI = URI.create("test:test");
        JsonNode node = resolver.resolve(testURI);
        assertThat(node.path("description").asText().length(), is(greaterThan(0)));
    }

}
