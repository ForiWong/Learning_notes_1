package com.wlp.myanjunote.customview.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp

private val IMAGE_WIDTH = 200f.dp
private val IMAGE_PADDING = 20f.dp
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)//图形混合模式

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
        //默认值为false，如果设置成true，那么在解码的时候就不会返回bitmap
        options.inJustDecodeBounds = true   //设置这个之后，是初略的读，效率快。 相当于只读图片的四边界限参数
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth //原始宽度
        options.inTargetDensity = width   //目标宽度，由于inScaled默认为true，因此 会自动按照目标宽度/原始宽度的尺寸缩放
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}
/*
在Android应用里，最耗费内存的就是图片资源。而且在Android系统中，读取位图Bitmap时，分给虚拟机中的图片的堆栈大小只有8M，
如果超出了，就会出现OutOfMemory异常。所以，对于图片的内存优化，是Android应用开发中比较重要的内容。
1) 要及时回收Bitmap的内存
一般来说，如果能够获得Bitmap对象的引用，就需要及时的调用Bitmap的recycle()方法来释放Bitmap占用的内存空间，而不要等
Android系统来进行释放。
　　
　　// 先判断是否已经回收
　　if(bitmap != null && !bitmap.isRecycled()){
　　// 回收并且置为null
　　bitmap.recycle();
　　bitmap = null;
　　}
　　System.gc();

从上面的代码可以看到，bitmap.recycle()方法用于回收该Bitmap所占用的内存，接着将bitmap置空，最后使用System.gc()调用
一下系统的垃圾回收器进行回收，可以通知垃圾回收器尽快进行回收。这里需要注意的是，调用System.gc()并不能保证立即开始进行回
收过程，而只是为了加快回收的到来。

2)压缩图片
如果知道图片的像素过大，就可以对其进行缩小。那么如何才知道图片过大呢?
使用BitmapFactory.Options设置inJustDecodeBounds为true后，再使用decodeFile()等方法，并不会真正的分配空间，即解码
出来的Bitmap为null，但是可计算出原始图片的宽度和高度，即options.outWidth和options.outHeight。通过这两个值，就可以
知道图片是否过大了。
　　
    BitmapFactory.Options opts = new BitmapFactory.Options();
    // 设置inJustDecodeBounds为true
　　opts.inJustDecodeBounds = true;
　　// 使用decodeFile方法得到图片的宽和高
　　BitmapFactory.decodeFile(path, opts);
　　// 打印出图片的宽和高
　　Log.d("example", opts.outWidth + "," + opts.outHeight);

*/

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