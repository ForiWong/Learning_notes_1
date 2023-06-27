package com.wlp.myanjunote.kotlin2.seven7

class TDerived(b: Base) : Base by b {  //将Base的实现委托给b实现
    override val str = "derived"  //重写了str，所以TDerived调用str时调用的是TDerived重写的str，而不是委托给b

}