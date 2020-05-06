package com.procialize.mrgeApp20.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class RobotoTextViewHeader extends AppCompatTextView {

    public RobotoTextViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextViewHeader(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Medium.ttf");
        setTypeface(tf, 1);


    }

}
