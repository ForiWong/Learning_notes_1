package com.wlp.myanjunote.kotlin.delegate09

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
委托类
Kotlin“委托类”委托的是接口方法，而“委托属性”委托的，则是属性的 getter、setter。

1、标准委托
Kotlin 提供了好几种标准委托，其中包括两个属性之间的直接委托、by lazy 懒加载委托、Delegates.observable
观察者委托，以及 by map 映射委托。前面两个的使用频率比较高，后面两个频率比较低。
 */

/**(1)将属性 A 委托给属性 B
 *为什么要分别定义 count 和 total？我们直接用 count 不好吗？这个特性，其实对我们软件版本之间的兼容很有帮助。
 */
class Item {
    var count: Int = 0

    //              ①  ②
    //              ↓   ↓
    var total: Int by ::count
}


//(2)定义懒加载委托
//               ↓   ↓
val data: String by lazy {
    request()
}

fun request(): String {
    println("执行网络请求")
    return "网络数据"
}

fun main() {
    println("开始")
    println(data)
    println(data)
}
//结果：
//开始
//执行网络请求
//网络数据
//网络数据

class Delegate {

}

//2、自定义委托
class StringDelegate(private var s: String = "Hello") {
    //     ①                           ②                              ③
    //     ↓                            ↓                               ↓
    operator fun getValue(thisRef: Owner, property: KProperty<*>): String {
        return s
    }

    //      ①                          ②                                     ③
    //      ↓                           ↓                                      ↓
    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: String) {
        s = value
    }
}

//      ②
//      ↓
class Owner {
    //               ③
    //               ↓
    var text: String by StringDelegate()
}


class StringDelegate2(private var s: String = "Hello") : ReadWriteProperty<Owner, String> {
    override operator fun getValue(thisRef: Owner, property: KProperty<*>): String {
        return s
    }

    override operator fun setValue(thisRef: Owner, property: KProperty<*>, value: String) {
        s = value
    }
}

//todo 提供委托???
//class SmartDelegator {
//
//    operator fun provideDelegate(thisRef: Owner, prop: KProperty<*>): ReadWriteProperty<Owner, String> {
//        return if (prop.name.contains("log")) {
//            StringDelegate("log")
//        } else {
//            StringDelegate("normal")
//        }
//    }
//}
//
//class Owner2 {
//    var normalText: String by SmartDelegator()
//    var logText: String by SmartDelegator()
//}
//
//fun main2() {
//    val owner = Owner()
//    println(owner.normalText)
//    println(owner.logText)
//}

//委托的使用
//(1)属性可见性封装
class Model {
    val data: List<String> by ::_data
    private val _data: MutableList<String> = mutableListOf()

    fun load() {
        _data.add("Hello")
    }
}

//(2)ViewModel 委托
// MainActivity.kt
//private val mainViewModel: MainViewModel by viewModels()
