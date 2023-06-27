package com.wlp.myanjunote.kotlin2.seven7

class DerivedClient() {

    fun main() {
        val impl = BaseImpl()
        val derived = TDerived(impl) //impl作为TDerived的Base委托对象
        derived.print()  //调用derived.print()实际委托给b即BaseImpl的print方法，所以会打印出abc
        println(derived.str)  //TDerived重写了str，所以此处调用str时调用的是TDerived重新的str，而不是委托给b，所以此处打印的是derived
    }
}