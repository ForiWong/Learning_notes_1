package com.wlp.myanjunote.kotlin2.reflex10

interface ValueSerializer<T> {

    fun toJsonValue(t : T) : Any?

    fun fromJsonValue(jsonValue : Any?) :T

}