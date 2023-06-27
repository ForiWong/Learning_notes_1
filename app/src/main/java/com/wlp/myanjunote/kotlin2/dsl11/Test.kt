package com.wlp.myanjunote.kotlin2.dsl11


fun main(args: Array<String>){

    //带接收者的lambda
    val s = buildString {
        append("Hello, ")
        append("World!")
    }
    println(s)

    //invoke约定
    val chris = Greetor("Chris")
    chris("Hi")
}