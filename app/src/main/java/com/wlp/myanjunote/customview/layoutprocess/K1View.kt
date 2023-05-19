package com.wlp.myanjunote.customview.layoutprocess

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

private const val TAG = "K1View"

class K1View(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    Log.d(TAG, "onMeasure")
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  override fun layout(l: Int, t: Int, r: Int, b: Int) {
    Log.d(TAG, "layout")
    super.layout(l, t, r, b)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    Log.d(TAG, "onLayout")
    super.onLayout(changed, left, top, right, bottom)
  }

  override fun onDraw(canvas: Canvas?) {
    Log.d(TAG, "onDraw")
    super.onDraw(canvas)
  }
}