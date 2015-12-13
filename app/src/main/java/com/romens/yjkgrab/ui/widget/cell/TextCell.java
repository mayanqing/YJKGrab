package com.romens.yjkgrab.ui.widget.cell;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.romens.yjkgrab.utils.DimenHelper;

/**
 * Created by myq on 15-12-9.
 */
public class TextCell extends LinearLayout {


    private TextView mTextView;
    private String mContent = "";
    private int textColor = Color.BLACK;
    private float textSize = 16;

    public TextCell(Context context) {
        this(context, null);
    }

    public TextCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextView = new TextView(getContext());
        setTextViewPadding(1, 1, 1, 1);
        addView(mTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setTextViewPadding(float left, float top, float right, float bottom) {
        if (mTextView == null)
            return;
        mTextView.setPadding(DimenHelper.dp(left, getContext()), DimenHelper.dp(top, getContext()), DimenHelper.dp(right, getContext()), DimenHelper.dp(bottom, getContext()));
    }

    public String getText() {
        return mContent;
    }

    public void setText(int resId) {
        this.mContent = getContext().getString(resId);
        mTextView.setText(mContent);
    }

    public void setText(String mContent) {
        this.mContent = mContent;
        mTextView.setText(mContent);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mTextView.setTextColor(textColor);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        mTextView.setTextSize(textSize);
    }
}
