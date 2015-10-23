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
import android.text.style.ClickableSpan;
import android.view.View;

public class CustomClickableSpan extends ClickableSpan {

    private TextClickListener textClickListener;

    public CustomClickableSpan(TextClickListener textClickListener) {
        this.textClickListener = textClickListener;
    }

    @Override
    public void onClick(View widget) {
        textClickListener.onClick();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.argb(255, 0, 118, 255));
    }

    public interface TextClickListener {
        void onClick();
    }

}
