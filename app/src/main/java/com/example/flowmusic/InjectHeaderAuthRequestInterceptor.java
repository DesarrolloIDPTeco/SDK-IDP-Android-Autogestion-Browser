/*
 * Copyright (c) 2020 ForgeRock. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.example.flowmusic;

import androidx.annotation.NonNull;

import org.forgerock.android.auth.Action;
import org.forgerock.android.auth.FRRequestInterceptor;
import org.forgerock.android.auth.Request;

/**
 * Sample {@link FRRequestInterceptor} to add header
 */
public class InjectHeaderAuthRequestInterceptor implements FRRequestInterceptor<Action> {

    @NonNull
    @Override
    public Request intercept(@NonNull Request request, Action action) {
        return request.newBuilder()
                .addHeader("headerName", "headerValue")
                .addHeader("headerName", "headerValue2")
                .build();

    }
}
