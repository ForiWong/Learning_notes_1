package com.wlp.myanjunote.kotlin2.seven7

import kotlin.reflect.KProperty

class Delegate(var a: Int) {

    operator fun getValue(foo: Foo, property: KProperty<*>): Int {
        return a
    }

    operator fun setValue(foo: Foo, property: KProperty<*>, i: Int) {
        a = i + 100
    }
}