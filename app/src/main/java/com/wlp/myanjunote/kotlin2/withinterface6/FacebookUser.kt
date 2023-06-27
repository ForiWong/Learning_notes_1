package com.wlp.myanjunote.kotlin2.withinterface6

class FacebookUser(val accountId: Int) : User {

    override val nickname: String
        get() = accountId.toString()

}