package com.wlp.myanjunote.kotlin2.reflex10.reflect

//定义注解
annotation class Anno

//使用3个注解修饰该类
@Deprecated("该类已经不推荐使用")
@Anno
@Suppress("UNCHECKED_CAST")
class KClassTest(age: Int) {
    var name: String = "Kotlin"

    //为该类定义一个私有的构造器
    private constructor() : this(20) {}

    //定义一个有参数的构造器
    constructor(name: String) : this(15) {
        println("执行有参数的构造器：${name}")
    }

    //定义一个无参数的info方法
    fun info() {
        println("执行无参数的info方法")
    }

    //定义一个有参数的info方法
    fun info(str: String) {
        println("执行有参数的info方法，其str参数值：${str}")
    }

    //定义一个测试用的嵌套类
    class Inner
}

//为KClassTest定义扩展方法
fun KClassTest.bar() {
    println("扩展的bar方法")
}

//为KClassTest定义扩展属性
val KClassTest.foo: Double
    get() = 2.4
