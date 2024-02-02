/*
 * Copyright 2021-2023 Aklivity Inc.
 *
 * Aklivity licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.aklivity.zilla.runtime.engine.test.internal.catalog;

import io.aklivity.zilla.runtime.engine.catalog.CatalogHandler;
import io.aklivity.zilla.runtime.engine.test.internal.catalog.config.TestCatalogOptionsConfig;

public class TestCatalogHandler implements CatalogHandler
{
    private final String schema;
    private final int id;

    public TestCatalogHandler(
        TestCatalogOptionsConfig options)
    {
        this.id = options != null ? options.id : NO_SCHEMA_ID;
        this.schema = options != null ? options.schema : null;
    }

    @Override
    public int register(
        String subject,
        String type,
        String schema)
    {
        return id;
    }

    @Override
    public int resolve(
        String subject,
        String version)
    {
        return id;
    }

    @Override
    public String resolve(
        int schemaId)
    {
        return schemaId == id ? schema : null;
    }
}
