package com.wlp.myanjunote.customview.animation

import android.animation.*
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.animation.view.ProvinceEvaluator
import com.wlp.myanjunote.customview.dp

/**
 * 属性动画 和 硬件加速
 * ViewPropertyAnimator 属性动画
 * ObjectAnimator 自定义属性动画
 * AnimatorSet 动画集合
 * PropertyValuesHolder 更加详细的动画：多个属性值作用于同一个view。 属性值持有者
 * Keyframe 做的就更细了，关键帧。配合使⽤ Keyframe ，对⼀个属性分多个段
 * Interpolator 插值器，⽤于设置时间完成度到动画完成度的计算公式，直⽩地说即设置动画的速度曲线
 * TypeEvaluator 自定义估值器⽤于设置动画完成度到属性具体值的计算公式。
 *
 **/
class AnimationActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_animation)

    /**
    属性动画
    （1）ViewPropertyAnimator
    使⽤ View.animate() 创建对象，以及使⽤
    ViewPropertyAnimator.translationX() 等⽅法来设置动画；
    可以连续调⽤来设置多个动画；
    可以⽤ setDuration() 来设置持续时间；
    可以⽤ setStartDelay() 来设置开始延时；
    以及其他⼀些便捷⽅法。
    ObjectAnimator
     **/
    /*view.animate() // radius --> ViewPropertyAnimator 视图的属性动画。 缺点：就是可以改变的属性就那么几个。 --> 如果是自定义的属性，然后就有了ObjectAnimator
      .translationX(200.dp) //相当于 setTranslationX(10) setTranslationX(20) setTranslationX(40)
      .translationY(100.dp)
      .alpha(0.5f)
      .scaleX(2f)
      .scaleY(2f)
      .rotation(90f)
      //可以⽤ setDuration() 来设置持续时间；
      //可以⽤ setStartDelay() 来设置开始延时；
      .setStartDelay(1000)
      .setDuration(1000)*/

    /**
    ObjectAnimator 自定义属性动画
    使⽤ ObjectAnimator.ofXxx() 来创建对象，以及使⽤
    ObjectAnimator.start() 来主动启动动画。它的优势在于，可以为⾃定义属性设置动画。
    另外，⾃定义属性需要设置 getter 和 setter ⽅法，并且 setter ⽅法⾥需要调⽤invalidate() 来触发重绘。
    自定义属性要是可以被获取的，not private ,而是public

    可以使⽤ setDuration() 来设置持续时间；
    可以⽤ setStartDelay() 来设置开始延时；
    以及其他⼀些便捷⽅法。
     **/
    //自定义属性动画的使用： 注意控件的CircleView的属性radius, set(value) 方法 ObjectAnimator 自定义属性动画
    /*val animator = ObjectAnimator.ofFloat(view, "radius", 150.dp)
    animator.startDelay = 1000
    animator.start()*/

    /**
    AnimatorSet()动画集合
    AnimatorSet: 将多个属性动画 Animator 合并在⼀起使⽤，先后顺序或并列顺序都可以：
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animator1, animator2);
    animatorSet.start();
     **/
    /*val bottomFlipAnimator = ObjectAnimator.ofFloat(view, "bottomFlip", 60f)
    bottomFlipAnimator.startDelay = 1000
    bottomFlipAnimator.duration = 1000

    val flipRotationAnimator = ObjectAnimator.ofFloat(view, "flipRotation", 270f)
    flipRotationAnimator.startDelay = 200
    flipRotationAnimator.duration = 1000

    val topFlipAnimator = ObjectAnimator.ofFloat(view, "topFlip", - 60f)
    topFlipAnimator.startDelay = 200
    topFlipAnimator.duration = 1000
//    topFlipAnimator.start()

    val animatorSet = AnimatorSet()//动画集合，对多个动画的操作
//    animatorSet.play()
    animatorSet.playSequentially(bottomFlipAnimator, flipRotationAnimator, topFlipAnimator)//执行的顺序
    animatorSet.start()*/

    /**
     * PropertyValuesHolder 更加详细的动画：多个属性值作用于同一个view。 属性值持有者
     * 同一个动画，操作多个属性
     **/
    /*val bottomFlipHolder = PropertyValuesHolder.ofFloat("bottomFlip", 60f)
    val flipRotationHolder = PropertyValuesHolder.ofFloat("flipRotation", 270f)
    val topFlipHolder = PropertyValuesHolder.ofFloat("topFlip", - 60f)
    val holderAnimator = ObjectAnimator.ofPropertyValuesHolder(view, bottomFlipHolder,  flipRotationHolder,  topFlipHolder)
    holderAnimator.startDelay = 1000
    holderAnimator.duration = 2000
    holderAnimator.start()*/

    /**
     * Keyframe 做的就更细了 关键帧
     * 配合使⽤ Keyframe ，对⼀个属性分多个段：
     **/
    //fraction: The time, expressed as a value between 0 and 1, representing the fraction
    //          of time elapsed of the overall animation duration. 代表过的时间
    //value: fraction对应的值是多少
    /*val length = 200.dp
    val keyframe1 = Keyframe.ofFloat(0f, 0f)
    val keyframe2 = Keyframe.ofFloat(0.2f, 1.5f * length)
    val keyframe3 = Keyframe.ofFloat(0.8f, 0.6f * length)
    val keyframe4 = Keyframe.ofFloat(1f, 1f * length)
    val keyframeHolder = PropertyValuesHolder.ofKeyframe("translationX", keyframe1, keyframe2, keyframe3, keyframe4)
    val animator = ObjectAnimator.ofPropertyValuesHolder(view, keyframeHolder)
    animator.startDelay = 1000
    animator.duration = 2000
    animator.start()*/

    /**
    Interpolator
    插值器，⽤于设置时间完成度到动画完成度的计算公式，直⽩地说即设置动画的速
    度曲线，通过 setInterpolator(Interpolator) ⽅法来设置。
    常⽤的有 AccelerateDecelerateInterpolator
    AccelerateInterpolator
    DecelerateInterpolator
    LinearInterpolator
     */
    //Interpolator 插值器（时间完成度-->动画完成度）；⽤于设置时间完成度到动画完成度的计算公式，直⽩地说即设置动画的速
    //度曲线，通过 setInterpolator(Interpolator) ⽅法来设置。
    /*val animator2 = ObjectAnimator.ofFloat();
    animator2.interpolator = AccelerateDecelerateInterpolator()//插值器*/

    /**
    TypeEvaluator 自定义估值器
    ⽤于设置动画完成度到属性具体值的计算公式。默认的 ofInt() ofFloat() 已
    经有了⾃带的 IntEvaluator FloatEvaluator ，但有的时候需要⾃⼰设置
    Evaluator。例如，对于颜⾊，需要为 int 类型的颜⾊设置 ArgbEvaluator，⽽不是
    让它们使⽤ IntEvaluator：
        animator.setEvaluator(new ArgbEvaluator());
    如果你对 ArgbEvaluator 的效果不满意，也可以⾃⼰写⼀个 HsvEvaluator。
     **/
    //TypeEvaluator 估值器（完成度-->属性值）； ⽤于设置动画完成度到属性具体值的计算公式。
    //效果：会动的一个黑点
    /*val animator = ObjectAnimator.ofObject(view, "point", PointFEvaluator(), PointF(100.dp, 200.dp))
    animator.startDelay = 1000
    animator.duration = 2000
    animator.start()*/

    //效果：会变化的字符串动画
