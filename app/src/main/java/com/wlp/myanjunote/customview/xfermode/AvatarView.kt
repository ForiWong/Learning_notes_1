package com.wlp.myanjunote.customview.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp

private val IMAGE_WIDTH = 200f.dp
private val IMAGE_PADDING = 20f.dp
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)//转换模式

class AvatarView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF(//矩形
        IMAGE_PADDING,
        IMAGE_PADDING,
        IMAGE_PADDING + IMAGE_WIDTH,
        IMAGE_PADDING + IMAGE_WIDTH
    )

    override fun onDraw(canvas: Canvas) {
        //为什么要使用离屏缓冲呢？ 对比使用与不使用的区别
        val count = canvas.saveLayer(bounds, null) //canvas 离屏缓存。  相当于挖出一个矩形大小画布。这个大小越小越好，因为离屏缓冲耗费资源

        //绘制椭圆
        canvas.drawOval(
            IMAGE_PADDING,
            IMAGE_PADDING,
            IMAGE_PADDING + IMAGE_WIDTH,
            IMAGE_PADDING + IMAGE_WIDTH,
            paint
        )

        //转换方式  //所以，以上的椭圆就成了背景了
        paint.xfermode = XFERMODE //不要在onDraw()方法内创建对象

        //绘制图片
        canvas.drawBitmap(
            getAvatar(IMAGE_WIDTH.toInt()),
            IMAGE_PADDING,
            IMAGE_PADDING,
            paint
        )  //left top
        paint.xfermode = null     //绘制完之后，将xfermode还原回之前的

        canvas.restoreToCount(count)  //绘制好之后，将离屏缓存还回去，恢复
    }

    //获取图片 bitmap，对换取图片进行了优化
    fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true   //设置这个之后，是初略的读，效率快。 相当于只读图片的四边界限参数
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width   //  目标大小
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}

/**
Xfermode国外有大神称之为过渡模式，这种翻译比较贴切但恐怕不易理解，大家也可以直接称之为图像混合模式，
因为所谓的“过渡”其实就是图像混合的一种，这个方法跟我们上面讲到的setColorFilter蛮相似的

## 也就是源图像和目标图像的混合模式？？，他两要怎么重叠取舍

在API中Android为我们提供了18种（比上图多了两种ADD和OVERLAY）模式：　

　　ADD:饱和相加,对图像饱和度进行相加,不常用

　　CLEAR:清除图像

　　DARKEN:变暗,较深的颜色覆盖较浅的颜色，若两者深浅程度相同则混合

　　DST:只显示目标图像

　　DST_ATOP:在源图像和目标图像相交的地方绘制【目标图像】，在不相交的地方绘制【源图像】，相交处的效果受到源图像和目标图像alpha的影响

　　DST_IN:只在源图像和目标图像相交的地方绘制【目标图像】，绘制效果受到源图像对应地方透明度影响

　　DST_OUT:只在源图像和目标图像不相交的地方绘制【目标图像】，在相交的地方根据源图像的alpha进行过滤，源图像完全不透明则完全过滤，完全
透明则不过滤

　　DST_OVER:将目标图像放在源图像上方

　　LIGHTEN:变亮，与DARKEN相反，DARKEN和LIGHTEN生成的图像结果与Android对颜色值深浅的定义有关

　　MULTIPLY:正片叠底，源图像素颜色值乘以目标图像素颜色值除以255得到混合后图像像素颜色值

　　OVERLAY:叠加

　　SCREEN:滤色，色调均和,保留两个图层中较白的部分，较暗的部分被遮盖

　　SRC:只显示源图像

　　SRC_ATOP:在源图像和目标图像相交的地方绘制【源图像】，在不相交的地方绘制【目标图像】，相交处的效果受到源图像和目标图像alpha的影响

　　SRC_IN:只在源图像和目标图像相交的地方绘制【源图像】

　　SRC_OUT:只在源图像和目标图像不相交的地方绘制【源图像】，相交的地方根据目标图像的对应地方的alpha进行过滤，目标图像完全不透明则完全
过滤，完全透明则不过滤

　　SRC_OVER:将源图像放在目标图像上方

　　XOR:在源图像和目标图像相交的地方之外绘制它们，在相交的地方受到对应alpha和色值影响，如果完全不透明则相交处完全不绘制
 **/