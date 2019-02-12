package com.example.customindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

public class Indicator extends View {

    private static final float START_BORDER_ANGEL = 150;
    private static final float SWEEP_BORDER_ANGEL = sweepAngel(START_BORDER_ANGEL);

    private static final float START_SCALE_ANGEL = 160;
    private static final float SWEEP_SCALE_ANGEL = sweepAngel(START_SCALE_ANGEL);

    private List<Sector> sectors;

    private int width;
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
        sectors = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);

        cx = width/2f *0.95f;
        cy = heigth /2f *0.95f;

        borderRadius = Math.min(cx, cy);
        scaleRadius = Math.round(borderRadius*0.8);
        centerRadius = Math.round(borderRadius*0.1);
        arrowLength = Math.round(borderRadius*0.9);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float shadowRadius = width/60f;
        float shadowX = cx/30;
        float shadowY = cy/30;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(width/60f);

        paint.setShadowLayer(shadowRadius, shadowX, shadowY, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);

        oval.set(cx - borderRadius, cy - borderRadius, cx + borderRadius, cy + borderRadius);
        canvas.drawArc(oval, START_BORDER_ANGEL, SWEEP_BORDER_ANGEL, false, paint);

        paint.setShadowLayer(0.0f, shadowX, shadowY, Color.GRAY);

        paint.setStyle(Paint.Style.STROKE);
        //paint.setColor(Color.GREEN);
        paint.setStrokeWidth(borderRadius/10);

        oval.set(cx - scaleRadius, cy - scaleRadius, cx + scaleRadius, cy + scaleRadius);
        float a = SWEEP_SCALE_ANGEL/(maxValue - minValue);
        float b = START_SCALE_ANGEL - SWEEP_SCALE_ANGEL*minValue/(maxValue - minValue);

        float a1 = SWEEP_SCALE_ANGEL/(maxValue-minValue);

        for (Sector sector:sectors){
          paint.setColor(sector.getColor());
          canvas.drawArc(oval, a*sector.getStart() + b, a1*(sector.getEnd()-sector.getStart()), false, paint);
        }
        //canvas.drawArc(oval, START_SCALE_ANGEL, SWEEP_SCALE_ANGEL, false, paint);

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
                cy + (mTextHeight /2f) + borderRadius*0.7f,
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

    public void addSector(Sector sector){
        sectors.add(sector);
        invalidate();
    }

    static class Sector{
        private int start;
        private int end;
        private int color;

        Sector(int start, int end, int color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }

        int getStart() {
            return start;
        }

        int getEnd() {
            return end;
        }

        int getColor() {
            return color;
        }
    }

}