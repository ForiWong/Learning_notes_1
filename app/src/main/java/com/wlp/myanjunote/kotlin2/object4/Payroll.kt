package com.wlp.myanjunote.kotlin2.object4

/**
 * object 关键字：定义一个类并同时创建一个实例
 * 对象声明--定义单例
 * 伴生对象--可以持有工厂方法和其他与这个类相关
 * 对象表达式--替代Java的匿名内部类
 *
 * 对象声明：高效地定义一个类和一个该类的变量
 * 不允许声明构造方法
 *
 * #kotlin
 * Payroll.cal();
 *
 * #java
 * 在Java中调用：Payroll.INSTANCE.cal();
 *
 * 有这样一个疑问，在对象声明和伴生对象中，java代码调用总会跟上INSTANCE或者Companion，直观上感觉不太友好，
 * 能不能像kotlin代码那样可以直接调用？
 * 答案肯定是可以的。需要使用kotlin自带的注解：@JvmStatic或者@JvmField，这样就可以和kotlin调用保持一致。
 * @JvmStatic既可以修饰属性，也可以修饰方法；
 * 而@JvmField只能修饰属性
 */
object Payroll {
    val allEmployees = arrayListOf<Person>()

    fun cal(){
        for (person in allEmployees){
            //....
        }
    }
}

fun main() {
    var user1 = User.newSubscribingUser("1a2b3c@163.com")
    var user2 = User.newFacebookUser(123)
    println(user1.nickname)
    println(user2.nickname)

    var person = Person.Companion.fromJSON("www")
    println(person.name)
}