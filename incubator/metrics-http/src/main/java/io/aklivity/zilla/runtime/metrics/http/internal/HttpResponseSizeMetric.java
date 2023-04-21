/*
 * Copyright 2021-2022 Aklivity Inc
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
package io.aklivity.zilla.runtime.metrics.http.internal;

import static io.aklivity.zilla.runtime.metrics.http.internal.HttpUtils.INVALID_CONTENT_LENGTH;
import static io.aklivity.zilla.runtime.metrics.http.internal.HttpUtils.findContentLength;
import static io.aklivity.zilla.runtime.metrics.http.internal.HttpUtils.isInitial;
import static io.aklivity.zilla.runtime.metrics.http.internal.HttpUtils.parseContentLength;

import java.util.function.LongConsumer;

import org.agrona.DirectBuffer;
import org.agrona.collections.Long2LongCounterMap;
import org.agrona.collections.Long2ObjectHashMap;

import io.aklivity.zilla.runtime.engine.EngineContext;
import io.aklivity.zilla.runtime.engine.binding.function.MessageConsumer;
import io.aklivity.zilla.runtime.engine.metrics.Metric;
import io.aklivity.zilla.runtime.engine.metrics.MetricContext;
import io.aklivity.zilla.runtime.metrics.http.internal.types.HttpHeaderFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.AbortFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.BeginFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.DataFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.EndFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.FrameFW;
import io.aklivity.zilla.runtime.metrics.http.internal.types.stream.ResetFW;

public class HttpResponseSizeMetric implements Metric
{
    private static final String GROUP = HttpMetricGroup.NAME;
    private static final String NAME = String.format("%s.%s", GROUP, "response.size");

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public Kind kind()
    {
        return Kind.HISTOGRAM;
    }

    @Override
    public Unit unit()
    {
        return Unit.BYTES;
    }

    @Override
    public MetricContext supply(
        EngineContext context)
    {
        return new HttpResponseSizeMetricContext();
    }

    @Override
    public String description()
    {
        return "HTTP response content length";
    }

    private final class HttpResponseSizeMetricContext implements MetricContext
    {
        private static final long INITIAL_VALUE = 0L;
        private final Long2LongCounterMap responseSize = new Long2LongCounterMap(INITIAL_VALUE);
        private final Long2ObjectHashMap<HttpMetricConsumer> handlers = new Long2ObjectHashMap<>();
        private final FrameFW frameRO = new FrameFW();
        private final BeginFW beginRO = new BeginFW();
        private final DataFW dataRO = new DataFW();

        @Override
        public String group()
        {
            return GROUP;
        }

        @Override
        public Metric.Kind kind()
        {
            return HttpResponseSizeMetric.this.kind();
        }

        @Override
        public MessageConsumer supply(
            LongConsumer recorder)
        {
            return (t, b, i, l) -> handle(recorder, t, b, i, l);
        }

        private void handle(
            LongConsumer recorder,
            int msgTypeId,
            DirectBuffer buffer,
            int index,
            int length)
        {
            final FrameFW frame = frameRO.wrap(buffer, index, index + length);
            final long streamId = frame.streamId();
            if (!isInitial(streamId)) // it's a sent stream
            {
                handleInitial(recorder, streamId, msgTypeId, buffer, index, length);
            }
        }

        private void handleInitial(
            LongConsumer recorder,
            long streamId,
            int msgTypeId,
            DirectBuffer buffer,
            int index,
            int length)
        {
            switch (msgTypeId)
            {
            case BeginFW.TYPE_ID:
                final BeginFW begin = beginRO.wrap(buffer, index, index + length);
                final HttpHeaderFW contentLengthHeader = findContentLength(begin);
                long contentLength = parseContentLength(contentLengthHeader);
                if (contentLength == INVALID_CONTENT_LENGTH)
                {
                    handlers.put(streamId, this::handleDynamicLength);
                }
                else
                {
                    if (contentLength != INITIAL_VALUE)
                    {
                        responseSize.put(streamId, contentLength);
                    }
                    handlers.put(streamId, this::handleFixedLength);
                }
                break;
            default:
                HttpMetricConsumer handler = handlers.getOrDefault(streamId, HttpMetricConsumer.NOOP);
                handler.accept(recorder, streamId, msgTypeId, buffer, index, length);
                break;
            }
        }

        private void handleFixedLength(
            LongConsumer recorder,
            long streamId,
            int msgTypeId,
            DirectBuffer buffer,
            int index,
            int length)
        {
            switch (msgTypeId)
            {
            case EndFW.TYPE_ID:
                recorder.accept(responseSize.remove(streamId));
                handlers.remove(streamId);
                break;
            case AbortFW.TYPE_ID:
            case ResetFW.TYPE_ID:
                responseSize.remove(streamId);
                handlers.remove(streamId);
                break;
            }
        }

        private void handleDynamicLength(
            LongConsumer recorder,
            long streamId,
            int msgTypeId,
            DirectBuffer buffer,
            int index,
            int length)
        {
            switch (msgTypeId)
            {
            case DataFW.TYPE_ID:
                final DataFW data = dataRO.wrap(buffer, index, index + length);
                responseSize.getAndAdd(streamId, data.length());
                break;
            case EndFW.TYPE_ID:
                recorder.accept(responseSize.remove(streamId));
                handlers.remove(streamId);
                break;
            case AbortFW.TYPE_ID:
            case ResetFW.TYPE_ID:
                responseSize.remove(streamId);
                handlers.remove(streamId);
                break;
            }
        }
    }
}
