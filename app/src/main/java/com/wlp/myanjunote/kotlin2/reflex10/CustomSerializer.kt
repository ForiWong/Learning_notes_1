package com.wlp.myanjunote.kotlin2.reflex10

import kotlin.reflect.KClass

annotation class CustomSerializer(val serializeClass : KClass<out ValueSerializer<*>>) {
}