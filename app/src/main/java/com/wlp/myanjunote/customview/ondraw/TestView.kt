package com.wlp.myanjunote.customview.ondraw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.customview.dp

/**
11、图形的位置和尺寸测量
hencoder网站的自定义view
（1）绘制的基本要素
onDraw(Canvas) 重写绘制的方法
Canvas 画布，绘制线点、线、圆圈等
Paint 画笔，设置颜色、线条粗细等
坐标系 x、Y坐标轴，四象限的转向，角度的转向

     |
三	 |    四
————————> x
     |
二	 |    一
    ↓
    Y

尺寸单位  是像素，而不是dp. dp转sp

 （2）Path 的方向Path.Direction以及封闭图形的内外判断Path.FillType：
Winding： 如果⽅向相反的穿插次数相等则为内部，不等则为外部。
Even Odd：不考虑⽅向。穿插奇数次则为内部，偶数次则为外部。

 （3）PathMeasure
把 Path 对象填⼊，⽤于对 Path 做针对性的计算（例如图形周⻓）。
 */

val RADIUS = 100f.dp

class TestView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)//anti alias 抗锯齿
    private val path = Path()//路径
    lateinit var pathMeasure: PathMeasure //路径测量

    //在view的大小变化的时候，在layout()中被调用。 view刚加入到视图层级中时，也会被调用
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        //Direction.CCW  .CW   Direction 方向 顺时针 clockwise 逆时针counter-clockwise
        path.addCircle(width / 2f, height / 2f, RADIUS, Path.Direction.CCW)
        path.addRect(
            width / 2f - RADIUS,
            height / 2f,
            width / 2f + RADIUS,
            height / 2f + 2 * RADIUS,
            Path.Direction.CCW
        )
        path.addCircle(width / 2f, height / 2f, RADIUS * 1.5f, Path.Direction.CCW)

        pathMeasure = PathMeasure(path, false) //forceClosed 是否闭合 测量path的长度
        //pathMeasure.length //长度
        //pathMeasure.getPosTan() //某处的切角

        //为啥下面这行代码在这里呢？
        path.fillType = Path.FillType.EVEN_ODD//填充类型，有四种枚举类型，分别代表什么意思呢
        //FillType 与 Direction 之间的关系
        //WINDING, 比较麻烦
        //EVEN_ODD, 镂空最好用这个，规则简单
        //INVERSE_WINDING,
        //INVERSE_EVEN_ODD; 对应EVENT_ODD

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画线
        canvas.drawLine(100f, 100f, 200f, 200f, paint)
        //画圆
        //canvas.drawCircle(width/2f, height/2f, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics), paint)//getResources().getDisplayMetrics()//display显示metrics指标
        //canvas.drawCircle(width/2f, height/2f, Utils.dp2px(100f, context), paint)//getResources().getDisplayMetrics()//display显示metrics指标
        //canvas.drawCircle(width/2f, height/2f, Utils.dp2px(100f), paint)//getResources().getDisplayMetrics()//display显示metrics指标
        //canvas.drawCircle(width/2f, height/2f, RADIUS, paint)//getResources().getDisplayMetrics()//display显示metrics指标

        //绘制路径path
        canvas.drawPath(path, paint)
    }
}