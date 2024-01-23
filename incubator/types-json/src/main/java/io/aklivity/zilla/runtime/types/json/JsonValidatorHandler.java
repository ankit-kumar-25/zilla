/*
 * Copyright 2021-2023 Aklivity Inc
 *
 * Licensed under the Aklivity Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 *   https://www.aklivity.io/aklivity-community-license/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.aklivity.zilla.runtime.types.json;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.function.LongFunction;

import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

import org.agrona.DirectBuffer;
import org.agrona.collections.Int2ObjectCache;
import org.agrona.io.DirectBufferInputStream;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonSchemaReader;
import org.leadpony.justify.api.JsonValidatingException;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;

import io.aklivity.zilla.runtime.engine.catalog.CatalogHandler;
import io.aklivity.zilla.runtime.engine.config.CatalogedConfig;
import io.aklivity.zilla.runtime.engine.config.SchemaConfig;
import io.aklivity.zilla.runtime.engine.converter.function.ValueConsumer;
import io.aklivity.zilla.runtime.engine.validator.ValidatorHandler;
import io.aklivity.zilla.runtime.types.json.config.JsonValidatorConfig;

public class JsonValidatorHandler implements ValidatorHandler
{
    private final SchemaConfig catalog;
    private final CatalogHandler handler;
    private final String subject;
    private final Int2ObjectCache<JsonSchema> schemas;
    private final Int2ObjectCache<JsonProvider> providers;
    private final JsonProvider schemaProvider;
    private final JsonValidationService service;
    private final JsonParserFactory factory;
    private DirectBufferInputStream in;
    private final InputStreamReader input;

    public JsonValidatorHandler(
        JsonValidatorConfig config,
        LongFunction<CatalogHandler> supplyCatalog)
    {
        this.schemaProvider = JsonProvider.provider();
        this.service = JsonValidationService.newInstance();
        this.factory = schemaProvider.createParserFactory(null);
        CatalogedConfig cataloged = config.cataloged.get(0);
        this.catalog = cataloged.schemas.size() != 0 ? cataloged.schemas.get(0) : null;
        this.handler = supplyCatalog.apply(cataloged.id);
        this.subject = catalog != null && catalog.subject != null
            ? catalog.subject
            : config.subject;
        this.schemas = new Int2ObjectCache<>(1, 1024, i -> {});
        this.providers = new Int2ObjectCache<>(1, 1024, i -> {});
        this.in = new DirectBufferInputStream();
        this.input = new InputStreamReader(in);
    }

    @Override
    public boolean validate(
        int flags,
        DirectBuffer data,
        int index,
        int length,
        ValueConsumer next)
    {
        boolean status = false;

        if ((flags & FLAGS_INIT) != 0x00)
        {

        }

        in.wrap(data, index, length);

        if ((flags & FLAGS_FIN) != 0x00)
        {
            try
            {
                JsonProvider provider = supplyProvider(catalog.id);
                provider.createReader(input).readValue();
                status = true;
            }
            catch (JsonValidatingException ex)
            {
                ex.printStackTrace();
            }
        }
        return status;
    }

    private JsonSchema supplySchema(
        int schemaId)
    {
        return schemas.computeIfAbsent(schemaId, this::resolveSchema);
    }

    private JsonProvider supplyProvider(
        int schemaId)
    {
        return providers.computeIfAbsent(schemaId, this::createProvider);
    }

    private JsonSchema resolveSchema(
        int schemaId)
    {
        JsonSchema schema = null;
        String schemaText = handler.resolve(schemaId);
        if (schemaText != null)
        {
            JsonParser schemaParser = factory.createParser(new StringReader(schemaText));
            JsonSchemaReader reader = service.createSchemaReader(schemaParser);
            schema = reader.read();
        }

        return schema;
    }

    private JsonProvider createProvider(
        int schemaId)
    {
        JsonSchema schema = supplySchema(schemaId);
        JsonProvider provider = null;
        if (schema != null)
        {
            provider = service.createJsonProvider(schema, parser -> ProblemHandler.throwing());
        }
        return provider;
    }
}
