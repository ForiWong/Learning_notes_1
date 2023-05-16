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
 在单指拖动的基础上，改成多点拖动，接力型多点触控
 */
class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, 200.dp.toInt())
    private var originalOffsetX = 0f//初始偏移
    private var originalOffsetY = 0f

    private var offsetX = 0f//图片偏移的坐标
    private var offsetY = 0f

    private var downX = 0f//按下的坐标
    private var downY = 0f

    private var trackingPointerId = 0//拖动的手指id,通过id来追踪手指

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    //MotionEvent
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                trackingPointerId = event.getPointerId(0)//保存id，用于追踪。这个时候只有一个一手指，所以第0个就是可以拿到了
                downX = event.x
                downY = event.y
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex//先拿到序号，再去获取id
                trackingPointerId = event.getPointerId(actionIndex)
                downX = event.getX(actionIndex)
                downY = event.getY(actionIndex)
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            // 抬起的逻辑，如果是非最后一根手指抬起是要考虑的。
            // 最后一根手指抬起就不管了，因为它已经完成了。
            MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                if (pointerId == trackingPointerId) {
                    //接棒的操作
                    val newIndex = if (actionIndex == event.pointerCount - 1) {//假如是最大那个的手指
                        event.pointerCount - 2
                    } else {//否则，就是前一个
                        event.pointerCount - 1
                    }
                    trackingPointerId = event.getPointerId(newIndex)
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val index = event.findPointerIndex(trackingPointerId)
                //偏移的坐标 = 当前移动的坐标 - 按下的坐标 + 初始偏移
                //就是手按在图片上，手滑动了多少，就移动多少
                offsetX = event.getX(index) - downX + originalOffsetX
                offsetY = event.getY(index) - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}