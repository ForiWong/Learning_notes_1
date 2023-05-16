package com.wlp.myanjunote.kotlin.generics10

/**
 * Created by wlp on 2023/3/20
 * Description: 来自扔物线 https://rengwuxian.com/kotlin-generics/
 */
class TestKt {

    fun test() {
        /**
         * 和 Java 泛型一样，Kolin 中的泛型本身也是不可变的。
         * 使用关键字 out 来支持协变，等同于 Java 中的上界通配符 ? extends。
         * 使用关键字 in 来支持逆变，等同于 Java 中的下界通配符 ? super。
         *
         * 换了个写法，但作用是完全一样的。
         * out 表示，我这个变量或者参数只用来输出，不用来输入，你只能读我不能写我；
         * in 就反过来，表示它只用来输入，不用来输出，你只能写我不能读我。
         */
        //val producer: Producer<BB> = Producer<CC>()
        val producer: Producer<out BB> = Producer<CC>()//这个是使用处声明
        val b = producer.produce()

        val list1: List<BB> = ArrayList<CC>()//默认是 List<out E>
        list1.get(0)

        //val consumer: Consumer<BB> = Consumer<AA>()
        val consumer: Consumer<in BB> = Consumer<AA>()
        consumer.consume(BB())

        val list2: MyList<in BB> = MyList<AA>()
        list2.add(BB())

        //todo *投影的使用 ？

    }

    //reified 关键字解决判断是不是T的问题，reified只能用在inline函数上
    //todo inline关键字
    inline fun <reified T> printIfTypeMatch(item: Any) {
        if (item is T) { // 👈 这里就不会在提示错误了
            println(item)
        }
    }
}

class MyList<T> {

    fun add(t: T) {

    }
}

/**
 * 可以在声明类的时候，给泛型符号加上 out 关键字，
 * 表明泛型参数 T 只会用来输出，在使用的时候就不用额外加 out 了。
 */
class Producer<out T> {
    fun produce(): T? {
        return null
    }
}

/**
 * 与 out 一样，可以在声明类的时候，给泛型参数加上 in 关键字，来表明这个泛型参数 T 只用来输入。
 */
class Consumer<in T> {
    fun consume(t: T) {
        //...
    }
}

open class AA() {

}

interface DD {

}

open class BB : AA() {

}

open class CC : BB() {

}

// T 的类型必须是 AA 的子类型
class Monster<T : AA>

// 设置多个边界可以使用 where 关键字
// T 的类型必须同时是 AA 和 DD 的子类型
class Monster2<T> where T : AA, T : DD
