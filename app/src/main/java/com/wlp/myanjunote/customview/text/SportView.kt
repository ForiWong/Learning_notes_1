package com.wlp.myanjunote.customview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp
import com.wlp.myanjunote.customview.sp

/**
 * Created by wlp on 2023/1/30
 * Description:
 * 文字的居中显示
 * 文字的贴边显示
 */
private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp

class SportView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //text size为什么成为了dp, 不用sp? 因为dp只受像素密度影响，而sp除了受像素密度影响外，还与系统设置的字体大小等级有关
        //dp 与 sp使用区别
        textSize = 100.dp
        typeface = ResourcesCompat.getFont(context, R.font.font)//typeface 字体
        textAlign = Paint.Align.CENTER //字体水平居中
    }
    private val bounds = Rect()//范围
    private val fontMetrics = Paint.FontMetrics()//字体规格

    override fun onDraw(canvas: Canvas) {
        // 绘制环
        paint.style = Paint.Style.STROKE//中空
        paint.color = CIRCLE_COLOR
        paint.strokeWidth = RING_WIDTH
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        // 绘制进度条 圆弧
        paint.color = HIGHLIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND //圆角 startAngle sweepAngle  cap 线的头，帽子
        canvas.drawArc(
            width / 2f - RADIUS,
            height / 2f - RADIUS,
            width / 2f + RADIUS,
            height / 2f + RADIUS,
            -90f,
            225f,
            false,
            paint
        )

        // 绘制文字1，如何让文字居中在圆环
        //paint.textSize = 100.dp //text size为什么成为了dp, 不用sp
        Log.d("...", "dp:" + (100.dp) + " ,sp: " + (100.sp))//实际上这里两个值的大小一般是相等的
        //填充模式.前面将style设置成了stroke中空模式，现在改回为FILL，再绘制
        //如果是stroke模式，你会看到绘制的文字像一团浆糊
        paint.style = Paint.Style.FILL
        //x 起始线； y 基准线 baseline 这个和绘制方框的x y 是不一样的
        //Paint.Align.CENTER 居中。
        //(0)如果只是如下这样绘制文字，文字不会在圆环中居中的，因为这个y值的坐标是基准线的坐标，所以还得减去一个偏移量
        //canvas.drawText("abcp", width / 2f, height / 2f, paint)
        //(1)如果是静态文字，使用getTextBounds()
        //paint.getTextBounds("abcp", 0, "abcp".length, bounds)
        //canvas.drawText("abcp", width/2f, height/2f - (bounds.top + bounds.bottom)/2f, paint)
        //(2)如果是动态文字，用fontMetrics
        paint.getFontMetrics(fontMetrics)
        canvas.drawText(
            "apdc",
            width / 2f,
            height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f,
            paint
        )
        Log.d(
            "绘制文字：",
            "" + width / 2f + " ：" + (height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f)
        )
        //bounds 和 使用 fontMetrics 两种不同的基准
        //静态文字使用bounds的中心，如果是动态文字就使用fontMetrics了
        //fontMetrics几个线: top ascent baseline descent bottom


        //绘制文字2，贴边
        paint.textAlign = Paint.Align.LEFT
        //这样的话，文字都出到顶边上面了，看不见了
        //canvas.drawText("abab", 0f, 0f, paint)
        paint.textSize = 50.dp
        paint.getFontMetrics(fontMetrics)
//        canvas.drawText("abab", 0f, -fontMetrics.top, paint)
        canvas.drawText("abab", 0f, -fontMetrics.ascent, paint)//这样就是贴边 顶、左,顶部稍微有缝隙
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        canvas.drawText("abab", 0f, -bounds.top.toFloat(), paint)//这个用bounds贴边，就很靠近顶部了,但是这个适合用于静态文字，显示美观


        // 绘制文字3
        paint.textSize = 50.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getFontMetrics(fontMetrics)
        paint.getTextBounds("ab-ab", 0, "abab".length, bounds)
        //canvas.drawText("abab",0f, - bounds.top.toFloat(), paint)
        canvas.drawText("abab", - bounds.left.toFloat(), - bounds.top.toFloat(), paint)

        // 绘制文字4
        paint.textSize = 15.dp
        //todo 这里的字体大小是15和上面文字字体大小50差很大时，UI下显示发现左边的空隙就不一样了，是因为left的区别
        //todo 使用 - bounds.left.toFloat() 就可以进行消除了
        paint.getTextBounds("aba-b", 0, "aba-b".length, bounds)
        //canvas.drawText("aba-b", 0f, - bounds.top.toFloat(), paint)
        canvas.drawText("aba-b", - bounds.left.toFloat(), - bounds.top.toFloat(), paint)

    }
}
