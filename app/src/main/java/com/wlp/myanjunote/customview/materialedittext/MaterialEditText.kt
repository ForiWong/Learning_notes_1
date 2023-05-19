package com.wlp.myanjunote.customview.materialedittext

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp

private val TEXT_SIZE = 12.dp //label的文字大小
private val TEXT_MARGIN = 8.dp //间隔
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

//在自定义控件内，使用动画
class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  private var floatingLabelShown = false

  var floatingLabelFraction = 0f//这个为什么用public 不用private呢？是为了给下面的ObjectAnimator获取到
    set(value) {
      field = value
      invalidate()
    }

  private val animator by lazy {//对自己做动画,可以重复使用的
    ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f)
  }

  var useFloatingLabel = false//是否使用标签
    set(value) {
      if (field != value) {
        field = value
        if (field) {
          // + TEXT_SIZE + TEXT_MARGIN 相当于预留出label的位置及空间 相当于父类的padding
          setPadding(paddingLeft, (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
        } else {
          setPadding(paddingLeft, (paddingTop - TEXT_SIZE - TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
        }
      }
    }

  init {
    paint.textSize = TEXT_SIZE

    //attrs属性集合，这个集合包括在xml中给控件设置的那些属性，没有设置的不包含在内。id 也在内的
    for (index in 0 until attrs.attributeCount) {
      println("Attrs: key: ${attrs.getAttributeName(index)}, value: ${attrs.getAttributeValue(index)}")
    }

    //AttributeSet 属性集合，拿到xml给的设置值
    //R.styleable.MaterialEditText 实际上是一个数组
    //val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
    val typedArray = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.useFloatingLabel))
    useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true)
    //useFloatingLabel = typedArray.getBoolean(0, true)  //数组内只有一个，写0 也可以
    typedArray.recycle()//回收
  }

  //文字内容变化回调， 写自定义view要逻辑很清楚
  override fun onTextChanged(text2: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
    if (useFloatingLabel && text.isNullOrEmpty() && floatingLabelShown) {
      floatingLabelShown = false
      floatingLabelFraction = 1f
      animator.reverse()//反向播放，这个好
    } else if (useFloatingLabel && !text.isNullOrEmpty() && !floatingLabelShown) {
      floatingLabelShown = true
      floatingLabelFraction = 0f
      animator.start()
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //这里根据floatingLabelFraction动画完成度，改变透明度、文字大小
    paint.alpha = (floatingLabelFraction * 0xff).toInt()
    paint.textSize = TEXT_SIZE*(2-floatingLabelFraction)
    val currentVertivalValue = VERTICAL_OFFSET + EXTRA_VERTICAL_OFFSET * (1 - floatingLabelFraction)
    canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, currentVertivalValue, paint)//这个是绘制hint的
  }
}