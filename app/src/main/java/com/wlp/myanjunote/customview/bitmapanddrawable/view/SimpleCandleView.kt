package com.wlp.myanjunote.customview.bitmapanddrawable.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.bitmapanddrawable.drawable.CandleDrawable

class SimpleCandleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  val drawable = CandleDrawable()

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    // 绘制蜡烛图
    drawable.setBounds(10,20,width,height)
    drawable.draw(canvas)

    // 绘制额外的简单信息
  }
}