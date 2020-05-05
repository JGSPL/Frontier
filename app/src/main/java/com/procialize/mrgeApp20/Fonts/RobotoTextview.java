package com.procialize.mrgeApp20.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Naushad on 11/1/2017.
 */

public class RobotoTextview extends AppCompatTextView {

    public RobotoTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Regular.ttf");
        setTypeface(tf, 1);


    }

}

//    Roboto-Light.ttf

