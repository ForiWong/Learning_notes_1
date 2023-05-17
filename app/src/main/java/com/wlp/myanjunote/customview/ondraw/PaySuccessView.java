package com.wlp.myanjunote.customview.ondraw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

public class PaySuccessView extends View {
    private Path mCirclePath, mDstPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Float mCurAnimValue;
    private int mCentX = 100;
    private int mCentY = 100;
    private int mRadius = 50;

    public PaySuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.BLACK);

        mCirclePath = new Path();//完整的路径
        mDstPath = new Path();//绘制的目标路径

        //画圆
        mCirclePath.addCircle(mCentX, mCentY, mRadius, Path.Direction.CW);
        //画对勾
        mCirclePath.moveTo(mCentX - mRadius / 2, mCentY);
        mCirclePath.lineTo(mCentX, mCentY + mRadius / 2);
        mCirclePath.lineTo(mCentX + mRadius / 2, mCentY - mRadius / 3);

        mPathMeasure = new PathMeasure(mCirclePath, false);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAnimValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(4000);
        animator.start();
    }

    boolean mNext = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        //mCurAnimValue 在0 -》 2
        if (mCurAnimValue < 1) {
            float stop = mPathMeasure.getLength() * mCurAnimValue;
            //从Path中来截取指定的一段路径
            mPathMeasure.getSegment(0, stop, mDstPath, true);
        } else {
            if (!mNext) {//当mCurAnimValue首次>1，需要跳转到下一条线
                mNext = true;
                //如果注释下面这一行，路径会出现一个小缺口
                mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDstPath, true);
                mPathMeasure.nextContour();  //移动到下一条曲线函数
                //mDstPath.reset(); //getSegment是不断往上添加到mDstPath。调用一下reset(),就知道差异了
            }
            float stop = mPathMeasure.getLength() * (mCurAnimValue - 1);
            mPathMeasure.getSegment(0, stop, mDstPath, true);
        }
        canvas.drawPath(mDstPath, mPaint);
    }
}