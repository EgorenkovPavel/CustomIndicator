package com.example.customindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;

import androidx.annotation.Nullable;

public class Indicator extends View {

    private static final float START_BORDER_ANGEL = 150;
    private static final float SWEEP_BORDER_ANGEL = sweepAngel(START_BORDER_ANGEL);

    private static final float START_SCALE_ANGEL = 160;
    private static final float SWEEP_SCALE_ANGEL = sweepAngel(START_SCALE_ANGEL);

    private int width;
    private int heigth;
    private float cx;
    private float cy;
    private float borderRadius;
    private float scaleRadius;
    private float centerRadius;
    private float arrowLength;

    private int minValue = 0;
    private int maxValue = 1;
    private int value = minValue;

    private Paint paint;
    private RectF oval;
    private Path arrow;
    private Rect textBoundRect;

    private static float sweepAngel(float angel){
        return 180 + (180 - angel)*2;
    }

    public Indicator(Context context) {
        super(context);
        init();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint = new Paint();
        oval = new RectF();
        arrow = new Path();
        textBoundRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        heigth = MeasureSpec.getSize(heightMeasureSpec);

        cx = width/2 *0.95f;
        cy = heigth/2 *0.95f;

        borderRadius = Math.min(cx, cy);
        scaleRadius = Math.round(borderRadius*0.8);
        centerRadius = Math.round(borderRadius*0.1);
        arrowLength = Math.round(borderRadius*0.9);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float shadowRadius = width/60;
        float shadowX = cx/30;
        float shadowY = cy/30;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(width/60);

        paint.setShadowLayer(shadowRadius, shadowX, shadowY, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);

        oval.set(cx - borderRadius, cy - borderRadius, cx + borderRadius, cy + borderRadius);
        canvas.drawArc(oval, START_BORDER_ANGEL, SWEEP_BORDER_ANGEL, false, paint);

        paint.setShadowLayer(0.0f, shadowX, shadowY, Color.GRAY);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(borderRadius/10);

        oval.set(cx - scaleRadius, cy - scaleRadius, cx + scaleRadius, cy + scaleRadius);
        canvas.drawArc(oval, START_SCALE_ANGEL, SWEEP_SCALE_ANGEL, false, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        arrow.moveTo(cx-centerRadius, cy);
        arrow.lineTo(cx, cy - arrowLength);
        arrow.lineTo(cx+centerRadius, cy);
        arrow.close();

        float angel = SWEEP_SCALE_ANGEL * value/(maxValue - minValue) - 90 - (180 - START_SCALE_ANGEL);

        float x1 = Math.round(Math.cos(angel*Math.PI/180) * shadowX);
        float y1 = Math.round(Math.sin(angel*Math.PI/180) * shadowY + shadowY/2);

        // выводим результат
        paint.setShadowLayer(shadowRadius, y1, x1, Color.GRAY);
        canvas.rotate(angel, cx,cy);
        canvas.drawPath(arrow, paint);
        canvas.rotate(-angel, cx,cy);
        paint.setShadowLayer(0.0f, shadowX, shadowY, Color.GRAY);

        paint.setTextSize(borderRadius/5);
        paint.setColor(Color.BLACK);
        String text = String.format(Locale.getDefault(), "%d%%", value*100/(maxValue - minValue));

        paint.getTextBounds(text, 0, text.length(), textBoundRect);
        float mTextWidth = paint.measureText(text);
        float mTextHeight = textBoundRect.height();

        canvas.drawText(text,
                cx - (mTextWidth / 2f),
                cy + (mTextHeight /2f) + borderRadius*0.8f,
                paint
        );
    }

    public void setMaxValue(int max){
        this.maxValue = max;
    }

    public void setMinValue(int min){
        this.minValue = min;
    }

    public void setValue(int value){
        this.value = value;
        invalidate();
    }

}