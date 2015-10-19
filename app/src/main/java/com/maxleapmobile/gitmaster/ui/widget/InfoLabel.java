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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.util.DeviceUtils;

public class InfoLabel extends View {

    //两个画笔
    private Paint namePaint;
    private Paint valuePaint;
    private Rect nameRect;
    private Rect valueRect;

    private int nameColor;
    private int vauleColor;

    private String label_name;
    private String label_value;


    public InfoLabel(Context context) {
        this(context, null);
    }

    public InfoLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.user_verical_label);
        label_name = a.getString(R.styleable.user_verical_label_name);
        label_value = a.getString(R.styleable.user_verical_label_value);

        nameColor = a.getColor(R.styleable.user_verical_label_name_color, Color.WHITE);
        vauleColor = a.getColor(R.styleable.user_verical_label_value_color, Color.WHITE);

        a.recycle();

        nameRect = new Rect();
        namePaint = new Paint();
        namePaint.setTextSize(DeviceUtils.sp2px(this.getContext(), 15));
        namePaint.setColor(nameColor);

        valueRect = new Rect();
        valuePaint = new Paint();
        valuePaint.setTextSize(DeviceUtils.sp2px(this.getContext(), 25));
        valuePaint.setColor(vauleColor);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        namePaint.getTextBounds(label_name, 0, label_name.length(), nameRect);

        valuePaint.getTextBounds(label_value, 0, label_value.length(), valueRect);



        int valueX = getMeasuredWidth() / 2 - valueRect.width() / 2;
        int valueY = getMeasuredWidth() / 2 - (valueRect.height() + nameRect.height())/2;

        int nameX = getMeasuredWidth() / 2 - nameRect.width() / 2;
        int nameY = valueY + nameRect.height()+ DeviceUtils.sp2px(this.getContext(), 3);


        canvas.drawText(label_value, valueX, valueY, valuePaint);
        canvas.drawText(label_name, nameX, nameY, namePaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(200, widthMeasureSpec);
        int height = measureDimension(200, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    public int measureDimension(int defaultSize, int measureSpec){
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = defaultSize;   //UNSPECIFIED
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    public void setName(String name){
        this.label_name = name;
        invalidate();
    }

    public void setValue(String value){
        this.label_value = value;
        invalidate();
    }


}