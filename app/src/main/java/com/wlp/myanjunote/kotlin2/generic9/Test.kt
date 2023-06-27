package com.wlp.myanjunote.kotlin2.generic9

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.lang.IllegalArgumentException

fun main(args: Array<String>){
    val authors = mutableListOf<String>()//可变的list
    val readers : MutableList<String> = mutableListOf()

    readers.filter { it !in authors }

    val serviceImpl = String::javaClass

    val letters = ('a'..'z').toList()
    println(letters.slice<Char>(0..2))
    println(letters.slice(10..13))

    val nullPro = Processor<String?>()//可空类型
    nullPro.process(null)

//    if(letters is List<String>){ //报错：Cannot check for instance of erased type: List<String> 无法检查擦除类型的实例
//
//    }

    if(letters is List<*>){//换成这样时可以的is List<*> 或者 is List

    }

//    printSum2(setOf(1, 2, 3))//List is excepted
    printSum2(listOf(1, 2, 3))//6
//    printSum2(listOf('1', '2', '3'))

    println(isA<String>("abc"))//true
    println(isA<String>(123))//false

    val items = listOf("One", 2, "three")
    println(items.filterIsInstance<String>())//[One, three] 从集合中过滤出那些指定类型的数据
    //public inline fun <reified R> Iterable<*>.filterIsInstance(): List<@kotlin.internal.NoInfer R>

    val strings = mutableListOf("abc", "bac")
    //addAnswer(strings)
    //如果函数添加或者替换了列表中的元素就是不安全的
}

//声明泛型类
//interface List<T>{
//    operator fun get(index: Int): T
//}
//
//class StringList: List<String>{
//
//    override fun get(index: Int): String {
//        TODO("Not yet implemented")
//    }
//}
//
//class ArrayList<M>: List<M>{
//    override fun get(index: Int): M {
//        TODO("Not yet implemented")
//    }
//}

//interface Comparable<T>{
//    operator fun compareTo(other: T): Int
//}

//class String: Comparable<String>{
//
//    override fun compareTo(other: String): Int {
//        TODO("Not yet implemented")
//    }
//}

//fun<T: Number> List<T>.sum():T{
//    return T
//}

/**
实化类型参数
声明点类型

====
1、泛型类型参数
泛型允许你定义带类型形参的类型
List<String>  //这个String就是类型形参   List<String>--就是整体的类型，就是【带类型形参的】类型
也可以给一个类声明多个类型形参
class Map<K, V>
-- Map<String, Person>

//在变量声明中说明泛型的类型，显式说明
val readers: MutableList<String> = mutableListOf()
//在创建列表的函数中说明类型实参，被推导出
val readers1 = mutableListOf<String>()

2、泛型函数和属性
泛型函数  [类型形参]在每次函数调用时都必须替代成具体的[类型实参]

val letters = ('a'..'z').toList()
println(letters.slice<Char>(0..2))
println(letters.slice(10..13))

public fun <T> List<T>.slice(indices: IntRange): List<T>

可以给类或接口的方法、顶层函数，以及扩展函数声明类型参数

 3、声明泛型类

 4、类型参数约束
 可以限制作为泛型类和泛型函数的类型实参的类型
 比如定义一个sum函数，只能是Int\Double这样的
 而不能是String这样的
 来做一个限制。

 上界约束：必须是这个具体类型或它的子类

 fun<T: Number> List<T>.sum():T

 5、让类型形参非空
 没有使用上界的类型形参默认是Any?
 如果你想非空的话，就指定Any

class Processor<T: Any>{
fun process(value: T){
value?.hashCode()
}
}

 ====
 1、运行时的泛型：擦除和实化类型参数
（1）运行时的泛型：类型检查和转换
和Java一样，kotlin的泛型在运行时也被擦除了。这意味着泛型类实例不会携带用于创建它的类型实参的信息。
 比如你创建了List<String>在运行时，你能判断时List，但时不知道时String类型但数据
 只有编译器知道这个类型实参，后面就被擦除了。

if(letters is List<String>){ //报错：Cannot check for instance of erased type: List<String> 无法检查擦除类型的实例

if(letters is List<*>){//换成这样时可以的is List<*> 或者 is List
 //这样你就你能判断它是否是一个未知类型实参的泛型类型了

 关于as 和 as? 的使用

（2）声明带实化类型参数的函数
 泛型函数的类型实参也实会被擦除的

fun<T> isA(value: Any) = value is T //Cannot check for instance of erased type: T

 通常情况下是这样的，只有一种情况是例外：内联函数。
 内联函数的类型形参能够被实化，意味着在运行时你可以知道实际的类型实参
 总结：inline函数的类型参数可以被实化

（3）使用实化类型参数代替类引用
常见的场景是接受java.lang.Class类型参数的API
 比如接受Service、Activity
 **/

