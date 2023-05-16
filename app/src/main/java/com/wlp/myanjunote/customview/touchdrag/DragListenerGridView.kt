package com.wlp.myanjunote.customview.touchdrag

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.*

private const val COLUMNS = 2
private const val ROWS = 3

/**
自定义触摸算法之拖拽 API 详解
1、OnDragListener
（1）通过 startDrag() 来启动拖拽
（2）用setOnDragListener() 来监听
※ OnDragListener 内部只有一个方法： onDrag()
※ onDragEvent() 方法也会收到拖拽回调（界面中的每个 View 都会收到）

2、ViewDragHelper
※ 需要创建一个 ViewDragHelper 和 Callback()
※ 需要写在 ViewGroup 里面，重写 onIntercept() 和 onTouchevent()

3、为什么要这两个东西，而不是一个？
（1）OnDragListener
※ API 11 加入的工具类，用于拖拽操作。
※ 使用场景：用户的「拖起 -> 放下」操作，重在内容的移动。可以附加拖拽数据
※ 不需要写自定义 View，使用startDrag() / startDragAndDrop() 手动开启拖拽
※ 拖拽的原理是创造出一个图像在屏幕的最上层，用户的手指拖着图像移动
可以跨进程拖拽，比如拖拽照片

（2）ViewDragHelper
※ 2015 年的 support v4 包中新增的工具类，用于拖拽操作。
※ 使用场景：用户拖动 ViewGroup 中的某个子 View
※ 需要应用在自定义 ViewGroup 中调用ViewDragHelper.shouldInterceptTouchEvent() 和 processTouchEvent()，程序会自动开启拖拽
※ 拖拽的原理是实时修改被拖拽的子 View 的 mLeft, mTop, mRight,mBottom 值

 * */
class DragListenerGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private var dragListener: OnDragListener = HenDragListener()
  private var draggedView: View? = null
  private var orderedChildren: MutableList<View> = ArrayList()

  init {
    isChildrenDrawingOrderEnabled = true
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    for (child in children) {
      orderedChildren.add(child) // 初始化位置，这个会有半透明的效果
      child.setOnLongClickListener { v -> //设置长按监听器
        draggedView = v
        v.startDrag(null, DragShadowBuilder(v), v, 0)//拖拽 这些参数？？
        false
      }
      child.setOnDragListener(dragListener)//每个子view监听
    }
  }

  //?
  override fun onDragEvent(event: DragEvent?): Boolean {
    return super.onDragEvent(event)

  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val specWidth = MeasureSpec.getSize(widthMeasureSpec)
    val specHeight = MeasureSpec.getSize(heightMeasureSpec)
    val childWidth = specWidth / COLUMNS
    val childHeight = specHeight / ROWS
    measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
    setMeasuredDimension(specWidth, specHeight)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in children.withIndex()) {
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.layout(0, 0, childWidth, childHeight)
      child.translationX = childLeft.toFloat()//一开始都是摆放在左上角，然后再做偏移
      child.translationY = childTop.toFloat()
    }
  }

  private inner class HenDragListener : OnDragListener {//拖一个子view，其他的子View都会收到回调
    override fun onDrag(v: View, event: DragEvent): Boolean {
      when (event.action) {
        DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {//比较是不是被拖拽的那个view
          v.visibility = View.INVISIBLE //手动隐藏
        }
        DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) {
          sort(v)//跨越边界，重排子view
        }
        DragEvent.ACTION_DRAG_EXITED -> {
        }
        DragEvent.ACTION_DRAG_ENDED -> if (event.localState === v) {
          v.visibility = View.VISIBLE
        }
      }
      return true
    }
  }

  private fun sort(targetView: View) {
    var draggedIndex = -1
    var targetIndex = -1
    for ((index, child) in orderedChildren.withIndex()) {
      if (targetView === child) {
        targetIndex = index
      } else if (draggedView === child) {
        draggedIndex = index
      }
    }
    orderedChildren.removeAt(draggedIndex)
    orderedChildren.add(targetIndex, draggedView!!)
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in orderedChildren.withIndex()) {
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.animate()
        .translationX(childLeft.toFloat())
        .translationY(childTop.toFloat())
        .setDuration(150)
    }
  }
}
