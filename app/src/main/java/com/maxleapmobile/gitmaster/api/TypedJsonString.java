/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.api;

import retrofit.mime.TypedString;

public class TypedJsonString extends TypedString {

    public TypedJsonString(String body) {
        super(body);
    }

    @Override public String mimeType() {
        return "application/json";
    }
}