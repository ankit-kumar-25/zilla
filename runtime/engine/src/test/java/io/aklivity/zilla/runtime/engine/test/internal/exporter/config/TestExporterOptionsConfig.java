/*
 * Copyright 2021-2024 Aklivity Inc.
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
package io.aklivity.zilla.runtime.engine.test.internal.exporter.config;

import java.util.List;
import java.util.function.Function;

import io.aklivity.zilla.runtime.engine.config.OptionsConfig;

public final class TestExporterOptionsConfig extends OptionsConfig
{
    public final String mode;
    public final List<Event> events;

    public static TestExporterOptionsConfigBuilder<TestExporterOptionsConfig> builder()
    {
        return new TestExporterOptionsConfigBuilder<>(TestExporterOptionsConfig.class::cast);
    }

    public static <T> TestExporterOptionsConfigBuilder<T> builder(
        Function<OptionsConfig, T> mapper)
    {
        return new TestExporterOptionsConfigBuilder<>(mapper);
    }

    TestExporterOptionsConfig(
        String mode,
        List<Event> events)
    {
        this.mode = mode;
        this.events = events;
    }

    public static final class Event
    {
        public final String qName;
        public final String id;
        public final String name;
        public final String message;

        public Event(
                String qName,
                String id,
                String name,
                String message)
        {
            this.qName = qName;
            this.id = id;
            this.name = name;
            this.message = message;
        }
    }
}
