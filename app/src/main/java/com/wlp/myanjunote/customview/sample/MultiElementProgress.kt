package com.wlp.myanjunote.customview.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import com.wlp.myanjunote.customview.Utils

import kotlin.jvm.JvmOverloads;
import kotlin.math.min

/**
 * Kotlin中@JvmOverloads 注解
 * 在Kotlin中@JvmOverloads注解的作用就是：在有默认参数值的方法中使用@JvmOverloads注解，则Kotlin就会暴露多个重载方法。
 * 注解说明：指示Kotlin编译器为这个函数生成替代默认参数值的重载。
 * 使用说明：该注解可以用在方法前，可以用在构造函数前。
 **/
class MultiElementProgress @JvmOverloads constructor
    (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var strokeWidth = 0f //画笔宽度
    private var startAngle = 90f //开始角度

    //apply内置函数
    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
            strokeWidth = this@MultiElementProgress.strokeWidth
        }
    }

    private val rectf by lazy {
        when {
            mWidth > mHeight -> RectF(
                mWidth / 2 - mHeight / 2 + strokeWidth / 2,
                0f + strokeWidth / 2,
                mWidth / 2 - mHeight / 2 + mHeight - strokeWidth / 2,
                mHeight - strokeWidth / 2
            )
            mWidth == mHeight -> RectF(
                0f + strokeWidth / 2,
                0f + strokeWidth / 2,
                min(mWidth, mHeight) - strokeWidth / 2,
                min(mWidth, mHeight) - strokeWidth / 2
            )
            else -> RectF(
                0f + strokeWidth / 2,
                mHeight / 2 - mWidth / 2 + strokeWidth / 2,
                mWidth - strokeWidth / 2,
                mHeight / 2 - mWidth / 2 + mWidth - strokeWidth / 2
            )
        }
    }

    private val multiElement by lazy { ArrayMap<Int, Float>() }
    private var mHeight: Float = 0f
    private var mWidth: Float = 0f

    init {
        multiElement.put(Color.parseColor("#eadc4b"), 30f)
        multiElement.put(Color.parseColor("#f9a844"), 60f)
        multiElement.put(Color.parseColor("#b5db39"), 90f)
        strokeWidth = Utils.dp2px(32f)
    }

    //测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec).toFloat()
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("控件的高宽1：", "$mHeight*$mWidth")//660.0*750.0
        Log.d("控件的高宽2：", "$height*$width")//0*0
        Log.d("控件的高宽3：", "$measuredHeight*$measuredWidth")//660*750
    }
    //TODO 什么时候获取控件的坐标、width\height 才是有效的？？

    override fun onDraw(canvas: Canvas?) {
        var angle = startAngle
        paint.strokeWidth = strokeWidth;
        var allValue = 0f
        multiElement.all { entry ->
            allValue += entry.value
            true
        }
        for (entry in multiElement) {
            //设置圆弧顺时针扫过的角度
            val sweepAngle = (entry.value.toFloat() / allValue) * 360
            paint.color = entry.key
            //useCenter如果设置为true, 并且当前画笔的描边属性设置为Paint.Style.FILL的时候，画出的就是扇形。
            //Paint.Style.FILL + true --> 扇形
            //Paint.Style.FILL + false --> 填满的弧形
            //Paint.Style.STROKE + false --> 环形
            canvas?.drawArc(rectf, angle, sweepAngle, true, paint)
            angle += sweepAngle
        }
        super.onDraw(canvas)
    }

    /**
     * 设置每一个选项的 Item
     */
    public fun setProgress(elements: List<Element>) {
        multiElement.clear()
        for (element in elements) {
            multiElement.put(element.color, element.progress)
        }
        postInvalidate()//刷新UI
    }

    /**
     * 设置画笔宽度
     */
    public fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        postInvalidate()
    }

    /**
     * 设置开始画的角度
     */
    public fun setStartAngle(startAngle: Float) {
        this.startAngle = startAngle
        postInvalidate()
    }

    public class Element(@ColorInt val color: Int, val progress: Float)
}