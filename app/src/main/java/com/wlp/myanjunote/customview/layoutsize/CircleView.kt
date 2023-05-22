package com.wlp.myanjunote.customview.layoutsize

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.dp

private val RADIUS = 100.dp
private val PADDING = 10.dp

/**
（2）完全自定义 View 的尺寸
（测量过程不是在父类的基础上，完全是自己来）
重写 onMeasure()
计算出自己的尺寸
用resolveSize() 或者 resolveSizeAndState() 修正结果
使用setMeasuredDimension(width, height) 保存结果
例子：CircleView
 */
class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val size = ((PADDING + RADIUS) * 2).toInt()

    //可以看看resolveSize里面的源码，取舍算法
    val width = resolveSize(size, widthMeasureSpec)//todo resolveSize() 这个就是结合父view意见，可以尝试不同的效果
    val height = resolveSize(size, heightMeasureSpec)
    //看看 resolveSize() 或者 resolveSizeAndState() 两个方法的区别

    //MeasureSpec.getMode() 测量规范
    val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
    /**
    public static final int AT_MOST = -2147483648;
    public static final int EXACTLY = 1073741824;
    public static final int UNSPECIFIED = 0;
     */

    setMeasuredDimension(width, height)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(PADDING + RADIUS, PADDING + RADIUS, RADIUS, paint)
  }
}
