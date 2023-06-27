package com.wlp.myanjunote.kotlin2.withinterface6

interface IUser {
    val email: String //这个抽象属性必须被重写

    val nickname: String //可以被继承
        get() = email.substringBefore('@')
}