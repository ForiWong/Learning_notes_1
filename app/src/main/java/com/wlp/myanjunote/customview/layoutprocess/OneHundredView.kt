package com.wlp.myanjunote.customview.layoutprocess

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
## 自定义的三个痛点：
测量-布局
绘制
触摸反馈

## 定义布局：布局流程的完全解析
（1）布局过程
确定每个 View 的位置和尺寸
（其实就是：相对自己父布局的位置，和本身的尺寸。）
作用：为绘制和触摸范围做支持
绘制：才知道往哪里绘制
触摸反馈：才知道用户点的是哪里

## 流程
《1》从整体看：
1）测量流程：从根 View 递归调用每一级子 View 的 measure() 方法，对它们进行
测量（子view期望是多大，然后父view再计算这个view位置和实际尺寸）
2）布局流程：从根 View 递归调用每一级子 View 的 layout() 方法，把测量过程得
出的子 View 的位置和尺寸传给子View，子 View 保存

测量和布局是同一次递归的吗？不是同一次，分两次递归。
3）为什么要分两个流程？
因为可能会重复测量，或者需要多次测量。

《2》从个体看，对于每个 View：
1）运行前，开发者在 xml 文件里写入对 View 的布局要求 layout_width 和 layout_height
2）父View 在自己的 onMeasure() 中，根据开发者在 xml 中写的对子View 的要求，和自己的可用空间，
得出对子 View 的具体尺寸要求
3）子View 在自己的 onMeasure() 中，根据父View 的要求和自己的特性算出自己的期望尺寸。
（父View要求优先级高于子view）
 * 如果是 ViewGroup，还会在这里调用每个子 View 的 measure() 进行测量

4）父View在子View 计算出期望尺寸后，得出子 View 的实际尺寸和位置
5）子View 在自己的 layout() 方法中，将父 View 传进来的自己的实际尺寸和位置保存
 *如果是 ViewGroup，还会在 onLayout() 里调用每个子 View 的 layout() 把它们的尺寸位置传给它们

 */
class OneHundredView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(100, 100)
   //设置自己测量的尺寸为100，也就是期望的尺寸，这个值还是有可能被改的
  }

  override fun layout(l: Int, t: Int, r: Int, b: Int) {
    super.layout(l, t, l + 100, t + 100)
    //保持尺寸
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
  }
}