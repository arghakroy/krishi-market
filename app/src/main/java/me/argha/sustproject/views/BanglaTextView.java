package me.argha.sustproject.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class BanglaTextView extends TextView{

    public BanglaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BanglaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BanglaTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "SolaimanLipi.ttf");
            setTypeface(tf);
        }
    }
}
