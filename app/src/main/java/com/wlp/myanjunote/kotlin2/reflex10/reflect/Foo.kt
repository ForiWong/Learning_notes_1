package com.wlp.myanjunote.kotlin2.reflex10.reflect

class Foo(var name: String = "未知")

//test函数的参数是（String）->Foo类型
fun test(factory: (String) -> Foo) {
    val x: Foo = factory("Kotlin")
    println(x.name)
}

fun main(args: Array<String>) {
    //通过::Foo引用Foo类的主构造器
    test(::Foo)
}