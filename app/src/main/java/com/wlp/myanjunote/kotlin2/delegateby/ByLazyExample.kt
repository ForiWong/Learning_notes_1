package com.wlp.myanjunote.kotlin2.delegateby

class ByLazyExample {

    val lazyValue: String by lazy {
        println("computed!")     // 第一次调用输出，第二次调用不执行
        "Hello"
    }

}