//    val animator = ObjectAnimator.ofObject(findViewById<View>(R.id.view), "province", ProvinceEvaluator(), "澳门特别行政区")
//    animator.startDelay = 1000
//    animator.duration = 10000
//    animator.start()
//
//    findViewById<View>(R.id.view).animate()
//      .translationY(200.dp)
//      .withLayer()//withLayer 在属性动画过程中开启硬件加速，临时的。 这个只有对自带的属性才有用

    /**
    Listeners 和 View 的点击、⻓按监听器⼀样，Animator 也可以使⽤ setXxxListener()
    addXxxListener() 来设置监听器。

    ValueAnimator
    这是最基本的 Animator，它不和具体的某个对象联动，⽽是直接对两个数值进⾏渐
    变计算。使⽤很少。
     */

    /**
    硬件加速 硬件绘制 软件绘制

    硬件加速
    （1）硬件加速是什么
    使⽤ CPU 绘制到 Bitmap，然后把 Bitmap 贴到屏幕，就是软件绘制；
    使⽤ CPU 把绘制内容转换成 GPU 操作，交给 GPU，由 GPU 负责真正的绘制，就叫硬件绘制；
    使⽤ GPU 绘制就叫做硬件加速
    （2）怎么就加速了？
    GPU 分摊了⼯作
    GPU 绘制简单图形（例如⽅形、圆形、直线）在硬件设计上具有先天优势，会更快
    流程得到优化（重绘流程涉及的内容更少）
    （3）硬件加速的缺陷：
    兼容性。由于使⽤ GPU 的绘制（暂时）⽆法完成某些绘制，因此对于⼀些特定的
    API，需要关闭硬件加速来转回到使⽤ CPU 进⾏绘制。
    官网给出的：Unsupported drawing operations 硬件加速不支持的操作
     */

    /**
    离屏缓冲：
    离屏缓冲是什么：单独的⼀个绘制 View（或 View 的⼀部分）的区域
    setLayerType() 和 saveLayer()
    （1）setLayerType() 是对整个 View，不能针对 onDraw() ⾥⾯的某⼀具体过程
    这个⽅法常⽤来关闭硬件加速，但它的定位和定义都不只是⼀个「硬件
    加速开关」。它的作⽤是为绘制设置⼀个离屏缓冲，让后⾯的绘制都单
    独写在这个离屏缓冲内。如果参数填写 LAYER_TYPE_SOFTWARE ，
    会把离屏缓冲设置为⼀个 Bitmap ，即使⽤软件绘制来进⾏缓冲，这样
    就导致在设置离屏缓冲的同时，将硬件加速关闭了。

    但需要知道，这个⽅法被⽤来关闭硬件加速，只是因为 Android 并没有提供⼀个便捷的⽅
    法在 View 级别简单地开关硬件加速⽽已。

    （2）saveLayer() 是针对 Canvas 的，所以在 onDraw() ⾥可以使⽤ saveLayer()
    来圈出具体哪部分绘制要⽤离屏缓冲。
    然⽽……最新的⽂档表示这个⽅法太重了，能不⽤就别⽤，尽量⽤
    setLayerType() 代替
     **/
  }

  class PointFEvaluator : TypeEvaluator<PointF> {
    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
      val startX = startValue.x
      val endX = endValue.x
      val currentX = startX + (endX - startX) * fraction
      val startY = startValue.y
      val endY = endValue.y
      val currentY = startY + (endY - startY) * fraction
      return PointF(currentX, currentY)
    }
  }

  /**
   * This evaluator can be used to perform type interpolation between <code>float</code> values.
   * Float小数估值器，这个就是自带的小数估值器
   */
  /*class FloatEvaluator implements TypeEvaluator<Number> {
    public Float evaluate(float fraction, Number startValue, Number endValue) {
      float startFloat = startValue.floatValue();
      return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
  }*/
}