package com.wlp.myanjunote.customview.touch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import kotlin.math.abs


class TouchLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    /**
     * 一个是onLayout()，用来布局子控件.
     * ViewGroup类的onLayout()函数是abstract型，继承者必须实现，由于ViewGroup的定位就是一个容器，用来盛放子控件
     * 的，所以就必须定义要以什么的方式来盛放。比如LinearLayout就是以横向或者纵向顺序存放，而RelativeLayout则以
     * 相对位置来摆放子控件，同样，我们的自定义ViewGroup也必须给出我们期望的布局方式，而这个定义就通过onLayout()
     * 函数来实现。
     *
     * 当ViewGroup的位置被确定后，它在onLayout中会遍历所有的子元素并调用其layout方法，在layout方法中又会调用onLayout
     * 方法。layout和onLayout区别：layout方法确定view本身的位置，onLayout确定所有子元素的位置。
     *
     * 参数：
     * @param changed 该参数指出当前ViewGroup的尺寸或者位置是否发生了改变
     * @param left top right bottom 当前ViewGroup相对于其父控件的坐标位置
     *
     **/
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 自定义容器的时候, 如果不需要滚动, 那么需要重写 shouldDelayChildPressedState(),并且返回 false；
     * 这个方法如果要滚动的话, 会延迟 press 的状态。
     **/
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    /**
     * dispatchTouchEvent是处理触摸事件分发,事件(多数情况)是从Activity的dispatchTouchEvent开始的。
     * 执行super.dispatchTouchEvent(ev)，事件向下分发。
     *
     * onInterceptTouchEvent是ViewGroup提供的方法，默认返回false，返回true表示拦截。
     *
     * onTouchEvent是View中提供的方法，ViewGroup也有这个方法，view中不提供onInterceptTouchEvent。
     * view中默认返回true，表示消费了这个事件。
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
//        val delta = ev.y - ??
          //如果超过某个阈值，就拦截. 一旦有拦截操作，那么就要重写onTouchEvent()方法
//        if (abs(delta) > SLOP) {
//            return true
//        } else {
//            return false //不拦截
            //一般情况，都是先不拦截的，先放过去给子view，然后一直判断，到达某个阈值时，才拦截
            //如果说，某一触摸事件一开始就无脑拦截了，子view还有什么机会啊
            //而且，很多时候，下面的onTouchEvent()也是达不到，因为会被子view消费掉，只有在这个ViewGroup
            //拦截后，再会去调用这个onTouchEvent(),或者走过了所有子view的onTouchEvent()都没消费，然后到了
            //本ViewGroup的onTouchEvent()
//        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        ???
//        return ???
      return true
    }
}