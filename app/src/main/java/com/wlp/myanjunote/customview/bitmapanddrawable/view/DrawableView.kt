package com.wlp.myanjunote.customview.bitmapanddrawable.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.bitmapanddrawable.drawable.MeshDrawable
import com.wlp.myanjunote.customview.dp

class DrawableView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  //private val drawable = ColorDrawable(Color.RED)//一个颜色的Drawable
  private val drawable = MeshDrawable()

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //drawable的绘制
    drawable.setBounds(50.dp.toInt(), 50.dp.toInt(), 250.dp.toInt(), 250.dp.toInt())//设置绘制边界
    drawable.draw(canvas)//把Drawable中设置的绘制内容绘制到canvas中
  }
}