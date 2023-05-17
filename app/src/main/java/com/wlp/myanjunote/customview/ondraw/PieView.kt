package com.wlp.myanjunote.customview.ondraw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 饼图
（1）⽤ drawArc() 绘制扇形
（2）⽤ Canvas.translate() 来移动扇形，并⽤ Canvas.save() 和 Canvas.restore() 来保存和恢复位置
（3）⽤三⻆函数 cos 和 sin 来计算偏移
 **/
private val RADIUS_3 = 150f.dp

//饼的几个角度和颜色
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(
    Color.parseColor("#C2185B"),
    Color.parseColor("#00ACC1"),
    Color.parseColor("#558B2F"),
    Color.parseColor("#5D4037")
)

private val OFFSET_LENGTH = 20f.dp

class PieView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    }

    override fun onDraw(canvas: Canvas) {
        // 画弧 画饼图，其实就是画几个扇形
        var startAngle = 0f
        for ((index, angle) in ANGLES.withIndex()) {
            paint.color = COLORS[index]//每次对paint 设置颜色
            if (index == 2) {
                /**
                 * canvas.save()和canvas.restore()有什么用呢？
                 * canvas.save( )：用来保存Canvas的状态属性
                 * canvas.restore( )：用来恢复Canvas旋转、缩放等之前的状态，
                 * 当和canvas.save( )一起使用时，恢复到canvas.save( )保存时的状态。
                 *
                 * 这里的状态包括矩阵的变换状态，如：平移(Translate), 缩放(Scale), 旋转(Rotate), 倾斜(Skew)，
                 * 以及画布的裁剪区域clip。
                 *
                 * 当我们对画布进行旋转，缩放，平移等操作的时候其实是对特定的元素进行操作，比如图片，一个矩形等。但是
                 * 当你用canvas的方法来进行这些操作的时候，其实是对整个画布进行了操作，那么之后在画布上的元素都会受
                 * 到影响，所以我们在操作之前调用canvas.save()来保存画布当前的状态，当操作之后取出之前保存过的状态
                 * ，这样就不会对其他的元素进行影响
                 **/
                canvas.save()//
                canvas.translate(//对canvas进行平移
                    OFFSET_LENGTH * cos(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat(),
                    OFFSET_LENGTH * sin(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat()
                )
            }
            canvas.drawArc(
                width / 2f - RADIUS_3,
                height / 2f - RADIUS_3,
                width / 2f + RADIUS_3,
                height / 2f + RADIUS_3,
                startAngle,
                angle,
                true,
                paint
            )
            startAngle += angle
            if (index == 2) {
                canvas.restore()//
            }
        }
    }
}
