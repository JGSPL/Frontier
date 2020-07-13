package com.procialize.mrgeApp20.CustomTools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Naushad on 1/4/2018.
 */

@SuppressLint("AppCompatCustomView")
public class VerticalTextView extends TextView {
    boolean topDown = false;


    public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity)
                && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
                    | Gravity.TOP);
            topDown = false;
        } else
            topDown = true;
    }

    public VerticalTextView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            TextPaint textPaint = getPaint();
            textPaint.setColor(getCurrentTextColor());
            textPaint.drawableState = getDrawableState();

            canvas.save();

            if (topDown) {
                canvas.translate(getWidth(), 0);
                canvas.rotate(90);
            } else {
                canvas.translate(0, getHeight());
                canvas.rotate(-90);
            }

            canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

            getLayout().draw(canvas);
            canvas.restore();
        }catch (Exception e)
        {e.printStackTrace();}
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Regular.ttf");
        setTypeface(tf, 1);

    }
}