package com.wlp.myanjunote.customview.touchmultitouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wlp.myanjunote.customview.dp
import com.wlp.myanjunote.customview.getAvatar

/**
 * 多点触控的原理与写法全解析
1、多点触控的三种类型
1）接力型：那个手指后来谁有用

2）配合型

3）各自为战型：比如画图程序，多指都可以画图；

2、事件序列的模型
p(x, y, index, id)
各种MotionEvent

 单指拖动图片，只需要管MotionEvent.ACTION_DOWN、MotionEvent.ACTION_MOVE

 todo 结合这里学到的知识，进行一些复杂一点的自定义控件，备面试会使用
 * */
class MultiTouchView0(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, 200.dp.toInt())
    private var originalOffsetX = 0f//初始偏移
    private var originalOffsetY = 0f

    private var offsetX = 0f//图片偏移的坐标
    private var offsetY = 0f

    private var downX = 0f//按下的坐标
    private var downY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x //event.x 与 event.getX(index)的区别
                downY = event.y
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                //偏移的坐标 = 当前移动的坐标 - 按下的坐标 + 初始偏移
                //就是手按在图片上，手滑动了多少，就移动多少
                offsetX = event.x - downX + originalOffsetX
                offsetY = event.y - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}