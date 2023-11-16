package com.wlp.myanjunote.customview.sample

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt


class LineChart : View {

    private var options = ChartOptions()

    /**
     * X轴相关
     */
    private val xAxisTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val xAxisLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xAxisTexts = mutableListOf<String>()
    private var xAxisHeight = 0f


    /**
     * Y轴相关
     */
    private val yAxisTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val yAxisLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val yAxisTexts = mutableListOf<String>()
    private var yAxisWidth = 0f
    private val yAxisCount = 5
    private var yAxisMaxValue: Int = 0

    /**
     * 原点
     */
    private var originX = 0f
    private var originY = 0f

    /**
     * 间隔
     */
    private var xGap = 0f
    private var yGap = 0f

    /**
     * 数据相关
     */
    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.STROKE
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.parseColor("#79EBCF")
        it.style = Paint.Style.FILL
    }

    private val points = mutableListOf<ChartBean>()

    //文字绘制区域
    private val bounds = Rect()

    //折线的路径
    private val path = Path()

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isEmpty()) return

        val xAxisOptions = options.xAxisOptions
        val yAxisOptions = options.yAxisOptions
        val dataOptions = options.dataOptions

        //1、设置原点
        //OriginX：Y轴宽度
        //OriginY：View高度 - X轴高度
        originX = yAxisWidth
        originY = height - xAxisHeight

        //2、设置X轴Y轴间隔：计算X轴的刻度间隔
        xGap = (width - originX) / points.size
        //Y轴默认顶部会留出一半空间
        yGap = originY / (yAxisCount - 1 + 0.5f)

        //3、绘制X轴
        //轴线 --> 绘制轴线比较简单，沿原点向控件右侧画一条直线即可
        if (xAxisOptions.isEnableLine) {
            xAxisLinePaint.strokeWidth = xAxisOptions.lineWidth
            xAxisLinePaint.color = xAxisOptions.lineColor
            xAxisLinePaint.pathEffect = xAxisOptions.linePathEffect
            canvas.drawLine(originX, originY, width.toFloat(), originY, xAxisLinePaint)
        }

        //3-1、网格线：只需要根据X轴的刻度，沿Y轴方向依次向控件顶部，画直线即可
        //文本：文本需要通过画笔，提前测量出待绘制文本的区域，然后计算出居中位置绘制即可
        xAxisTexts.forEachIndexed { index, text ->
            val pointX = originX + index * xGap
            //刻度线
            if (xAxisOptions.isEnableRuler) {
                xAxisLinePaint.strokeWidth = xAxisOptions.rulerWidth
                xAxisLinePaint.color = xAxisOptions.rulerColor
                canvas.drawLine(
                    pointX, originY,
                    pointX, originY - xAxisOptions.rulerHeight,
                    xAxisLinePaint
                )
            }
            //网格线
            if (xAxisOptions.isEnableGrid) {
                xAxisLinePaint.strokeWidth = xAxisOptions.gridWidth
                xAxisLinePaint.color = xAxisOptions.gridColor
                xAxisLinePaint.pathEffect = xAxisOptions.gridPathEffect
                canvas.drawLine(pointX, originY, pointX, 0f, xAxisLinePaint)
            }
            //文本
            bounds.setEmpty()
            xAxisTextPaint.textSize = xAxisOptions.textSize
            xAxisTextPaint.color = xAxisOptions.textColor
            xAxisTextPaint.getTextBounds(text, 0, text.length, bounds)
            val fm = xAxisTextPaint.fontMetrics
            val fontHeight = fm.bottom - fm.top
            val fontX = originX + index * xGap + (xGap - bounds.width()) / 2f
            val fontBaseline = originY + (xAxisHeight - fontHeight) / 2f - fm.top
            canvas.drawText(text, fontX, fontBaseline, xAxisTextPaint)
        }

        //4、绘制Y轴 --> Y轴的轴线、网格线、文本剩下的内容与X轴的处理方式几乎一致
        //轴线
        if (yAxisOptions.isEnableLine) {
            yAxisLinePaint.strokeWidth = yAxisOptions.lineWidth
            yAxisLinePaint.color = yAxisOptions.lineColor
            yAxisLinePaint.pathEffect = yAxisOptions.linePathEffect
            canvas.drawLine(originX, 0f, originX, originY, yAxisLinePaint)
        }

        yAxisTexts.forEachIndexed { index, text ->
            //刻度线
            val pointY = originY - index * yGap
            if (yAxisOptions.isEnableRuler) {
                yAxisLinePaint.strokeWidth = yAxisOptions.rulerWidth
                yAxisLinePaint.color = yAxisOptions.rulerColor
                canvas.drawLine(
                    originX,
                    pointY,
                    originX + yAxisOptions.rulerHeight,
                    pointY,
                    yAxisLinePaint
                )
            }
            //网格线
            if (yAxisOptions.isEnableGrid) {
                yAxisLinePaint.strokeWidth = yAxisOptions.gridWidth
                yAxisLinePaint.color = yAxisOptions.gridColor
                yAxisLinePaint.pathEffect = yAxisOptions.gridPathEffect
                canvas.drawLine(originX, pointY, width.toFloat(), pointY, yAxisLinePaint)
            }
            //文本
            bounds.setEmpty()
            yAxisTextPaint.textSize = yAxisOptions.textSize
            yAxisTextPaint.color = yAxisOptions.textColor
            yAxisTextPaint.getTextBounds(text, 0, text.length, bounds)
            val fm = yAxisTextPaint.fontMetrics
            val x = (yAxisWidth - bounds.width()) / 2f
            val fontHeight = fm.bottom - fm.top
            val y = originY - index * yGap - fontHeight / 2f - fm.top
            canvas.drawText(text, x, y, yAxisTextPaint)//
        }

        //绘制数据
        // --> 折线的连接，这里使用的是Path，将一个一个坐标点连接，最后将Path绘制，就形成了图中的折线图
        path.reset()
        points.forEachIndexed { index, point ->
            val x = originX + index * xGap + xGap / 2f
            val y = originY - (point.yAxis.toFloat() / yAxisMaxValue) * (yGap * (yAxisCount - 1))
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            //圆点
            circlePaint.color = dataOptions.circleColor
            canvas.drawCircle(x, y, dataOptions.circleRadius, circlePaint)//
        }
        pathPaint.strokeWidth = dataOptions.pathWidth
        pathPaint.color = dataOptions.pathColor
        canvas.drawPath(path, pathPaint)//
    }
    //todo api: drawLine    drawText:FontMetrics    drawCircle      drawPath:Path

    /**
     * 设置数据
     */
    fun setData(list: List<ChartBean>) {
        points.clear()
        points.addAll(list)
        //设置X轴、Y轴数据
        setXAxisData(list)
        setYAxisData(list)
        invalidate()
    }

    /**
     * 设置X轴数据
     */
    private fun setXAxisData(list: List<ChartBean>) {
        val xAxisOptions = options.xAxisOptions
        val values = list.map { it.xAxis }
        //X轴文本
        xAxisTexts.clear()
        xAxisTexts.addAll(values)
        //0-1、X轴高度
        //计算X轴高度
        //--> 思路：获取X轴画笔FontMetrics，根据其top、bottom计算出X轴文字高度，在加上其上下Margin间距即X轴高度
        val fontMetrics = xAxisTextPaint.fontMetrics
        val lineHeight = fontMetrics.bottom - fontMetrics.top
        xAxisHeight = lineHeight + xAxisOptions.textMarginTop + xAxisOptions.textMarginBottom
    }

    /**
     * 设置Y轴数据
     */
    private fun setYAxisData(list: List<ChartBean>) {
        val yAxisOptions = options.yAxisOptions
        yAxisTextPaint.textSize = yAxisOptions.textSize
        yAxisTextPaint.color = yAxisOptions.textColor

        val texts = list.map { it.yAxis.toString() }
        yAxisTexts.clear()
        yAxisTexts.addAll(texts)
        //0-2、计算Y轴宽度
        //--> 思路：遍历Y轴的绘制文字，用画笔测量其最大宽度，在加上其左右Margin间距即Y轴宽度
        //todo 注意这个方法：paint.measureText()
        val maxTextWidth = yAxisTexts.maxOf { yAxisTextPaint.measureText(it) }
        yAxisWidth = maxTextWidth + yAxisOptions.textMarginLeft + yAxisOptions.textMarginRight

        //Y轴间隔
        val maxY = list.maxOf { it.yAxis }
        val interval = when {
            maxY <= 10 -> getYInterval(10)
            else -> getYInterval(maxY)
        }

        //Y轴文字
        yAxisTexts.clear()
        for (index in 0..yAxisCount) {
            val value = index * interval
            yAxisTexts.add(formatNum(value))
        }
        yAxisMaxValue = (yAxisCount - 1) * interval
    }

    /**
     * 格式化数值
     */
    private fun formatNum(num: Int): String {
        val absNum = Math.abs(num)
        return if (absNum >= 0 && absNum < 1000) {
            return num.toString()
        } else {
            val format = DecimalFormat("0.0")
            val value = num / 1000f
            "${format.format(value)}k"
        }
    }

    /**
     * 根据Y轴最大值、数量获取Y轴的标准间隔
     */
    private fun getYInterval(maxY: Int): Int {
        val yIntervalCount = yAxisCount - 1
        val rawInterval = maxY / yIntervalCount.toFloat()
        val magicPower = floor(log10(rawInterval.toDouble()))
        var magic = 10.0.pow(magicPower).toFloat()
        if (magic == rawInterval) {
            magic = rawInterval
        } else {
            magic *= 10
        }
        val rawStandardInterval = rawInterval / magic
        val standardInterval = getStandardInterval(rawStandardInterval) * magic
        return standardInterval.roundToInt()
    }

    /**
     * 根据初始的归一化后的间隔,转化为目标的间隔
     */
    private fun getStandardInterval(x: Float): Float {
        return when {
            x <= 0.1f -> 0.1f
            x <= 0.2f -> 0.2f
            x <= 0.25f -> 0.25f
            x <= 0.5f -> 0.5f
            x <= 1f -> 1f
            else -> getStandardInterval(x / 10) * 10
        }
    }

    /**
     * 重置参数
     */
    fun setOptions(newOptions: ChartOptions) {
        this.options = newOptions
        setData(points)
    }

    fun getOptions(): ChartOptions {
        return options
    }

    data class ChartBean(val xAxis: String, val yAxis: Int)
}


