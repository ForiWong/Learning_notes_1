package com.wlp.myanjunote.customview.clipandcamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp

private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

//todo 基础简单，但是翻页效果的例子没看懂
class CameraView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)//如果是裁剪，一定会不平滑
    private val bitmap = getAvatar(BITMAP_SIZE.toInt())
    private val clipped = Path().apply {
        addOval(//椭圆
            BITMAP_PADDING,
            BITMAP_PADDING,
            BITMAP_PADDING + BITMAP_SIZE,
            BITMAP_PADDING + BITMAP_SIZE,
            Path.Direction.CCW
        )
    }

    private val camera = Camera()//Creates a new camera, with empty transformations.

    init {
        camera.rotateX(30f)//旋转
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)//这里设置了一个动态的，与设备像素密度有关
    }

    //todo 翻页动画效果?

    override fun onDraw(canvas: Canvas) {
        /**
        Canvas 的⼏何变换
        translate(x, y)
        rotate(degree)
        scale(x, y)
        skew(x, y)
        重点：Canvas 的⼏何变换⽅法参照的是 View 的坐标系，⽽绘制⽅法（drawXxx()）参照的是 Canvas ⾃⼰的坐标系
         * */
        //（1）平移 可以先平移，再绘制也可以
        //一开始，canvas和view的坐标系是重合的，先平移一下，之后的canvas绘制的x\y是相对于canvas自己的坐标系的
        //canvas.translate(50f, 50f)
        //(2)旋转
        //先平移再旋转； 或者先旋转，再平移，最后绘制得到的结果是一样的
        //canvas.rotate(-30f)
        //（3）缩放
        //canvas.scale(2f, 2f)
        //（4）倾斜
        //canvas.skew(0.8f, 0.8f)
        //canvas.drawBitmap(bitmap, 0f, 0f, paint)

        /**
        Matrix 的⼏何变换
        preTranslate(x, y) / postTranslate(x, y)
        preRotate(degree) / postRotate(degree)
        preScale(x, y) / postScale(x, y)
        preSkew(x, y) / postSkew(x, y)
        其中 preXxx() 效果和 Canvas 的准同名⽅法相同， postXxx() 效果和 Canvas的准同名⽅法顺序相反。
        注意
        如果多次绘制时重复使⽤ Matrix，在使⽤之前需要⽤ Matrix.reset() 来把Matrix 重置。
         **/
        //canvas.drawBitmap(bitmap, 0f, 0f, paint)

        /**
         * clipRect() 裁剪
         * clipPath()
         * clipOutRect() 反向裁剪
         * clipOutPath()
         **/
        //(1)范围裁剪：通过clipPath,头像效果
        //canvas.clipPath(clipped) //仔细看，切出来的圆为什么没有抗锯⻮效果？因为「强⾏切边」
        //（2）范围裁剪：clipRect() 裁剪出左上的1/4部分
        //canvas.clipRect(BITMAP_PADDING, BITMAP_PADDING, BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        //canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)

        /**
        使⽤ Camera 做三维旋转
        rotate() / rotateX() / rotateY() / rotateZ()
        translate()
        setLocation()
        其中，⼀般只⽤ rotateX() 和 rorateY() 来做沿 x 轴或 y 轴的旋转，以及使
        ⽤ setLocation() 来调整放缩的视觉幅度。
        对 Camera 变换之后，要⽤ Camera.applyToCanvas(Canvas) 来应⽤到 Canvas。

        setLocation() //意思：差不多就是一个光源，将canvas的图投影到view上面

        这个⽅法⼀般前两个参数都填 0，第三个参数为负值。由于这个值的单位是硬编码写
        死的，因此像素密度越⾼的⼿机，相当于 Camera 距离 View 越近，所以最好把这个
        值写成与机器的 density 成正⽐的⼀个负值，例如 -6 * density。
         * */
        // 上半部分
        canvas.save()//todo 与restore()成対出现
        //旋转
        canvas.translate((BITMAP_PADDING + BITMAP_SIZE / 2), (BITMAP_PADDING + BITMAP_SIZE / 2))
        canvas.rotate(-30f)
        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0f)
        canvas.rotate(30f)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE / 2), -(BITMAP_PADDING + BITMAP_SIZE / 2))
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()

        // 下半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
        canvas.rotate(-30f)
        camera.applyToCanvas(canvas)//camera 相当于在Z坐标上一个光点，将图投影到x、y面
        canvas.clipRect(-BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)
        canvas.rotate(30f)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE / 2), -(BITMAP_PADDING + BITMAP_SIZE / 2))
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()
    }

    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}