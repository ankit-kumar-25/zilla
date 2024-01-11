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
package io.aklivity.zilla.runtime.engine.converter;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.ServiceLoader.load;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.function.LongFunction;

import io.aklivity.zilla.runtime.engine.catalog.CatalogHandler;
import io.aklivity.zilla.runtime.engine.config.ConverterConfig;

public final class ConverterFactory
{
    private final Map<String, ConverterFactorySpi> converterSpis;

    public static ConverterFactory instantiate()
    {
        return instantiate(load(ConverterFactorySpi.class));
    }

    public Converter createReader(
        ConverterConfig config,
        LongFunction<CatalogHandler> supplyCatalog)
    {
        String type = config.type;
        requireNonNull(type, "name");

        ConverterFactorySpi converterSpi = requireNonNull(converterSpis.get(type), () -> "Unrecognized Converter name: " + type);

        return converterSpi.createReader(config, supplyCatalog);
    }

    public Converter createWriter(
        ConverterConfig config,
        LongFunction<CatalogHandler> supplyCatalog)
    {
        String type = config.type;
        requireNonNull(type, "name");

        ConverterFactorySpi converterSpi = requireNonNull(converterSpis.get(type), () -> "Unrecognized Converter name: " + type);

        return converterSpi.createWriter(config, supplyCatalog);
    }

    public Collection<ConverterFactorySpi> converterSpis()
    {
        return converterSpis.values();
    }

    private static ConverterFactory instantiate(
        ServiceLoader<ConverterFactorySpi> converters)
    {
        Map<String, ConverterFactorySpi> converterSpisByName = new TreeMap<>();
        converters.forEach(converterSpi -> converterSpisByName.put(converterSpi.type(), converterSpi));

        return new ConverterFactory(unmodifiableMap(converterSpisByName));
    }

    private ConverterFactory(
        Map<String, ConverterFactorySpi> converterSpis)
    {
        this.converterSpis = converterSpis;
    }
}
