package com.wlp.myanjunote.customview.touchscalableimageview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.wlp.myanjunote.customview.dp
import com.wlp.myanjunote.customview.getAvatar
import kotlin.math.max
import kotlin.math.min

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACTOR = 1.5f

/**
双向滑动的ScalableImageView

【1】GestureDetector】手势检测，像是view的一个外挂检测器，帮你完成了检测。
用于在点击和长按之外，增加其他手势的监听，例如双击、滑动。通过在
View.onTouchEvent() 里调用 GestureDetector.onTouchEvent() ，以代理的形式来实现:

override fun onTouchEvent(event: MotionEvent): Boolean{
    return gestureDetector.onTouchEvent(event)
}

【2】GestureDetector 的默认监听器：】
OnGestureListener
通过构造⽅法 GestureDetector(Context, OnGestureListener) 来配置:

private gestureDetector = GestureDetectorCompat(context, gestureListener)

OnGestureListener的几个回调方法：

override fun onDown(e: MotionEvent): Boolean {
//每次 ACTION_DOWN 事件出现的时候都会被调用，在这里返回 true可以保证必然消费掉事件
return true
}

override fun onShowPress(e: MotionEvent) {
//用户按下 100ms 不松手后会被调用，用于标记「可以显示按下状态了」
}

override fun onSingleTapUp(e: MotionEvent): Boolean {
// 用户单击时被调用(支持长按时长按后松手不会调用、双击的第二下时不会被调用)
return false
}

override fun onScroll(downEvent: MotionEvent,currentEvent: MotionEvent, distanceX: Float, distanceY:Float): Boolean {
// 用户滑动时被调用
// 第一个事件是用户按下时的 ACTION_DOWN 事件，第二个事件是当前事件
// 偏移是按下时的位置 - 当前事件的位置
return false
}

override fun onLongPress(e: MotionEvent) {
// 用户长按（按下 500ms 不松⼿）后会被调用
// 这个 500ms 在 GestureDetectorCompat 中变成了 600ms
(？？？ )
}

override fun onFling(downEvent: MotionEvent, currentEvent: MotionEvent, velocityX: Float, velocityY:Float): Boolean {
// 用于滑动时迅速抬起时被调用，用于用户希望控件进行惯性滑动的场景
return false
}

【3】双击监听器： OnDoubleTapListener】
通过GestureDetector.setOnDoubleTapListener(OnDoubleTapListener)
来配置：
gestureDetector.setOnDoubleTapListener(doubleTapListener);

OnDoubleTapListener 的几个回调方法：

override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
// 用户单击时被调⽤
// 和 onSingltTapUp() 的区别在于，用户的一次点击不会立即调用
// 这个方法，而是在一定时间后（300ms），确认用户没有进行双击，这个方法才会被调用
return false
}

override fun onDoubleTap(e: MotionEvent): Boolean {
// 用户双击时被调用
// 注意：第二次触摸到屏幕时就调用，而不是抬起时
return false
}

override fun onDoubleTapEvent(e: MotionEvent): Boolean {
// 用户双击第二次按下时、第二次按下后移动时、第二次按下后抬起时都会被调
// 常用于「双击拖拽」的场景
return false
}


【4】OverScroller】
用于自动计算滑动的偏移。

scroller = OverScroller(context);

常用于 onFling() 方法中，调用 OverScroller.fling() 方法来启动惯性滑动的计算：

override fun onFling(downEvent: MotionEvent,
currentEvent: MotionEvent, velocityX: Float, velocityY:Float) {
// 初始化滑动
scroller.fling(startX, startY, velocityX, velocityY,minX, maxX, minY, maxY)
// 下一帧刷新
ViewCompat.postOnAnimation(this, this)
return false
}

...

override fun run() {
// 计算此时的位置，并且如果滑动已经结束，就停止
if (scroller.computeScrollOffset()) {
// 把此时的位置应⽤于界⾯
offsetX = scroller.currX.toFloat()
offsetY = scroller.currY.toFloat()
invalidate()
// 下一帧刷新
ViewCompat.postOnAnimation(this, this)
}
}

【5】物理模型】

【6】ScaleGestureDetector 和 ScaleGestureListener

private scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

class HenScaleGestureListener : OnScaleGestureListener {

override fun onScaleBegin(detector:ScaleGestureDetector): Boolean {
// 捏撑开始
return true
}

override fun onScaleEnd(detector: ScaleGestureDetector) {
// 捏撑结束
}

override fun onScale(detector: ScaleGestureDetector):Boolean {
// 新的捏撑事件
currentScale *= detector.scaleFactor
// 这个返回值表示「事件是否消耗」，即「这个事件算不算数」
return true
}
}
 **/

//todo 可缩放的图片控件，其实这里用的是一个很小的图片的缩放。
// 如果使用是一个长度大图呢，怎么进行内存优化呢？
class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)

    private var originalOffsetX = 0f //旧的偏移值
    private var originalOffsetY = 0f
    private var offsetX = 0f//新的偏移值
    private var offsetY = 0f

    private var smallScale = 0f//小缩放比
    private var bigScale = 0f//大缩放比

    private val henGestureListener = HenGestureListener()
    private val henScaleGestureListener = HenScaleGestureListener()
    private val henFlingRunner = HenFlingRunner()
    private val gestureDetector = GestureDetectorCompat(context, henGestureListener)//手势监听器
    private val scaleGestureDetector = ScaleGestureDetector(context, henScaleGestureListener)//双指缩放

    private var big = false

    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }

    //缩放动画
    private val scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)
    private val scroller = OverScroller(context)//这个是什么？

    //屏幕尺寸改变时
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {//胖图
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {//瘦图
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
        }

        currentScale = smallScale
        scaleAnimator.setFloatValues(smallScale, bigScale)//更新
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {//如果是在捏撑操作，就不调用
            gestureDetector.onTouchEvent(event)//使用手势监测的事件处理
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    private fun fixOffsets() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    /**
     * public static class SimpleOnGestureListener
     * implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, GestureDetector.OnContextClickListener
     *
     **/
    inner class HenGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true //一般都是返回True，不然就没意义了
        }

        override fun onFling(
            downEvent: MotionEvent?,
            currentEvent: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (big) {
                scroller.fling(
                    offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2).toInt(),
                    ((bitmap.width * bigScale - width) / 2).toInt(),
                    (-(bitmap.height * bigScale - height) / 2).toInt(),
                    ((bitmap.height * bigScale - height) / 2).toInt()
                )
                //...
                ViewCompat.postOnAnimation(this@ScalableImageView, henFlingRunner)
            }
            return false
        }

        override fun onScroll(
            downEvent: MotionEvent?,
            currentEvent: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffsets()//用于限制滑动贴边
                invalidate()
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
                fixOffsets()
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return true
        }
    }

    //这些的难点，其实是理解操作的模型，再进行动态计算
    inner class HenScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {

        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempCurrentScale = currentScale * detector.scaleFactor
            if (tempCurrentScale < smallScale || tempCurrentScale > bigScale) {
                return false
            } else {
                //根据scale系数的大小同步给currentScale，同步绘制缩放后的图片
                currentScale *= detector.scaleFactor // 缩放系数： 1）缩系数：0 1;  2）放系数: 0 无穷
                return true
            }
        }
    }

    inner class HenFlingRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                /**
                 * postOnAnimation()
                 *
                 * <p>Causes the Runnable to execute on the next animation time step.
                 * The runnable will be run on the user interface thread.</p>
                 *
                 * <p>This method can be invoked from outside of the UI thread
                 * only when this View is attached to a window.</p>
                 */
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }
}