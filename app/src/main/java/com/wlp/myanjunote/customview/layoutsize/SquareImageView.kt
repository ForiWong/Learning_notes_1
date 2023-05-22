package com.wlp.myanjunote.customview.layoutsize

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

/**
自定义布局：尺寸的自定义
（1）简单改写继承已有的View 的尺寸
（是继承已有view，它已经测量好了，但是不符合你的需求，你要进行修改它的尺寸）
重写 onMeasure()
用getMeasuredWidth() 和 getMeasuredHeight() 获取到测量出的尺寸
计算出最终要的尺寸
用setMeasuredDimension(width, height) 把结果保存
例子：SquareImageView

## 为什么不重写 layout()？
因为重新 layout() 会导致「不听话」
详细讲，就是通过layout也可以得到相同的结果，但是layout这里改容易出问题，父view是不知道你改了的。
容易乱。
但是，如果在onMeasure中改的话，父布局也是知道的。

（2）完全自定义 View 的尺寸
（测量过程不是在父类的基础上，完全是自己来）
重写 onMeasure()
计算出自己的尺寸
用resolveSize() 或者 resolveSizeAndState() 修正结果
使用setMeasuredDimension(width, height) 保存结果
例子：CircleView

（3）自定义 Layout
1）重写 onMeasure()
# 遍历每个子 View，测量子View
# 测量完成后，得出子View 的实际位置和尺寸，并暂时保存
# 有些子View 可能需要重新测量
# 测量出所有子 View 的位置和尺寸后，计算出自己的尺寸，并用
setMeasuredDimension(width, height) 保存
2）重写 onLayout()
遍历每个子View，调用它们的 layout() 方法来将位置和尺寸传给它们

例子：TagLayout
 **/
class SquareImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    //用getMeasuredWidth() 和 getMeasuredHeight() 获取到测量出的尺寸
    val size = min(measuredWidth, measuredHeight)

    //这个四个尺寸的区别
    getMeasuredWidth()//你自己测量的期望尺寸
    getMeasuredHeight()
    getWidth()//实际的尺寸，你的父view告诉你的实际尺寸
    getHeight()

    setMeasuredDimension(size, size) //保存尺寸
  }

  //用 layout() 还是 onMeasure()
  override fun layout(l: Int, t: Int, r: Int, b: Int) {
    //super.layout(l, t, r, b)
    val width = r - l
    val height = b - t
    val size = min(width, height)
    super.layout(l, t, l + size , t + size)
  }
}