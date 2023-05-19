package com.wlp.myanjunote.customview.bitmapanddrawable.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

/**
 * draw：绘制方法，这个自然不必说，和自定义View的绘制是一样的，相关的效果都是在这里绘制
 * setAlpha：设置Drawable的透明度，一般我们会把这个透明度传递给绘制的画笔，或者不做处理
 * setColorFilter：设置了一个颜色过滤器，那么在绘制出来之前，被绘制内容的每一个像素都会被颜色过滤器改变
 * getOpacity：获得不透明度，其值可以根据setAlpha中设置的值进行调整。
 */
class CandleDrawable : Drawable() {
  override fun draw(canvas: Canvas) {
    // 绘制蜡烛
    // 绘制基本信息
  }

  override fun setAlpha(alpha: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getOpacity(): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}