/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.maxleapmobile.gitmaster.R;


public class WebViewProgressBar extends ProgressBar {

    private int progress = 1;
    private final static int HEIGHT = 5;
    private Paint paint;
    private final static int colors[] = new int[]{};
    public WebViewProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewProgressBar(Context context) {
        super(context);
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(HEIGHT);
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.colorPrimaryLight));
    }
    public void setProgress(int progress){
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth() * progress / 100, HEIGHT, paint);
    }
}