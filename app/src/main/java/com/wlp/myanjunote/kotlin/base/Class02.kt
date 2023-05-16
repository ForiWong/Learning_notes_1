package com.wlp.myanjunote.kotlin.base

import android.util.Log

/**
 * Created by wlp on 2023/3/23
 * Description:

面向对象编程：类、接口、继承、嵌套，以及 Kotlin 独有的数据类和密封类、密封接口。

 */


/**
 *
 * name 用val修饰，对应的有getter方法，没有setter
 * age 用var修饰，有getter\setter
 *
 * Kotlin 定义的类，在默认情况下是 public 的，编译器会帮我们生成“构造函数”，对于类当中的属性，
 * Kotlin 编译器也会根据实际情况，自动生成 getter 和 setter。
 **/
class Person(val name: String, var age: Int){
    //增加一个方法，判断是否是成年人
    fun isAdult(): Boolean { return age >= 18 }
    //简化
    fun isAdult2() = age >= 18
    //自定义getter方法
    val isAdult3
        get() = age >= 18
    //假如需要干点其他的
    val isAdult4: Boolean//todo 这里用val修饰有问题吗？
        get() {
        // do something else
        return age >= 18
    }
    // 虽然是增加了一个属性isAdult
    // 在实现层面，isAdult 仍然还是个方法。这也就意味着，isAdult 本身不会占用内存，它的性能和
    // 我们用 Java 写的方法是一样的。

    // todo 如果简化为这样呢？实际是不可行的
    val isAdult5 = age >= 18
}

class Person2(val name: String) {
    //自定义属性 setter
    var age: Int = 0
    // 这就是age属性的setter
    // ↓
        set(value: Int) {//private 也可以设置成私有的
        Log.d("打印", "value: $value")
        field = value //对 age 的赋值操作

    }
}

abstract class Person3(val name: String){
    abstract fun walk()

}
/**
 * 只能继承抽象类，或者是open类
 *
 * Kotlin 的类，默认是不允许继承的，除非这个类明确被 open 关键字修饰了。
 * 另外，对于被 open 修饰的普通类，它内部的方法和属性，默认也是不允许重写的，除非它们也被 open 修饰了：
 *
 * Java 的继承是默认开放的，Kotlin 的继承是默认封闭的。
 * */

//接口 与 实现
interface Behavior {
    // 接口内的可以有属性
    val canWalk: Boolean

    // 接口方法的默认实现
    fun walk() {
        if (canWalk) {
            // do something
        }
    }
}

class Person4(val name: String): Behavior {
    // 重写接口的属性
    override val canWalk: Boolean
        get() = true
}


class A {
    val name: String = ""
    fun foo() = 1


//    class B {
//        val a = name   // 报错
//        val b = foo()  // 报错
//    }

    //Kotlin 当中的普通嵌套类，它的本质是静态的
    //Kotlin 当中定义一个普通的内部类，我们需要在嵌套类的前面加上 inner 关键字。
    inner class C {
        val a = name // 通过
        val b = foo() // 通过
    }

    class D{

    }

}


class Class02 {

    fun test1(){
        val person = Person("abc", 12)
        person.isAdult5

        val innerC = A().C()//普通的内部类
        val staticD = A.D()//默认的静态类
    }

    fun test2(){
        val tom = Person10("Tom", 18)
        val jack = Person10("Jack", 19)

        println(tom.equals(jack)) // 输出：false
        println(tom.hashCode())   // 输出：对应的hash code
        println(tom.toString())   // 输出：Person(name=Tom, age=18)

        //使用了数据类的解构声明。这种方式，可以让我们快速通过数据类来创建一连串的变量。
        val (name, age) = tom     // name=Tom, age=18
        println("name is $name, age is $age .")

        val mike = tom.copy(name = "Mike") //copy方法
        println(mike)             // 输出：Person(name=Mike, age=18)
    }

    fun display(data: Result<Any?>) = when(data) {
        is Result.Success -> log(1)
        is Result.Error -> log(2)
        else -> log(3)
    }

    fun log(a: Int){
        Log.d("log", "$a")
    }
}


// 数据类用于存放数据的类。当中，最少要有一个属性
data class Person10(val name: String, val age: Int)

//枚举
enum class Human {
    MAN, WOMAN
}

fun isMan(data: Human) = when(data) {
    Human.MAN -> true
    Human.WOMAN -> false
    // 这里不需要else分支，编译器自动推导出逻辑已完备
}

//密封类
// 密封类是它的子类是固定的，枚举类是对象固定的
sealed class Result<out R> {
    data class Success<out T>(val data: T, val message: String = "") : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()

    data class Loading(val time: Long = System.currentTimeMillis()) : Result<Nothing>()
}

//todo 可以继承买房类吗
class Son() : Result<Any?>(){

}