package com.wlp.myanjunote.kotlin2.object4

class User private constructor(val nickname: String) {

    //使用工厂方法替代从构造方法， 声明伴生对象
    companion object{
        fun newSubscribingUser(email: String) = User(email.substringBefore('@'))

        fun newFacebookUser(accountId: Int) = User(accountId.toString())
    }

}