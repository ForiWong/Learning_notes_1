package com.wlp.myanjunote.customview.bitmapanddrawable.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.bitmapanddrawable.drawable.CandleDrawable

class DetailedCandleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  val drawable = CandleDrawable()

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    // 绘制蜡烛图
    drawable.setBounds(0,0,200,200)
    drawable.draw(canvas)

    // 绘制额外的完整信息
  }
}