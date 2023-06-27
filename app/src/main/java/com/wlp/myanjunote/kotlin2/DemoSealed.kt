package com.wlp.myanjunote.kotlin2

//密封类的使用
//sealed class 是一种同时拥有枚举类 enum 和 普通类 class 特性的类，叫做密封类。使用起来很简单
sealed class DemoSealed {
    class Num(val value: Int) : DemoSealed()

    class Sum(val left: DemoSealed, val right: DemoSealed) : DemoSealed()
}

fun eval(e: DemoSealed): Int = //这两个一起使用非常方便
    when (e) {
        is DemoSealed.Num -> e.value
        is DemoSealed.Sum -> eval(e.right) + eval(e.right)
    }
