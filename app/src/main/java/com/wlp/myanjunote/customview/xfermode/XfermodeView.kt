package com.wlp.myanjunote.customview.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.dp

/**
（1）为什么要 Xfermode？为了把多次绘制进⾏「合成」，例如蒙版效果：⽤ A 的形状和 B 的图案
怎么做？
Canvs.saveLayer() 把绘制区域拉到单独的离屏缓冲⾥
绘制 A 图形
⽤ Paint.setXfermode() 设置 Xfermode
绘制 B 图形
⽤ Paint.setXfermode(null) 恢复 Xfermode
⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域

（2）为什么要⽤ saveLayer()才能正确绘制？
为了把需要互相作⽤的图形放在单独的位置来绘制，不会受 View 本身的影响。
如果不使⽤ saveLayer()，绘制的⽬标区域将总是整个 View 的范围，两个图形
的交叉区域就错误了。
 * */
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

class XfermodeView(context: Context?, attrs: AttributeSet?) :
  View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bounds = RectF(150f.dp, 50f.dp, 300f.dp, 200f.dp)
  //创建圆和方的bitmap
  private val circleBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)
  private val squareBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)

  init {
    //在创建的bitmap上画圆、画方
    //画出了圆后面的背景的透明部分
    val canvas = Canvas(circleBitmap)
    paint.color = Color.parseColor("#D81B60")
    canvas.drawOval(50f.dp, 0f.dp, 150f.dp, 100f.dp, paint)

    paint.color = Color.parseColor("#2196F3")
    canvas.setBitmap(squareBitmap)
    canvas.drawRect(0f.dp, 50f.dp, 100f.dp, 150f.dp, paint)
  }

  override fun onDraw(canvas: Canvas) {
    val count = canvas.saveLayer(bounds, null)//saveLayer() 离屏缓冲
    /**
     * 为什么要这样做啊？为什么在init内通过绘制drawOval -> 转变为Bitmap，再在这里drawBitmap呢？
     * 为什么不是直接在onDraw()方法内部drawOval
     */
    canvas.drawBitmap(circleBitmap, 150f.dp, 50f.dp, paint)//圆 DST 目标
    paint.xfermode = XFERMODE   //
    canvas.drawBitmap(squareBitmap, 150f.dp, 50f.dp, paint)//方  SRC 源
    paint.xfermode = null   //
    canvas.restoreToCount(count)
  }
}