package com.wlp.myanjunote.kotlin2.lam5

import com.wlp.myanjunote.kotlin2.Person

/**
 * Lambda表达式，本质上就是可以传递给其他函数的一小段代码.
 * 函数式编程；把函数当作值来对待。 替换了java通过你们内部类来实现,有助于消除冗余代码
 *
 * //lambda
 * binding.fab.setOnClickListener { view ->
 *   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
 *   .setAction("Action", null).show()
 * }
 *
 **/
class LambdaDemo {
    val num:Int = 0

    fun findMax(){
        val people = listOf<Person>(Person("abc", 123), Person("def", 456))

        //people.maxOf { it.age }

        val names = people.joinToString (separator = " ", transform = {p: Person -> p.name})

        val names2 = people.joinToString (" ") {p: Person -> p.name}

        //调用
        test()
        test2(3, 5)

        test3(10, sum(1,2))

        println(test(10,
            {num1: Int, num2: Int ->  num1 * num2}))
    }

    //在类内部使用lambda可以访问到这个类到参数
    val sum = {x:Int, y:Int ->
        println("val this sum of $x and $y")
        x + y + num}

    fun main(){
        println(sum(1,2))
    }

    //无参数
    //fun test() { println("无参数") }

    //lambda
    val test = { println("无参数")}

    fun test1(a: Int, b: Int){
        a + b
    }

    val test2 = {a:Int, b:Int -> a + b}

    fun test3(a:Int, b:Int): Int{
        return a + b
    }

    fun sum(num1:Int, num2:Int): Int{
        return num1 + num2
    }

    //lambda b是函数
    fun test(a: Int, b: (num1: Int, num2:Int) -> Int): Int{
        return a + b.invoke(3,5)
    }

}