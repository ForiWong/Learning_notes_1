package com.wlp.myanjunote.kotlin2.delegateby

// 实现此接口的被委托类,差不多就是实现类的意思
class BaseImpl(val x: Int) : Base {
    override fun print() { println("DaYing:$x") }
}