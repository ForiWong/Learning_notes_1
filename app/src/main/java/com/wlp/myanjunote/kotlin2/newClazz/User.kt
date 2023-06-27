package com.wlp.myanjunote.kotlin2.newClazz


open class User(
    val nickname: String,
    val isSubscribed: Boolean = true
) {//主构造方法,可以提供默认值

//    override fun toString(): String {
//        return super.toString()
//    }
//    override fun toString() = "User(name=$nickname, isSubscribed=$isSubscribed)"

    override fun equals(other: Any?): Boolean {//Any所有类的父类
        if (other == null || other !is User) //is 是否是指定类型
            return false

        return nickname == other.nickname &&
                isSubscribed == other.isSubscribed;
    }

    override fun hashCode(): Int = nickname.hashCode() * 31 + (if (isSubscribed) 1 else 0)

    fun copy(name: String = this.nickname, isSub: Boolean = this.isSubscribed) = User(name, isSub)

}

/**

class User(val nickname: String)

class UserTwo constructor(_nickname: String) {//带一个参数的主构造方法
val nickname: String

init {//初始化语句块
nickname = _nickname
}

}

kt 有主构造方法 和 从构造方法区分

构造方法：用不同的方式来初始化父类

 */