class ChartOptions {

    //X轴配置
    var xAxisOptions = AxisOptions()

    //Y轴配置
    var yAxisOptions = AxisOptions()

    //数据配置
    var dataOptions = DataOptions()

}


/**
 * 轴线配置参数
 */
class AxisOptions {

    companion object {

        private const val DEFAULT_TEXT_SIZE = 20f

        private const val DEFAULT_TEXT_COLOR = Color.BLACK

        private const val DEFAULT_TEXT_MARGIN = 20

        private const val DEFAULT_LINE_WIDTH = 2f

        private const val DEFAULT_RULER_WIDTH = 10f

    }

    /**
     * 文字大小
     */
    @FloatRange(from = 1.0)
    var textSize: Float = DEFAULT_TEXT_SIZE

    @ColorInt
    var textColor: Int = DEFAULT_TEXT_COLOR

    /**
     * X轴文字内容上下两侧margin
     */
    var textMarginTop: Int = DEFAULT_TEXT_MARGIN

    var textMarginBottom: Int = DEFAULT_TEXT_MARGIN

    /**
     * Y轴文字内容左右两侧margin
     */
    var textMarginLeft: Int = DEFAULT_TEXT_MARGIN

    var textMarginRight: Int = DEFAULT_TEXT_MARGIN

