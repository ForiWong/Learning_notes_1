package com.wlp.myanjunote.customview.layoutlayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

/**
 * 这个听的很糊涂

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
class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private val childrenBounds = mutableListOf<Rect>()

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var widthUsed = 0//已用的宽度
    var heightUsed = 0//已用的高度
    var lineWidthUsed = 0//当前行已使用的宽
    var lineMaxHeight = 0//当前行的最大的高度值
    val specWidthSize = MeasureSpec.getSize(widthMeasureSpec) //1
    val specWidthMode = MeasureSpec.getMode(widthMeasureSpec) //2

    //先遍历子view
    for ((index, child) in children.withIndex()) {
      //xml布局中指定的大小
//      val layoutParams = child.layoutParams
//      layoutParams.width //3
//      layoutParams.height

      //看看这里的源码，经过这个测量操作，就可以使用measuredWidth了
      measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)

      if (specWidthMode != MeasureSpec.UNSPECIFIED &&
        lineWidthUsed + child.measuredWidth > specWidthSize) {//换行的条件：这一行的已用的宽 + 当前子view的宽 > 父view的宽，就要换行了
        //换行之后，进行重置
        lineWidthUsed = 0
        heightUsed += lineMaxHeight //已用的高 = 旧的已用高 + 上一行最大的高
        lineMaxHeight = 0
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
      }

      if (index >= childrenBounds.size) {//list中加一个元素
        childrenBounds.add(Rect())
      }
      val childBounds = childrenBounds[index]
      //onMeasure 这里测量到尺寸暂存到bounds中，供layout中使用。
      childBounds.set(lineWidthUsed, heightUsed, lineWidthUsed + child.measuredWidth, heightUsed + child.measuredHeight)

      lineWidthUsed += child.measuredWidth
      widthUsed = max(widthUsed, lineWidthUsed)//
      lineMaxHeight = max(lineMaxHeight, child.measuredHeight)//暂存 最大的高
    }

    val selfWidth = widthUsed
    val selfHeight = heightUsed + lineMaxHeight
    //父view保持自己的尺寸
    setMeasuredDimension(selfWidth, selfHeight)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    //小例子
    for(child in children){
      //左右一个子view，铺满这个布局
      //child.layout(0, 0, r - l, b -t)

      //有两个子view，分别是左上和右下
      /*if(children.indexOf(child) == 0){
        child.layout(0, 0, (r - l)/2, (b -t)/2)
      }else{
        child.layout((r - l)/2, (b -t)/2, r - l, b -t)
      }*/
    }

    //这个是真正layout()
    for ((index, child) in children.withIndex()) {
      val childBounds = childrenBounds[index]
      child.layout(childBounds.left, childBounds.top, childBounds.right, childBounds.bottom)
    }
  }

  override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }
}