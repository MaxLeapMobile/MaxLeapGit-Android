/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.widget;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.argb(255, 0, 118, 255));
    }
}
