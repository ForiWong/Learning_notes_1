package com.wlp.myanjunote.kotlin2.reflex10.reflect

import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions

class CallFunction {
    fun test(msg: String) {
        println("执行带String参数的test方法：${msg}")
    }

    fun test(msg: String, price: Double) {
        println("执行带String，Double参数的test方法：${msg},${price}")
    }
}

fun main(args: Array<String>) {
    val clazz = CallFunction::class
    //创建实例
    val ins = clazz.createInstance()
    //获取clazz所代表类直接定义的全部函数
    val funs = clazz.declaredFunctions
    for (f in funs) {
        //如果函数具有3个参数
        if (f.parameters.size == 3) {
            //调用3个参数的函数
            f.call(ins, "Kotlin", 45.6)
        }

        //如果函数具有2个参数
        if (f.parameters.size == 2) {
            //调用带2个参数的函数
            f.call(ins, "Kotlin")
        }
    }
}