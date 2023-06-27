package com.wlp.myanjunote.kotlin2.delegateby

import kotlin.reflect.KProperty

// 委托的类
class Delegate(var a: String) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        println("$thisRef, 这里委托了 ${property.name} 属性 + a: $a")
        return a;
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
        a = value
    }
}