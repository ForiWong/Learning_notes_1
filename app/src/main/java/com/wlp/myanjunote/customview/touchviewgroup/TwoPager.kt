package com.wlp.myanjunote.customview.touchviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

//其实挺难的，需要知道的系统API多啊
class TwoPager(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
  private var downX = 0f
  private var downY = 0f
  private var downScrollX = 0f
  private var scrolling = false

  private val overScroller: OverScroller = OverScroller(context)

  private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
  private val velocityTracker = VelocityTracker.obtain()//速度追踪，初始化
  private var minVelocity = viewConfiguration.scaledMinimumFlingVelocity//速度下限
  private var maxVelocity = viewConfiguration.scaledMaximumFlingVelocity
  private var pagingSlop = viewConfiguration.scaledPagingTouchSlop

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    measureChildren(widthMeasureSpec, heightMeasureSpec)//给所有的子view一个统一的宽高限制，很少用
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft = 0
    val childTop = 0
    var childRight = width
    val childBottom = height
    for (child in children) {
      child.layout(childLeft, childTop, childRight, childBottom)
      childLeft += width //这个为什么这样啊？一直往右加吗
      childRight += width
    }
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()
    }
    velocityTracker.addMovement(event)
    var result = false
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        scrolling = false
        downX = event.x
        downY = event.y
        downScrollX = scrollX.toFloat()
      }
      MotionEvent.ACTION_MOVE -> if (!scrolling) {
        val dx = downX - event.x
        if (abs(dx) > pagingSlop) {//大于每个阈值，我们就认为他要滑动的是父view了，就拦截它
          scrolling = true
          parent.requestDisallowInterceptTouchEvent(true)//这里拦截了子view，一般本view也会要告诉父view不要拦截你了
          result = true //拦截
        }
      }
    }
    return result
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()//清楚
    }
    velocityTracker.addMovement(event)//更新
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = event.x
        downY = event.y
        downScrollX = scrollX.toFloat()
      }
      MotionEvent.ACTION_MOVE -> {
        val dx = (downX - event.x + downScrollX).toInt()//这个值是反着来的，而不是event.x - downX
          .coerceAtLeast(0)//下限
          .coerceAtMost(width)//上限 coerce强制的
        scrollTo(dx, 0)//滑动
      }
      MotionEvent.ACTION_UP -> {
        velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat()) // 5m/s 5km/h  计算当前速度（单位， 速度上限）
        val vx = velocityTracker.xVelocity
        val scrollX = scrollX
        val targetPage = if (abs(vx) < minVelocity) {//速度小的时候，看滑动的距离
          if (scrollX > width / 2) 1 else 0
        } else {//速度大的时候，就看速度值的正负，速度是负的，就是向左滑
          if (vx < 0) 1 else 0
        }
        val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX //滑动距离
        overScroller.startScroll(getScrollX(), 0, scrollDistance, 0)
        postInvalidateOnAnimation()//？？
      }
    }
    return true
  }

  override fun computeScroll() { //overScroller
    if (overScroller.computeScrollOffset()) {
      scrollTo(overScroller.currX, overScroller.currY)
      postInvalidateOnAnimation()
    }
  }
}
