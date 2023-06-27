package com.wlp.myanjunote.kotlin2.newClazz

//constructor 构造函数关键字
class UserTwo constructor(_nickname: String) {//带一个参数的主构造方法
    val nickname: String

    init {//初始化语句块
        nickname = _nickname
    }
}