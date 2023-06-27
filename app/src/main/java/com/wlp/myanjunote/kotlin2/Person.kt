package com.wlp.myanjunote.kotlin2

import android.icu.number.IntegerWidth
import java.lang.IllegalArgumentException
import java.util.*

data class Person(val name: String, val age: Int? = null)
/**
 * 数据类包含两个属性name\age
 * age 声明为可空类型，默认式null
 */

//顶层函数
fun doTest(){
    val person1 = listOf(Person("Alice"), //name 是默认的，是非空的
                        Person("Bob", age = 29))

    val person2 = listOf(Person("efg"), Person("abs", 36))

    val oldest = person2.maxOf { it.age ?: 0 }

    println("The oldest is: $oldest") //The oldest is: 29
}

/*
*更简洁、更高效、更安全
* 静态类型的编程语言
* 类型推导
* 函数式编程
* 设计哲学：务实、简洁、安全、互操作性
*
*/

fun main(args: Array<String>){
    println("hello world")
}

fun max (a: Int, b: Int):Int {
    return if(a > b) a else b
}

fun max2(a: Int, b: Int):Int = if(a > b) a else b

//表达式函数  类型推导
fun max3(a: Int, b: Int) = if(a > b) a else b

class Person2(
    val name:String, //只读属性 getter val
    var isMarried: Boolean //可写属性 setter / getter
)


//如果有需要，也可以声明自定义的访问器
class Rect(val height: Int, val width: Int){
//    val isSquare: Boolean
//        get() {
//            return height == width
//        }

    val isSquare: Boolean
        get() = height == width //上面的可以简写
}


//enum 关键字只有在class前面时才有意义
//枚举可以声明属性和方法
enum class Color(
    val r: Int, val g: Int, val b: Int
    ){
    RED(255, 0, 0),
    BLUE(255, 255, 0),
    BLACK(0,0,0); //注意这个分号

    fun rgb() = (r*256 + g) * 256 + b //方法
}

fun getMnemonic(color: Color) = //使用when来处理枚举类，直接返回一个"when"表达式
    when(color){
        // 'when' expression must be exhaustive, add necessary 'BLUE' branch or 'else' branch instead
        // 'when'表达式必须是穷尽的，请添加必要的'BLUE'分支或'else'分支
        Color.BLACK -> "black"
        Color.RED -> "red"
        Color.BLUE -> "blue"
    }

fun print(){
    println(getMnemonic(Color.RED))
}

fun getMnemonic2(color: Color) = when(color){ //when表达式里面可以使用任何的对象，非常的宽泛
        Color.BLACK, Color.BLUE -> "black" //两个一起的
        Color.RED -> "red"
    }

//或者你还可以使用不带参的when


interface Expr

class Num(val value: Int): Expr

class Sum(val left: Expr, val right: Expr): Expr

fun eval(e: Expr): Int =
    if (e is Num){
      e.value
    } else if(e is Sum){
      eval(e.left) + eval(e.right)
    } else{
      throw IllegalArgumentException("unKnown")
    }

fun eval2(e: Expr): Int =
    when(e){
        is Num -> e.value
        is Sum -> eval2(e.left) + eval2(e.right)
        else -> throw IllegalArgumentException("unKnown")
    }

//规则：代码块中最后的表达式就是结果

fun xunhuan(boolean: Boolean){
    while (boolean){
        //.....
    }

    do {//至少执行一次
        //......
    }while (boolean)

    val i = 0

    for(i in 1..100){
        println(i)
    }

    for(i in 100 downTo 1 step 2){
        print(i)
    }
}

//迭代map
fun demoMap(){
    val binaryReps = TreeMap<Char, String>()
    for( c in 'A'..'F'){//迭代A到F
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary //存储键值对 访问/更新
    }
    for((letter, binary) in binaryReps){//迭代map
        println("$letter = $binary")
    }

    //list中for的用法
    val list = arrayListOf("10", "11", "100")
    for((index, element) in list.withIndex()){
        println("$index: $element")
    }
}

//使用in 来检查集合和区间的成员
fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNoDigit(c: Char) = c !in '0'..'9'

fun recognize(c: Char) = when(c){
    in '0'..'9' -> "It's a digit"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter"
    else -> "I don't know..."
}

