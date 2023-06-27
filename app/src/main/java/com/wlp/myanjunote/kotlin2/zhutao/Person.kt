package com.wlp.myanjunote.kotlin2.zhutao

class Person1 private constructor(firstName : String) {

}

class Person2 (firstName : String) {

}

class Person3 {

    constructor(name : String){

    }
}

class Person4 {
    var name = "Kotlin"

    init {
        name = "I am Kotlin."
        println(name)
    }

    constructor(){
        name = "I am constructor"
        println(name)
    }
}