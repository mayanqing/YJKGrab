package com.romens.yjkgrab.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by myq on 15-12-9.
 */
public class SelectView extends View {

    private boolean isSelectPick = false;

    public SelectView(Context context) {
        super(context);
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isSelectPick() {
        return isSelectPick;
    }

    public void setIsSelectPick(boolean isSelectPick) {
        this.isSelectPick = isSelectPick;
        setVisibility(isSelectPick ? VISIBLE : GONE);
    }

}