//kotlin 中的异常
//throw\ try...catch...finally 和Java用法一样
fun demoException(num: Int) {
    val percentage =
        if (num in 0..100)
            num
        else throw IllegalArgumentException("A percentage value must be 0..100")
}

//创建集合
val set = hashSetOf(1, 3, 53)
val list = arrayListOf(1, 3, 5)
//list.last()
//list.max()  //其实是扩展函数
val map = hashMapOf(1 to "one", 7 to "seven", 5 to "five") //to 其实是一个开展方法
//println(map.javaClass) //查看它是什么类

//让函数更好调用
//命名参数
fun demoFun(){
    demoException(num = 123) //在调用的时候使用参数名称

}

//带默认参数值的函数声明，这样操作的好处是简化了Java中的重载
fun <T> joinToString(
    Collection: Collection<T>,
    separator: String = ", ", //等号后面的就是默认值
    prefix: String = " ",
    postfix: String = " "
):String {

    return ""
}

//消除静态工具类：顶层函数和属性
/**
 * Java用到的一些工具类，其实这些类不包含任何的状态或实例函数，而是仅仅作为一堆静态函数的容器
 *
 * 可以使用@JvmName 来指定顶层函数生成类的名称
 *
 * @file：JvmName("StringFunction")//注解指定类名
 * package strings //包的声明在文件逐渐之后
 * fun joinToString(...): String {...}
 *
 *
 * 顶层属性
 * 一样类似与静态属性给Java使用
 */

//扩展函数
/**
 * 扩展函数是一个类的成员函数，不过是定义在类的外面。
 * 接收者类型 String
 * 接收者对象 this
 *
 * fun String.lastChar(): Char = this.get(this.length - 1) //实际上，this的也是可以省略的
 * fun String.lastChar(): Char = get(length - 1)
 *
 * println("kotlin".lastChar())
 *
 * //导入
 * import strings.lastChar
 * import strings.*
 * import strings.lastChar as last //as 可以作为导入的重命名
 * val c = "Kotlin".last()
 *
 * //从Java中调用扩展函数
 * Char c = StringUtilKt.lastChar("Java"); //扩展函数所属类为StringUtil.Kt
 *
 * //使用扩展函数作为工具函数
 * joinToString(Collection: Collection<T> ....)
 * 比如上面这个工具函数，就可以修改为Collection<T>的扩展函数，方便使用
 *
 * //不可重写的扩展函数
 * 回想一下，扩展函数在Java中编译为静态函数，及使用方法就知道，扩展函数是不能被重写的。
 *
 *
 * 扩展属性
 * val String.lastChar: Char
 *     get() = get(length -1)
 *
 * //使用
 * println("Kotlin".lastChar)
 *
 * //在Java中使用
 * StringUtilKt.getLastChar("Kotlin");
 *
 */

//通过扩展函数来 扩展Java集合的API

val list2 = listOf(2,3,5)
/**
 * public fun <T> listOf(vararg elements: T): List<T> = if (elements.size > 0) elements.asList() else emptyList()
 * 其实上面这个方法是使用可变参数：让函数支持任意数量的参数
 * vararg 关键字
 *
 */
fun demoVararg(args: Array<String>){
    val list  = listOf("args:", *args) // * 是展开运算符，展开数组内容
    println(list)
}

//键值对的处理：中缀调用和解构声明
val map2 = hashMapOf(1 to "one", 7 to "seven", 5 to "five", 6.to("six"))
//to 是一种特殊的函数调用，被称为中缀调用。  上面两种调用都是可以的
// public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
// 这个就是to函数的声明  解构声明

//分割字符串
//println("12.345-6.A".split("\\.|-".toRegex()))
//[12, 345, 6, A]
//println("12.345-6.A".split(".", "-"))  //如果Java按这个split（"."）来分割是得不到这个结果的

//String的方法 substringBeforeLast() substringAfterLast() 进行分割，挺好用

//如何让你的函数更整洁 //User提供扩展函数
class User(val id: Int, val name: String, val address: String)

fun User.validateBeforeSave(){ //扩展函数
    fun validate(value: String, fieldName: String){ //内部函数
        if(value.isEmpty()){
            throw IllegalArgumentException(
                "Can't save user $id: empty $fieldName"
            )
        }
    }
    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User){
    user.validateBeforeSave() //如此，就很简洁了
    //保存user到数据库
}


