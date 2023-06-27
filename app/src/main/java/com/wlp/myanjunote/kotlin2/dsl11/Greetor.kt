package com.wlp.myanjunote.kotlin2.dsl11

class Greetor(val name: String) {

    operator fun invoke(greeting: String){
        println("$name, $greeting")
    }

}