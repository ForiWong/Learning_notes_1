package com.wlp.myanjunote.kotlin.base

import android.util.Log
import android.view.View

/**
 * Created by wlp on 2023/3/28
 * Description:
 */
class HighFun07 {
    /**
    高阶函数
    借助高阶函数，我们一个接口都不必定义。

    Kotlin 语言的设计者是怎么做的呢？实际上他们是分成了两个部分：
    用函数类型替代接口定义；
    用 Lambda 表达式作为函数参数；
     */

    //View.kt
    //                     (View) -> Unit 就是「函数类型 」
    //                       ↑        ↑
    var mOnClickListener: ((View) -> Unit)? = null
    var mOnContextClickListener: ((View) -> Unit)? = null

    // 高阶函数
    fun setOnClickListener(l: (View) -> Unit) {
        mOnClickListener = l;
    }

    // 高阶函数
    fun setOnContextClickListener(l: (View) -> Unit) {
        mOnContextClickListener = l;
    }

    //函数类型（Function Type）就是函数的类型。
    //         (Int,  Int) ->Float 这就是 add 函数的类型
    //           ↑     ↑      ↑
    fun add(a: Int, b: Int): Float { return (a+b).toFloat() }

    // 函数赋值给变量                    函数引用
    //    ↑                              ↑
    val function: (Int, Int) -> Float = ::add

    //高阶函数是将函数用作参数或返回值的函数

    //Lambda
    fun onClick(v: View): Unit {
        //...
    }

    fun test(){
        setOnClickListener(::onClick)

        //用 Lambda 表达式来替代函数引用
        setOnClickListener { v: View ->
            v.id
        }

        // 在实际开发中，我们经常使用这种简化方式
        setOnClickListener { Log.d("TAG", "test: ${it.id}") }
    }

    /**
     * OnClickListener 符合 SAM 转换的要求
     *
     * SAM 是 Single Abstract Method 的缩写，意思就是只有一个抽象方法的类或者接口。但在 Kotlin 和 Java 8
     * 里，SAM 代表着只有一个抽象方法的接口。只要是符合 SAM 要求的接口，编译器就能进行 SAM 转换，也就是我
     * 们可以使用 Lambda 表达式，来简写接口类的参数。
     */

    /**
     * 还有一个特殊的概念，叫做带接收者的函数类型
     *
     */

    //从外表上看，带接收者的函数类型，就等价于成员方法。但从本质上讲，它仍是通过编译器注入 this 来实现的。
    fun test2(){
        val person = Person("name", 12)
        person.apply {
            val a = name
            val age = age
        }
    }
//    public inline fun <T> T.apply(block: T.() -> Unit): T {
//        contract {
//            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//        }
//        block()
//        return this
//    }

    // Kotlin高阶函数：
    // 比如 Kotlin 官方的源代码StandardKt，你可以去分析其中的 with、let、also、takeIf、repeat、apply
    // 还有就是CollectionsKt，你可以去分析其中的 map、flatMap、fold、groupBy 等操作符
}
