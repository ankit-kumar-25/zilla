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
package io.aklivity.zilla.runtime.guard.jwt.internal.keys;

import io.aklivity.zilla.runtime.guard.jwt.config.JwtKeyConfig;

public final class JwtKeyConfigs
{
    public static final JwtKeyConfig RFC7515_RS256_CONFIG;
    public static final JwtKeyConfig RFC7515_ES256_CONFIG;

    static
    {
        // RFC 7515, section A.2.1
        RFC7515_RS256_CONFIG = JwtKeyConfig.builder()
                .kty("RSA")
                .kid("test")
                .use("verify")
                .n("ofgWCuLjybRlzo0tZWJjNiuSfb4p4fAkd_wWJcyQoTbji9k0l8W26mPddx" +
                   "HmfHQp-Vaw-4qPCJrcS2mJPMEzP1Pt0Bm4d4QlL-yRT-SFd2lZS-pCgNMs" +
                   "D1W_YpRPEwOWvG6b32690r2jZ47soMZo9wGzjb_7OMg0LOL-bSf63kpaSH" +
                   "SXndS5z5rexMdbBYUsLA9e-KXBdQOS-UTo7WTBEMa2R2CapHg665xsmtdV" +
                   "MTBQY4uDZlxvb3qCo5ZwKh9kG4LT6_I5IhlJH7aGhyxXFvUK-DWNmoudF8" +
                   "NAco9_h9iaGNj8q2ethFkMLs91kzk2PAcDTW9gb54h4FRWyuXpoQ")
                .e("AQAB")
                .alg("RS256")
                .build();

        // RFC 7515, section A.3.1
        RFC7515_ES256_CONFIG = JwtKeyConfig.builder()
                .kty("RSA")
                .kid("test")
                .use("verify")
                .crv("P-256")
                .x("f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU")
                .y("x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0")
                .build();
    }

    private JwtKeyConfigs()
    {
    }
}
