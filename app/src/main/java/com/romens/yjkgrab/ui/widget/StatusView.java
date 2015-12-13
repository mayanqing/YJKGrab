package com.romens.yjkgrab.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.romens.yjkgrab.R;
import com.romens.yjkgrab.utils.DimenHelper;

/**
 * Created by myq on 15-12-10.
 */
public class StatusView extends View {
    private Paint backPaint, textPaint;


    private int backColor = 0xff3F51B5;

    private int textColor = Color.WHITE;

    private String text;

    private float textSize = 13;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        init();
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        backColor = typedArray.getColor(R.styleable.StatusView_backColor, backColor);
        textColor = typedArray.getColor(R.styleable.StatusView_textColor, textColor);
        text = typedArray.getString(R.styleable.StatusView_text);
        text = text == null ? "测试的" : text;
        textSize = DimenHelper.sp(typedArray.getDimension(R.styleable.StatusView_textSize, 13), context);
        typedArray.recycle();
    }

    private void init() {
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backColor);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawTextBack(canvas);
        drawText(canvas);
        canvas.restore();
    }

    private void drawTextBack(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth(), getHeight() / 2);
        path.close();
        canvas.drawPath(path, backPaint);
    }

    private void drawText(Canvas canvas) {
        Path textPath = new Path();
        //文字所在基准线线的起点和终点坐标
        float startX = getWidth() / 5.0f, startY = 0, targetX = getWidth(), targetY = getHeight() / 2 * 4 / 5.0f;
        //文字所在线的长度
        double textLineLength = Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetY - startY, 2));

        //计算文字长度
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        //基准线线起点距离文字起点的距离占基准线长的比例
        double percent = ((textLineLength - bounds.width()) / 2) / textLineLength;

        float width = targetX - startX, height = targetY - startY;

        textPath.moveTo((float) (startX + width * percent), (float) (startY + height * percent));
        textPath.lineTo((float) (targetX - width * percent), (float) (targetY - height * percent));

        canvas.drawTextOnPath(text, textPath, 0, 0, textPaint);
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        backPaint.setColor(backColor);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = DimenHelper.sp(textSize, getContext());
        textPaint.setTextSize(textSize);
        invalidate();
    }
}
