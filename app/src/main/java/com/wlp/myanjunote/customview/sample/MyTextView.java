package com.wlp.myanjunote.customview.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

//属性动画，实现文本view的，文字颜色是会变化的
public class MyTextView extends View {
    private float progress = 0.0f;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        //invalidate();
        this.progress = progress;
        //Log.v("dd","progress -》"+ progress);
        invalidate();
    }

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    String text = "你好啊,欢迎阅览我的博客";

    private float TopRight;

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        super.onDraw(canvas);
        Log.v("zj", "ondraw");
        paint.setTextSize(100);
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.FILL);
        Paint.FontMetrics fm = paint.getFontMetrics();
        canvas.save();

        float drLeft = getWidth() / 2 - paint.measureText(text) / 2;
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
        canvas.drawText(text, drLeft, getHeight() / 2 - (fm.descent + fm.ascent) / 2, paint);

        canvas.restore();

        canvas.save();
        drLeft = getWidth() / 2 - paint.measureText(text) / 2;
        TopRight = (float) (drLeft + progress * paint.measureText(text));
        paint.setColor(Color.RED);
        Rect rect = new Rect((int) drLeft, 0, (int) TopRight, getHeight());
        canvas.clipRect(rect);
        canvas.drawText(text, drLeft, getHeight() / 2 - (fm.descent + fm.ascent) / 2, paint);
        canvas.restore();
    }
}
