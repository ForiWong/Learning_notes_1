package com.wlp.myanjunote.kotlin2.reflex10

data class Person(@JsonName("alias") val name: String,/*@JsonExclude*/ val age: Int? = null)