class Processor<T>{//没有使用上界的类型形参默认是Any?
    fun process(value: T){
        value?.hashCode()
    }
}

fun printSum2(c: Collection<*>){
    val intList = c as? List<Int> //警告：Unchecked cast: Collection<*> to List<Int>
        ?: throw IllegalArgumentException("List is excepted")
    println(intList.sum())
}

fun printSum3(c: Collection<Int>){
    if (c is List<Int>){ //当编译器知道相应的类型信息时，is 检查时允许的
        //...
    }
}

//fun<T> isA(value: Any) = value is T //Cannot check for instance of erased type: T

inline fun<reified T> isA(value: Any) = value is T//这样是可以的  reified具体化的，实化的

inline fun<reified T: Activity> Context.startActivity(){
    startActivity(Intent(this, T::class.java))
}

/**
 * 变型：泛型和子类型化
 * 1、为什么存在变型：给函数传递实参
 *
 * 2、类、类型和子类型
 * 非泛型类：类的名称可以当作类型使用
 * var x: String  类String  类型String
 * var x: String?            类型String?,可空类型
 *
 * 泛型类:需要一个作为类型实参的具体类型
 *    类 list
 *    类型 List<Int> List<String>......
 *
 * 子类型：只有值的类型是变量类型的子类型时，才允许变量存储该值。
 * Int 是Number的子类型
 *
 * 超类型：
 * 如果A是B的子类型，那么B就是A的超类型
 *
 * 非空类型A是可空A?的子类型
 *
 * 如果类型A就不是类型B的超类型，也不是子类型，就称为该类型参数上是不变型的。
 *
 * 3、协变：保留子类型化关系
 * A是B的子类型，那么Producer<A>就是Producer<B>的子类型
 * 要声明类在某个类型参数上是可以协变的，out关键字
 * interface Producer<out T> { //类被声明在T上协变
 *    fun produce(): T
 * }
 *
 * 4、out 生产类型
 * in  消费类型
 *
 * interface Transformer<T>{
 *   fun transformer(t: T): T
 *   //              in      out
 * }
 *
 * 5、逆变：反转子类型化关系
 * 逆变的概念可以被看成是协变的镜像。
 * interface Comparator<in T> {
 *      fun compare(e1: T, e2: T): Int{
 *          //....
 *      }
 * }
 *
 * 协变                                  逆变                                不变型
 * Producer<out T>                      Consumer<in T>                      MutableList<T>
 * 类的子类型化保留了：Producer<Cat>       子类型化反转了：Consumer<Animal>是     没有子类型化
 * 是Producer<Animal>的子类型             Consumer<Cat> 的子类型
 * T 只能在out位置                        T只能在in位置                        T可以在任何位置
 *
 * 6、使用点变型：在类型出现的地方指定变型
 *
 * 7、星号投影：使用* 代替类型参数
 *
 */
fun addAnswer(list: MutableList<Any>){
    list.add(22)
}


