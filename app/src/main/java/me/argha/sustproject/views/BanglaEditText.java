package me.argha.sustproject.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class BanglaEditText extends EditText {

    public BanglaEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BanglaEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BanglaEditText(Context context) {
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
