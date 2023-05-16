package com.wlp.myanjunote.customview.touchmultitouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.wlp.myanjunote.customview.dp

//各自为战型：比如画图程序，多指都可以画图；
class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var paths = SparseArray<Path>()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.dp
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        //onDraw()方法是什么时候会调用呢？如果其中一个path变化，是不是所有的paths都要重新绘制呢
        super.onDraw(canvas)

        for (i in 0 until paths.size()) {
            val path = paths.valueAt(i)
            canvas.drawPath(path, paint)//绘制path
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {//在onTouchEvent中生成path
        when (event.actionMasked) {//actionMasked 支持多点
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                val path = Path()
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))//记录一个点
                paths.append(event.getPointerId(actionIndex), path)
                invalidate()//invalidate() -> onDraw()
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until paths.size()) {
                    val pointerId = event.getPointerId(i)
                    val path = paths.get(pointerId)
                    path.lineTo(event.getX(i), event.getY(i))//划线
                }
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                paths.remove(pointerId)//抬起的时候清空
                invalidate()
            }
        }
        return true
    }
}