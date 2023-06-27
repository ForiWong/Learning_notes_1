package com.wlp.myanjunote.kotlin2.withinterface6

class SubscribingUser(val email: String): User {
    override val nickname: String
        get() = email.substringBefore("@")//提供一个getter方法
}