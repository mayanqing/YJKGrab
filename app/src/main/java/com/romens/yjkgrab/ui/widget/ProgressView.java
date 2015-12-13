package com.romens.yjkgrab.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.romens.yjkgrab.R;
import com.romens.yjkgrab.utils.DimenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgressView extends View {
    /**
     * 目标数目,总共有几步
     */
    private int targetNum = 4;
    private Paint circlePickedPaint, circleUnPickPaint, linePickedPaint, lineUnPickPaint;
    private Paint stepTextPaint, descPaint;
    private float textSize = 15, descSize = 15;
    /**
     * 每一步对应时间点的map
     */
    private HashMap<Integer, String> descMap = new HashMap<>();
    private int pickedColor = Color.RED, unPickColor = Color.BLACK, stepTextColor = Color.WHITE, descColor = Color.BLACK;
    /**
     * 当前处于那一阶段
     */
    private int currentStep = 3;
    /**
     * 是否显示时间点
     */
    private boolean showdesc = true;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        init();
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        targetNum = typedArray.getInt(R.styleable.ProgressView_targetNum, targetNum);
        currentStep = typedArray.getInt(R.styleable.ProgressView_currentStep, currentStep);
        showdesc = typedArray.getBoolean(R.styleable.ProgressView_showdesc, showdesc);
        textSize = DimenHelper.sp(typedArray.getDimension(R.styleable.ProgressView_stepSize, textSize), context);
        descSize = DimenHelper.sp(typedArray.getDimension(R.styleable.ProgressView_descSize, descSize), context);
        pickedColor = typedArray.getColor(R.styleable.ProgressView_pickedColor, pickedColor);
        unPickColor = typedArray.getColor(R.styleable.ProgressView_unPickColor, unPickColor);
        stepTextColor = typedArray.getColor(R.styleable.ProgressView_stepTextColor, stepTextColor);
        descColor = typedArray.getColor(R.styleable.ProgressView_descColor, descColor);
        typedArray.recycle();
    }

    private void init() {
        circlePickedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circleUnPickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePickedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePickedPaint.setStrokeWidth(5);
        lineUnPickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lineUnPickPaint.setStrokeWidth(5);
        stepTextPaint = new Paint();
        stepTextPaint.setStrokeWidth(3);
        stepTextPaint.setTextSize(textSize);

        stepTextPaint.setTextAlign(Paint.Align.LEFT);
        descPaint = new Paint();
        descPaint.setStrokeWidth(3);
        descPaint.setTextSize(descSize);
        descPaint.setTextAlign(Paint.Align.LEFT);

        linePickedPaint.setColor(pickedColor);
        circlePickedPaint.setColor(pickedColor);
        circleUnPickPaint.setColor(unPickColor);
        lineUnPickPaint.setColor(unPickColor);
        stepTextPaint.setColor(stepTextColor);
        descPaint.setColor(descColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int itemWidth = getWidth() / targetNum;
        int itemHeight = getHeight() / (showdesc ? 2 : 1);
        ArrayList<CirclePoint> circlePoints = new ArrayList<>();
        for (int i = 0; i < targetNum; i++) {
            CirclePoint circlePoint = new CirclePoint();
            circlePoint.cx = (itemWidth / 2 + i * itemWidth);
            circlePoint.cy = itemHeight / 2;
            circlePoint.radius = itemHeight / 5;
            circlePoints.add(circlePoint);
        }
        for (int i = 0; i < targetNum; i++) {
            CirclePoint circlePoint1 = circlePoints.get(i);
            if (i == targetNum - 1) {
                drawCircle(canvas, circlePoint1, i);
                drawText(canvas, String.valueOf(1 + i), circlePoint1);
                drawDesc(canvas, circlePoint1, i, itemHeight);
                break;
            }
            CirclePoint circlePoint2 = circlePoints.get(i + 1);
            canvas.drawLine(circlePoint1.cx, circlePoint1.cy, circlePoint2.cx, circlePoint2.cy, i <= currentStep - 2 ? linePickedPaint : lineUnPickPaint);
            drawCircle(canvas, circlePoint1, i);
            drawText(canvas, String.valueOf(1 + i), circlePoint1);
            drawDesc(canvas, circlePoint1, i, itemHeight);

        }
        canvas.restore();
    }

    private void drawCircle(Canvas canvas, CirclePoint circlePoint, int step) {
        canvas.drawCircle(circlePoint.cx, circlePoint.cy, circlePoint.radius, step <= currentStep - 1 ? circlePickedPaint : circleUnPickPaint);
    }


    private void drawDesc(Canvas canvas, CirclePoint circlePoint, int step, int itemHeight) {
        if (!isShowDesc())
            return;
        String desc = descMap.get(step + 1);
        desc = desc == null ? "" : desc;
        Rect bounds = new Rect();
        stepTextPaint.getTextBounds(desc, 0, desc.length(), bounds);
        Paint.FontMetricsInt fontMetrics = stepTextPaint.getFontMetricsInt();
        int baseline = -fontMetrics.bottom / 2 + fontMetrics.top / 2 - fontMetrics.top;
        canvas.drawText(desc, circlePoint.cx - bounds.width() / 2, circlePoint.cy + itemHeight + baseline, descPaint);
    }

    private void drawText(Canvas canvas, String textStr, CirclePoint circlePoint) {
        Rect bounds = new Rect();
        stepTextPaint.getTextBounds(textStr, 0, textStr.length(), bounds);
        Paint.FontMetricsInt fontMetrics = stepTextPaint.getFontMetricsInt();
        int baseline = -fontMetrics.bottom / 2 + fontMetrics.top / 2 - fontMetrics.top;
        canvas.drawText(textStr, circlePoint.cx - bounds.width() / (TextUtils.equals("1", textStr) ? 1 : 1.5f), circlePoint.cy + baseline, stepTextPaint);
    }

    public boolean isShowDesc() {
        return showdesc;
    }

    public void setShowDesc(boolean showdesc) {
        this.showdesc = showdesc;
        invalidate();
    }

    public int getPickedColor() {
        return pickedColor;
    }

    public void setPickedColor(int pickedColor) {
        this.pickedColor = pickedColor;
        circlePickedPaint.setColor(pickedColor);
        linePickedPaint.setColor(pickedColor);
        invalidate();
    }

    public int getUnPickColor() {
        return unPickColor;
    }

    public void setUnPickColor(int unPickColor) {
        this.unPickColor = unPickColor;
        circleUnPickPaint.setColor(unPickColor);
        lineUnPickPaint.setColor(unPickColor);
        invalidate();
    }

    public int getStepTextColor() {
        return stepTextColor;
    }

    public void setStepTextColor(int stepTextColor) {
        this.stepTextColor = stepTextColor;
        stepTextPaint.setColor(stepTextColor);
        invalidate();
    }

    public int getDescColor() {
        return descColor;
    }

    public void setDescColor(int descColor) {
        this.descColor = descColor;
        descPaint.setColor(descColor);
        invalidate();
    }

    public void setDescByStep(int step, String desc) {
        descMap.put(step, desc);
        invalidate();
    }

    public void setDescMap(HashMap<Integer, String> descMap) {
        this.descMap.putAll(descMap);
        invalidate();
    }


    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep > targetNum ? targetNum : currentStep < 0 ? 0 : currentStep;
        invalidate();
    }

    public int getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum < 1 ? 1 : targetNum;
        invalidate();
    }

    public float getDescSize() {
        return descSize;
    }

    public void setDescSize(float descSize) {

        this.descSize = DimenHelper.sp(descSize, getContext());
        descPaint.setTextSize(descSize);
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = DimenHelper.sp(textSize, getContext());
        stepTextPaint.setTextSize(textSize);
        invalidate();
    }

    class CirclePoint {
        public int cx;
        public int cy;
        public int radius;
    }
}
