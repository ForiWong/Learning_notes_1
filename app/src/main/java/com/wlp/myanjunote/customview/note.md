##############################################################################
## 这部分代码 Fork from https://github.com/rengwuxian/HenCoderPlus.git 
## HenCoder Plus 的课上代码分享
## plus.hencoder.com
## 在此仅做笔记用
##############################################################################

21.View绘制流程源码解析
这一节视频没啥好看的！

1）如何在子线程更新UI不会报错
requestLayout()  -->  viewRootImpl.checkThread() //检查线程的方法

2）在子线程中更新 UI 不报错的四种方式
1. 主线程申请成功后子线程申请
2. 在子线程中创建ViewRootImpl
3. 利用硬件加速机制绕开requestLayout()
在硬件加速的⽀持下，如果控件只是经常了 invalidate() ，而没有触发requestLayout()
是不会 触发 ViewRootImpl#checkThread() 的。
4. SurfaceView
Android 中有一个控件 SurfaceView ，它可以通过 holder 获得 Canvas 对象，
可以直接在子线程中更新 UI。

3）view与activity的生命周期
在activity的哪个生命周期可以拿到view的大小,在哪里完成了测绘流程
activity的创建 --> ActivityThread,经过反射创建

decorView 布局 screen_simple.xml

4）view 绘制流程函数调用


//自定义控件常见的问题



------------------------------------------------------------------------
packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
            .filterNot { it.name == this::class.java.name }
            .map { Class.forName(it.name) }
            .forEach { clazz ->
                linearLayout.addView(AppCompatButton(this).apply {
                    isAllCaps = false
                    text = clazz.simpleName
                    setOnClickListener {
                        startActivity(Intent(this@MainActivity, clazz))
                    }
                })
            }

https://blog.csdn.net/weixin_42814000/article/details/108210979
应用程序包管理类：PackageManager

有些场景下，我们会需要获取一些其它 App 的各项信息，例如：App 名称，包名、Icon 等。  
这个时候就需要使用到 PackageManager 这个类了。

你首先需要获取 PackageManager（以下简称 PM） 对象，通过 PM 对象，你就可以获取到你需要的各项 App 的信息类。

这里涉及到的 App 信息类包括：PackageInfo、ApplicationInfo、ActivityInfo/ServiceInfo/ProviderInfo 等，  
还有一个 ResolveInfo 类，它比较特殊一点，不和前面的结构为从属关系。

PM 中，提供了非常多的方法，供我们通过不同的条件，获取到 PackageInfo 对象、ApplicationInfo 对象等，它是本文的基础。

(1)PackageInfo
PackageInfo 从名称上也可以看出来，它主要用于存储获取到的 Package 的一些信息，包括：包名（packageName）、版本号（versionCode）  
、版本名（versionName）。

(2)ApplicationInfo
ApplicationInfo 相对于 PackageInfo 用的会比较少一些。它主要用于获取 Apk 定义在 AndroidManifest.xml 中的一些信息。

（3）ActivityInfo
ActivityInfo、ServiceInfo、ProviderInfo 这三个是平级的，熟悉的一眼就能看出来，它们就是 Android 定义的四大组件中的几个。  
各自涵盖了一部分信息。一般在外部获取其他 App 的信息的时候，不会获取到这么细致的数据，如果有，看看这几个类准没错。


------------------------------------------------------------------------
ValueAnimator 属性动画

//    这是最基本的 Animator，它不和具体的某个对象联动，⽽是直接对两个数值进⾏渐变计算。使⽤很少。
//    其实在这里用的很方便啊
val earthAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
    duration = 10000L//持续时间
    repeatCount = INFINITE //重复次数：infinite 无限的
    interpolator = LinearInterpolator() //线性插值器，其实是一个时间插值器，最终是继承接口TimeInterpolator
    addUpdateListener { //设置动画的监听
        val params = earth.layoutParams as ConstraintLayout.LayoutParams
        params.circleAngle = 45 + it.animatedFraction * 360
        moon.requestLayout()//invalidate()
    }
}
//todo 以上动画，如果moon earth 两个控件都调用invalidate()动画是没有用的。看看requestLayout() 与 invalidate()的区别

原文链接：https://blog.csdn.net/hxl517116279/article/details/90410345
（1）requestLayout：
requestLayout会直接递归调用父窗口的requestLayout，直到ViewRootImpl,然后触发peformTraversals，由于mLayoutRequested为  
true，会导致onMeasure和onLayout被调用。不一定会触发OnDraw。requestLayout触发onDraw可能是因为在在layout过程中发现  
l,t,r,b和以前不一样，那就会触发一次invalidate，所以触发了onDraw，也可能是因为别的原因导致mDirty非空（比如在跑动画）

（2）invalidate：
view的invalidate不会导致ViewRootImpl的invalidate被调用，而是递归调用父view的invalidateChildInParent，直到ViewRootImpl  
的invalidateChildInParent，然后触发peformTraversals，会导致当前view被重绘,由于mLayoutRequested为false，不会导致  
onMeasure和onLayout被调用，而OnDraw会被调用。

postInvalidate： postInvalidate是在非UI线程中调用，invalidate则是在UI线程中调用。

总之：一般来说，只要刷新的时候就调用invalidate，需要重新measure就调用requestLayout，后面再跟个invalidate  
（为了保证重绘），这是我个人的理解。


