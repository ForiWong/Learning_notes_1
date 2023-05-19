package com.wlp.myanjunote.customview.animation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.dp

class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  var radius = 50.dp//不用private
    set(value) {//这个是什么语法 相当于setter()方法
      field = value //设置属性值
      invalidate()
    // Invalidate the whole view. If the view is visible,
    // 如果view是可见的，就会让控件失效，到下一帧的时候就会被系统重新绘制
    }

  init {
    paint.color = Color.parseColor("#00796B")
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    canvas.drawCircle(width / 2f, height / 2f, radius, paint)
  }
}