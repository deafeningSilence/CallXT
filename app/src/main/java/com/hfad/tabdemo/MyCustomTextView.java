package com.hfad.tabdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sarala on 31-08-2016.
 */
public class MyCustomTextView extends TextView {
    public MyCustomTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"jigsaw.ttf"));
    }
}
