package com.wlp.myanjunote.customview.touchdrag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper

private const val COLUMNS = 2
private const val ROWS = 3

class DragHelperGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private var dragHelper = ViewDragHelper.create(this, DragCallback())//

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val specWidth = MeasureSpec.getSize(widthMeasureSpec)
    val specHeight = MeasureSpec.getSize(heightMeasureSpec)
    val childWidth = specWidth / COLUMNS
    val childHeight = specHeight / ROWS
    measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),//测子view，其实这里是个网格布局
      MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
    setMeasuredDimension(specWidth, specHeight)//给自己设
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in children.withIndex()) {
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)//布局
    }
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return dragHelper.shouldInterceptTouchEvent(ev)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dragHelper.processTouchEvent(event)
    return true
  }

  override fun computeScroll() {
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this)
    }
  }

  private inner class DragCallback : ViewDragHelper.Callback() {
    var capturedLeft = 0f
    var capturedTop = 0f

    //必须实现的方法 试着抓住view
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return true
    }

    //？？
    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
      return left
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return top
    }

    //当view被抓住回调
    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
      capturedChild.elevation = elevation + 1
      capturedLeft = capturedChild.left.toFloat()
      capturedTop = capturedChild.top.toFloat()
    }

    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
    }

    //当view被松开回调
    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      dragHelper.settleCapturedViewAt(capturedLeft.toInt(), capturedTop.toInt())
      postInvalidateOnAnimation()
    }
  }
}
