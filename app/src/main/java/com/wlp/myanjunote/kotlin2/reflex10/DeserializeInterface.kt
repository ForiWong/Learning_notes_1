package com.wlp.myanjunote.kotlin2.reflex10

import kotlin.reflect.KClass

//out关键字的作用：可以使用Any的子类
annotation class DeserializeInterface(val targetClass : KClass<out Any>) {
}