package com.wlp.myanjunote.customview.ondraw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
    private Path mCirclePath, mDstPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Float mCurAnimValue;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.BLACK);

        mDstPath = new Path();
        mCirclePath = new Path();
        mCirclePath.addCircle(100, 100, 50, Path.Direction.CW);

        mPathMeasure = new PathMeasure(mCirclePath, true);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAnimValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float length = mPathMeasure.getLength();
        float stop = length * mCurAnimValue;
        float start = (float) (stop - ((0.5 - Math.abs(mCurAnimValue - 0.5)) * length));
        mDstPath.reset();//清空之前的
        canvas.drawColor(Color.WHITE);
        // 用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,并将截取后的path保存到参数dst中,
        // 最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,通常设置为true
        mPathMeasure.getSegment(start, stop, mDstPath, true);

        canvas.drawPath(mDstPath, mPaint);
    }
}