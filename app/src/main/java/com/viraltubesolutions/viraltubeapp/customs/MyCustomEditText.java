package com.viraltubesolutions.viraltubeapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Shashi on 10/5/2017.
 */

public class MyCustomEditText extends AppCompatEditText {
    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public MyCustomEditText(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public MyCustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        init();
    }

    public MyCustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.attrs=attrs;
        this.defStyle=defStyle;
        init();
    }

    private void init() {
        Typeface font=Typeface.createFromAsset(getContext().getAssets(), "Fonts/candara.ttf");
        this.setTypeface(font);
    }
    @Override
    public void setTypeface(Typeface tf, int style) {
        tf= Typeface.createFromAsset(getContext().getAssets(), "Fonts/candara.ttf");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf=Typeface.createFromAsset(getContext().getAssets(), "Fonts/candara.ttf");
        super.setTypeface(tf);
    }
}