    /**
     * 轴线
     */
    var lineWidth: Float = DEFAULT_LINE_WIDTH

    @ColorInt
    var lineColor: Int = DEFAULT_TEXT_COLOR

    var isEnableLine = true

    var linePathEffect: PathEffect? = null

    /**
     * 刻度
     */
    var rulerWidth = DEFAULT_LINE_WIDTH

    var rulerHeight = DEFAULT_RULER_WIDTH

    @ColorInt
    var rulerColor = DEFAULT_TEXT_COLOR

    var isEnableRuler = true

    /**
     * 网格
     */
    var gridWidth: Float = DEFAULT_LINE_WIDTH

    @ColorInt
    var gridColor: Int = DEFAULT_TEXT_COLOR

    var gridPathEffect: PathEffect? = null

    var isEnableGrid = true

}


/**
 * 数据配置参数
 */
class DataOptions {

    companion object {

        private const val DEFAULT_PATH_WIDTH = 2f

        private const val DEFAULT_PATH_COLOR = Color.BLACK

        private const val DEFAULT_CIRCLE_RADIUS = 10f

        private const val DEFAULT_CIRCLE_COLOR = Color.BLACK
    }

    var pathWidth = DEFAULT_PATH_WIDTH

    var pathColor = DEFAULT_PATH_COLOR

    var circleRadius = DEFAULT_CIRCLE_RADIUS

    var circleColor = DEFAULT_CIRCLE_COLOR
}

