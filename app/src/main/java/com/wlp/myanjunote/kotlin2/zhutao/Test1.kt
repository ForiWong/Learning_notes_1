package com.wlp.myanjunote.kotlin2.zhutao

import android.content.Context
import android.widget.Toast


fun main(args: Array<String>) {
    var valueDouble = 1.22//Double
    var valueDouble2 : Double = 1.22

    var valueFloat = 1.2F//Float
    var valueFloat2 : Float = 1.2F

    var valueLong = 1234L
    var valueInt : Int = 1234//对应为Java的int
    var valueIntNull : Int? = 12345//可空类型对应为Integer

    //var one : Integer = 123//java.long.Integer 如此编译不通过
    var two : Integer = Integer(123) //can compiles

    getType(valueInt)
    getType(valueIntNull)//必须是Any? 而不是 Any
    getType(two)


    var value16 = 0X66
    var valueBinary = 0B010101
    var valueShort: Short = 12
    var valueByte: Byte = 1 //-128 ^ 127

    /*
    The root of the Kotlin class hierarchy. Every Kotlin class has Any as a superclass.
    Kotlin类层次结构的根,每个Kotlin类都有Any作为超类。
    Kotlin中的Any和Java中的Object是什么关系, 他们是各自语言的所有的类的超类。
    Object是Java中所有类的超类, Any是Kotlin的所有超类, 他们之间啥关系....
    用上面的代码可以看出来, 在kotlin中他们其实是等价的.

    Object:
    在Java中，Object是Java类层级的根，是所有引用类的超类，即引用类型的根，而基本数据类型像int、float、boolean等则不是
    类层级的结构的一部分。这意味着当你需要使用Object的时候，你必须使用Java.lang.Integer这样的包装类来表示基本数据类型的值。

    Any
    Any类型是Kotlin所有非空类型的超类型（非空类的根）,包含像Int这样的基本数据类型。
    在Kotlin中，把基本类型的值直接赋值给Any类的变量时会自动装箱

    val aValue:Any = 42

    Any是非空类，所以Any类型的变量不持有null值。如果你需要可以持有任何可能值得变量，包含null在内，则必须使用Any？类型。

    在底层，Any类型对应java.lang.Object，在Kotlin函数使用Any 时，它会编译成为Java字节码的Object。

    */
    var vAny : Any = 123
    var vAny2 : Any? = 123
    var vObject : Object = Object()

    var o = Object()
    var any = Any()
    println(o is Any)//true
    println(any is Object)//true
    println(vAny2 is Object)//true

    //变量定义
    var variableV = 123
    val variableV2 = 123

    var test1 = Test1()
    println(test1)//com.wlp.myanjunote.kotlin2.zhutao.Test1@511baa65

    var aa: String? = "Kotlin"
    //print(aa.length) // 编译器报错，因为 a 是可为空的类型
    aa = null
    println(aa?.length) // 使用?. 的方式调用，输出 null
    aa = ""
    println(aa?.length) // 0

    var b : String? = "qwe"
    //val l: Int = if (b != null) b.length else -1
    val l2 = b?.length ?: -1
    println(l2)

    //不安全的类型转换 as
    val y1 = null
    //val x: String = y1 as String
    //抛异常，null 不能被转换成 String

    //安全的类型转换 as?
    val y2 = null
    val z: String? = y2 as? String
    println(z)
    // 输出 null

    val a: Int = 128
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(a == boxedA)//true
    println(a === boxedA)//true
    print(boxedA == anotherBoxedA)//true
    print(boxedA === anotherBoxedA)//false
    // 输出什么内容?

    val a1: Int = 127
    val boxedA1: Int? = a1
    val anotherBoxedA1: Int? = a1
    println(a1 == boxedA1)//true
    println(a1 === boxedA1)//true
    print(boxedA1 == anotherBoxedA1)//true
    print(boxedA1 === anotherBoxedA1)//true
    // 输出什么内容?

    /*
    -128 ^ 127 如果该范围内的装箱整数具有相同的数值，则它们始终是同一对象。
    IntegerCache
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
    */

    var person4 = Person4()
    //init 代码块执行时机在类构造之后，但又在“次构造器”执行之前。
    //I am Kotlin.
    //I am constructor


    val htmlContent = html {
        head {
            title { "Kotlin Jetpack In Action" }
        }
        body {
            h1 { "Kotlin Jetpack In Action"}
            p { "-----------------------------------------" }
            p { "A super-simple project demonstrating how to use Kotlin and Jetpack step by step." }
            p { "-----------------------------------------" }
            p { "I made this project as simple as possible," +
                    " so that we can focus on how to use Kotlin and Jetpack" +
                    " rather than understanding business logic." }
            p {"We will rewrite it from \"Java + MVC\" to" +
                    " \"Kotlin + Coroutines + Jetpack + Clean MVVM\"," +
                    " line by line, commit by commit."}
            p { "-----------------------------------------" }
            p { "ScreenShot:" }
            img(src = "https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/6/15/172b55ce7bf25419~tplv-t2oaga2asx-image.image",
                alt = "Kotlin Jetpack In Action")
        }
    }.toString()

    println(htmlContent)

}

/**
1234 is Integer
12345 is Integer
123 is Integer
既然是同等对待的 Int 与 Integer
 */
fun getType(v : Any?) = when(v) {
    is Integer -> println("$v is Integer")//java.lang.Integer
    is Int -> println("$v is Int")
    else -> println("$v is other")
}


class Test1{

    var vInt = 123
    var valueIntNull : Int? = 1234

    var valueAny : Any = 123

    val vIntL = 123

    var dd = "cc"

    //val与final差别之处目前发现仅在于如下特殊写法
    //val的特殊写法 此处isTrue随着当前对象dd的值改变而改变，但final若初始化值后则无法发生更改
    //这种写法就很离谱了啊
    val isDd : Boolean
        get() {
            return this.dd=="cc"
        }

    val value by lazy { 20 }

    val num by lazy(LazyThreadSafetyMode.PUBLICATION) { "123" }

}


// 为 Context 类定义一个 toast 方法
fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, length).show()
}

fun Context?.toast2(msg: String, length: Int = Toast.LENGTH_SHORT){
    if (this != null) {
        Toast.makeText(this, msg, length).show()
    }
}

