package com.wlp.myanjunote.customview.bitmapanddrawable

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.wlp.myanjunote.R

/**
（1）Bitmap 是什么
Bitmap 是位图信息的存储，即⼀个矩形图像每个像素的颜⾊信息的存储器。

（2）Drawable 是什么
Drawable 是⼀个可以调⽤ Canvas 来进⾏绘制的上层⼯具。调⽤
Drawable.draw(Canvas) 可以把 Drawable 设置的绘制内容绘制到 Canvas
中。
Drawable 内部存储的是绘制规则，这个规则可以是⼀个具体的 Bitmap，也可以是
⼀个纯粹的颜⾊，甚⾄可以是⼀个抽象的、灵活的描述。Drawable 可以不含有具体
的像素信息，只要它含有的信息⾜以在 draw(Canvas) ⽅法被调⽤时进⾏绘制就
够了。
由于 Drawable 存储的只是绘制规则，因此在它的 draw() ⽅法被调⽤前，需要先
调⽤ Drawable.setBounds() 来为它设置绘制边界。

（3）Bitmap 和 Drawable 的互相转换
事实上，由于 Bitmap 和 Drawable 是两个不同的概念，因此确切地说它们并不是互
相「转换」，⽽是从其中⼀个获得另⼀个的对象：
Bitmap -> Drawable：创建⼀个 BitmapDrawable。
Drawable -> Bitmap：如果是 BitmapDrawable，使⽤BitmapDrawable.getBitmap() 直接获取；
如果不是，创建⼀个 Bitmap和⼀个 Canvas，使⽤ Drawable 通过 Canvas 把内容绘制到 Bitmap 中。

 （4）⾃定义 Drawable
1）怎么做？
重写⼏个抽象⽅法
重写 setAlpha() 的时候记得重写 getAlpha()
重写 draw(Canvas) ⽅法，然后在⾥⾯做具体的绘制⼯作
例如：MeshDrawable
2）有⽤吗？
有⽤。它就是⼀个更加抽象和专注的、仅仅⽤于绘制的⾃定义 View 模块。
3）⽤来⼲嘛？
需要共享在多个 View 之间的绘制代码，写在 Drawable ⾥，然后在多个⾃
定义 View ⾥只要引⽤相同的 Drawable 就好，⽽不⽤互相粘贴代码。
例如？
股票软件的多个蜡烛图界⾯，可以把共享的蜡烛图界⾯放进去
 **/
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    //互转：Bitmap --> drawable
    /*val bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)
    bitmap.toDrawable(resources)//bitmap --> BitmapDrawable 看看方法的源代码

    //drawable --> bitmap
    val drawable = ColorDrawable()
    drawable.toBitmap()//看看方法的源代码

    val bit = BitmapDrawable()
    bit.bitmap //getDrawable*/
  }
}