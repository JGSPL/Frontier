package com.procialize.mrgeApp20.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Naushad on 11/1/2017.
 */

@SuppressLint("AppCompatCustomView")
public class RobotoEditext extends EditText {

    public RobotoEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoEditext(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Light_13935.ttf");
        setTypeface(tf, 1);

    }

}