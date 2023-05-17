package com.wlp.myanjunote.customview.ondraw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wlp.myanjunote.customview.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by wlp on 2023/1/30
 * Description:
仪表盘
（1）⽤ drawArc() 绘制弧形
（2）三⻆函数的计算 横向的位移是 cos，纵向的位移是 sin
（3）PathDashPathEffect
    加上 PathEffect 之后，就只绘制 effect，⽽不绘制原图形。所以需要弧线和刻度分别绘制，⼀共两次。
    dash 的⽅向
 */
private const val OPEN_ANGLE = 120f//开口角度
private const val MARK = 10 //刻度值
private val DASH_WIDTH = 2f.dp
private val DASH_LENGTH = 10f.dp

class DashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dash = Path()
    private var RADIUS_2 = 150f.dp//半径
    private var LENGTH = 120f.dp//指针的程度
    private lateinit var pathEffect: PathEffect

    init {
        paint.strokeWidth = 3f.dp //设置线条的宽度
        paint.style = Paint.Style.STROKE //设置为空心
        Log.d("view的宽与高", " = $width : $height") //view的宽与高:  = 0 : 0 //在这里不能取尺寸

        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)//这个刻度其实就是个矩形
        //paint.pathEffect = PathDashPathEffect(dash, 50f, 0f, PathDashPathEffect.Style.ROTATE)
        //这个是给paint的Path效果
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        RADIUS_2 = w / 2f - 5f.dp
        LENGTH = RADIUS_2 * 0.8f

        path.reset()
        //startAngle起始角度、sweepAngle扫描的角度  设置弧度的path
        path.addArc(//把圆弧放到path里面去
            width / 2f - RADIUS_2, height / 2f - RADIUS_2,
            width / 2f + RADIUS_2, height / 2f + RADIUS_2,
            90 + OPEN_ANGLE / 2f, 360 - OPEN_ANGLE
        )

        val pathMeasure = PathMeasure(path, false)
        //PathEffect就是指,用各种笔触效果来绘制一个路径。 dash就是虚线
        pathEffect = PathDashPathEffect(
            dash,
            (pathMeasure.length - DASH_WIDTH) / 20f,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("2_view的宽与高", " = $width : $height")//2_view的宽与高:  = 750 : 750

        //画圆弧
//        canvas.drawArc(
//            width / 2f - RADIUS_2, height / 2f - RADIUS_2,
//            width / 2f + RADIUS_2, height / 2f + RADIUS_2,
//            //圆弧的其实角度， 圆弧的扫过角度
//            90 + OPEN_ANGLE / 2f, 360 - OPEN_ANGLE, false, paint
//        )
        /**
         * 为啥这个画弧，在view不同的宽高得到的是相同位置，他的基准点在哪？
         * 居中，再通过radius设置，应该吧radius改为可变的，就可以了,再给radius减去一个边界值
         *
         */
        canvas.drawPath(path, paint)

        //画刻度,  其实是在有Path效果的时候，再画一遍弧
//        paint.pathEffect = PathDashPathEffect(dash, 50f, 0f, PathDashPathEffect.Style.ROTATE)
        paint.pathEffect = pathEffect
//        canvas.drawArc(
//            width / 2f - RADIUS_2, height / 2f - RADIUS_2,
//            width / 2f + RADIUS_2, height / 2f + RADIUS_2,
//            //猿辅导其实角度， 圆弧的扫过角度
//            90 + OPEN_ANGLE / 2f, 360 - OPEN_ANGLE, false, paint
//        )
        canvas.drawPath(path, paint)
        paint.pathEffect = null //画好了刻度之后，再把效果去掉
        //画到这里，还得将弧的刻度固定一个数量，比如是20个刻度

        // 画指针：三角函数很重要，正弦/余弦，这个划线难点是计算线条的终点坐标
        canvas.drawLine(
            width / 2f, height / 2f,
            width / 2f + LENGTH * cos(markToRadians(MARK)).toFloat(),
            height / 2f + LENGTH * sin(markToRadians(MARK)).toFloat(), paint
        )
    }

    //角度 --> 弧度
    private fun markToRadians(mark: Int) =
        Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())
}







