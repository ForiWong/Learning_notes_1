package com.wlp.myanjunote.kotlin2.reflex10.reflect

import kotlin.reflect.full.createInstance


class Item(var name: String) {
    var price = 0.0

    constructor() : this("未知商品") {
        this.price = 0.0
    }

    constructor(name: String, price: Double) : this(name) {
        this.price = price
    }
}

fun main(args: Array<String>) {
    val clazz = Item::class
    //createInstance()方法调用无参数的构造器创建实例
    val inst1 = clazz.createInstance()
    println(inst1.name)
    println(inst1.price)
    //获取所有构造器
    val cons = clazz.constructors
    cons.forEach {
        if (it.parameters.size == 2) {
            //调用带两个参数的构造器创建实例
            val inst2 = it.call("Kotlin", 45.6)
            println(inst2.name)
            println(inst2.price)
        }
    }
}