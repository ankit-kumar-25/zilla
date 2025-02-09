/*
 * Copyright 2021-2024 Aklivity Inc
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
package io.aklivity.zilla.runtime.model.core.config;

import java.util.function.Function;

import io.aklivity.zilla.runtime.engine.config.ModelConfig;

public class DoubleModelConfig extends ModelConfig
{
    public static final String DOUBLE = "double";

    public final String format;
    public final double max;
    public final double min;
    public final Double multiple;
    public final boolean exclusiveMax;
    public final boolean exclusiveMin;

    public DoubleModelConfig(
        String format,
        double max,
        double min,
        boolean exclusiveMax,
        boolean exclusiveMin,
        Double multiple)
    {
        super(DOUBLE);
        this.format = format;
        this.max = max;
        this.min = min;
        this.exclusiveMax = exclusiveMax;
        this.exclusiveMin = exclusiveMin;
        this.multiple = multiple;
    }

    public static <T> DoubleModelConfigBuilder<T> builder(
        Function<ModelConfig, T> mapper)
    {
        return new DoubleModelConfigBuilder<>(mapper::apply);
    }

    public static DoubleModelConfigBuilder<DoubleModelConfig> builder()
    {
        return new DoubleModelConfigBuilder<>(DoubleModelConfig.class::cast);
    }
}
