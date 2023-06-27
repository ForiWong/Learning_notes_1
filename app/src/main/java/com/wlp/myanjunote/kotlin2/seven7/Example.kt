package com.wlp.myanjunote.kotlin2.seven7

class Example {
    //使用 by 关键字
    var param: String by TrimDelegate()

    fun getRes(): String{
        return param
    }
}
