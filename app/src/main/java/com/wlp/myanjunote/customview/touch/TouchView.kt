package com.wlp.myanjunote.customview.touch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
触摸反馈原理全解析

更重要的去了解触摸反馈整体的物理模型。
概念模型才是重要的，比读源码重要。知道原理模型读源码也就不会一头雾水了。

【1】自定义单个View 的触摸反馈
1.重写 onTouchEvent()，在方法内部定制触摸反馈算法
1）是否消费事件取决于 ACTION_DOWN 事件是否返回 true
2）MotionEvent
# getActionMasked() 和 getAction() 怎么选？
## 选 getActionMasked()。因为到了多点触控时代， getAction() 已经不够准确。
## 那为什么有些地方（包括 Android 源码里）依然在用getAction()？ 因为它们的场景不考虑多点触控
# POINTER_DOWN / POINTER_UP：多点触控时的事件
## getActionIndex()：多点触控时用到的方法
## 关于 POINTER_DOWN、 POINTER_UP 和 getActionIndex()，后面多点触控的课程里会详细讲


如何解决ViewGroup里面有子View，且子View是平行或重叠情况下，用户点击事件的穿透如何处理。
消费。

比如MotionEvent做一个按下、抬起的操作，DOWN、UP，这两个事件其实是一组事件序列。
实际上，我们都是按一个序列来分析的。
每个事件序列都是以Down开始的，以UP或CANCEL结束的。

return true
//表示我要消费这组事件序列，而不是这个事件。
或者说我要消费这个事件，以及消费这个事件在这个序列后续的所有事件。

其实是两个意思
（1）一个子View消费了这个事件后，调用onTouchEvent()内部的方法之后，就不会往下传了。
（2）并且，这个事件序列之后事件也是被消费了。
此外，只在DOWN事件时候返回，其他事件不判断。
return TRUE 作用等同于event.actionMasked == MotionEvent.ACTION_DOWN

也就是你宣布消费所有权只跟DOWN有关，只能在DOWN事件时宣布消费TRUE，或者FALSE。


MotionEvent.ACTION_UP
MotionEvent.ACTION_DOWN
MotionEvent.ACTION_CANCEL
MotionEvent.ACTION_MOVE

MotionEvent.ACTION_POINTER_DOWN  //非第一根手指的按下
MotionEvent.ACTION_POINTER_UP //非第一根手指的抬起

MotionEvent.ACTION_POINTER_DOWN_3


点击子view，还是要滑动父view



【2】View.onTouchEvent() 的源码逻辑
//看View源码

1）当用户按下（ACTION_DOWN）：
如果不在滑动控件中，切换至按下状态，并注册长按计时器；
如果在滑动控件中，切换至预按下状态，并注册按下计时器；
2）当进入按下状态并移动（ACTION_MOVE）：
重绘 Ripple Effect；
如果移动出自己的范围，自我标记本次事件失效，忽略后续事件；
3）当用户抬起（ACTION_UP）：
如果是按下状态并且未触发长按，切换至抬起状态并触发点击事件，并清除一切状态；
如果已经触发长按，切换至抬起状态并清除一切状态；
4）当事件意外结束（ACTION_CANCEL）：
切换至抬起状态，并清除一切状态。
Tool Tip：新版 Android 加入的「长按提示」功能。


【3】自定义 ViewGroup 的触摸反馈
拦截不拦截、消费不消费

1）除了重写 onTouchEvent() ，还需要重写 onInterceptTouchEvent()
2）onInterceptTouchEvent() 不用在第一时间返回 true，而是在任意一个事件里，
需要拦截的时候返回 true 就行
3）在 onInterceptTouchEvent() 中除了判断拦截，还要做好拦截之后的准备
工作（主要和 onTouchEvent() 的代码逻辑一致）
【因为开始一部分事件的onTouchEvent()都没有触发，只有在拦截之后才会被触发，所有之前的
事件的一些信息要记录下来，比如最开始的按下操作的坐标，这个就叫做准备工作
比如，一个滑动父控件里面有子view，用户的滑动事件是在子view点击的呢，还是在父view点击的呢？
都是有可能的。
】

一旦被拦截后，就会再经过子view处理了

【4】触摸反馈的流程】
Activity.dispatchTouchEvent()
    递归: ViewGroup(View).dispatchTouchEvent() //一般情况这个方法都不需要重写
        ViewGroup.onInterceptTouchEvent()
        child.dispatchTouchEvent()
        super.dispatchTouchEvent()
            View.onTouchEvent()
    Activity.onTouchEvent()

【5】View.dispatchTouchEvent()】
1）如果设置了 OnTouchListener，调用 OnTouchListener.onTouch()
   如果 OnTouchListener 消费了事件，返回 true
   如果 OnTouchListener 没有消费事件，继续调用自己的 onTouchEvent()，并返回和 onTouchEvent() 相同的结果
2）如果没有设置 OnTouchListener，同上

【6】ViewGroup.dispatchTouchEvent()】
1）如果是用户初次按下（ACTION_DOWN），清空 TouchTargets 和DISALLOW_INTERCEPT 标记
2）拦截处理
3）如果不拦截并且不是 CANCEL 事件，并且是 DOWN 或者 POINTER_DOWN，尝试把 pointer（手指）通过 TouchTarget
分配给子View；并且如果分配给了新的子 View，调用 child.dispatchTouchEvent()把事件传给子 View
4）看有没有 TouchTarget
  如果没有，调用自己的 super.dispatchTouchEvent()
  如果有，调用 child.dispatchTouchEvent() 把事件传给对应的子 View（如果有的话）
5）如果是 POINTER_UP，从 TouchTargets 中清除 POINTER 信息；如果是 UP 或CANCEL，重置状态

【7】TouchTarget】
作用：记录每个子View 是被哪些 pointer（手指）按下的
结构：单向链表

 */
class TouchView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  override fun onTouchEvent(event: MotionEvent): Boolean {
    //如果直接返回true,就不没有调用设置的点击事件了，长按也没有用
    //return true

    //return super.onTouchEvent(event);

    if (event.actionMasked == MotionEvent.ACTION_UP) {//如果触发抬起事件，就认为是点击了
      performClick()//执行点击 即OnClickListener onClick()
    }

    return true //等同于event.actionMasked == MotionEvent.ACTION_DOWN
  }
}