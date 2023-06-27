package com.wlp.myanjunote.kotlin2.forward3

import java.io.BufferedReader
import java.util.*
import com.wlp.myanjunote.kotlin2.forward3.Color.*

//第二章
fun main(args: Array<String>) {
    val name = if (args.isNotEmpty()) args[0] else "Kotlin"
    println("Hello , $name") //字符串模板

    val person = Person("Chris", 30)
    val person2 = Person("Tun")
    println("${person.name} : ${person.age}")
    //Chris : 30
    println("${person2.name} : ${person2.age}")
    //Tun : null

    for (i in 1..20) {//从1循环到20
        println(fizzBuzz(i))
    }

    for (i in 20 downTo 1 step 2) {//从20到1,步长为2
        print(i)
        print(fizzBuzz(i))
    }

    for (i in 1 until 10) { //不包含结束值 //123456789
        print(i)
    }

    println()

    val binaryReps = TreeMap<String, String>()

    for (c in 1..5) {
        val binary = Integer.toBinaryString(c)
        binaryReps["$c"] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }
    //1 = 1
    //2 = 10
    //3 = 11
    //4 = 100
    //5 = 101

    val list = arrayListOf("10", "100", "111")
    for ((index, element) in list.withIndex()) {
        println("$index : $element")
    }
    //0 : 10
    //1 : 100
    //2 : 111

    var number = 11
    val percentage =
        if (number in 0..100) number else throw IllegalArgumentException("A percentage value must be between")

    max(1, 100)

    println(Rectangle(2, 3).isSquare)

    println(getMnemonic(BLUE))

    try {
        println(mix(BLUE, RED))
        println(mixOptimized(BLUE, RED))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    println(eval(Num(12)))
    println(eval(Sum(12, 13)))

    println(isLetter('B'))
    println(isNotDigit('B'))
}

/**
 * 表达式是有值的，意味者可以省略掉return,代码块的最后一个表达式会被作为结果返回
 * 表达式体的函数的返回值类型可以省略，但是有代码块的则不可以
 */
fun max(a: Int, b: Int): Int = if (a > b) a else b //直接返回表达式，称作表达式体

/**
 * 自定义访问器
 * 大多数情况下，属性会有一个对应的支持字段来保存属性的值，但如果这个值可以及时计算，可以自定义getter
 */
class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        get() { //自定义访问器
            return height == width
        }
}

/**
 * 可以有代码块，代码块中最后的表达式就是结果(对常规函数不成立)
 */
fun getMnemonic(color: Color): String = when (color) { //when允许使用任意对象
    RED -> {
        println("Richard is Beautiful")
        "Richard"
    }
    ORANGE -> "Of"
    YELLOW -> "York"
    GREEN -> "Gave"
    BLUE -> "Battle"
    INDIGO, VIOLET -> "In"
}

/**
 * when传递的参数可以是任意对象
 */
fun mix(c1: Color, c2: Color) = when (setOf(c1, c2)) {
    setOf(RED, YELLOW) -> ORANGE
    setOf(YELLOW, BLUE) -> GREEN
    setOf(BLUE, VIOLET) -> INDIGO
    else -> throw  Exception("Dirty color") //如果没有任何其他分支匹配则执行该行
}

/**
 * 省略参数的when语句必须添加else语句，分支条件为任意的布尔表达式
 * 优点不会创建额外的对象，缺点是更难理解
 */
fun mixOptimized(c1: Color, c2: Color) = when {
    (c1 == RED && c2 == YELLOW) ||
            (c1 == YELLOW && c2 == RED) -> ORANGE
    (c1 == YELLOW && c2 == BLUE) ||
            (c1 == BLUE && c2 == YELLOW) -> GREEN
    (c1 == BLUE && c2 == VIOLET) ||
            (c1 == VIOLET && c2 == BLUE) -> INDIGO
    else -> throw Exception("Dirty Color")
}

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Int, val right: Int) : Expr

/**
 * 智能转换：IDE会使用背景色高亮显示智能转换
 * 可以使用as关键字来显示转换œ
 * Type checking has run into a recursive problem. Easiest workaround:
 * specify types of your declarations explicitly
 */
fun eval(e: Expr) = when {
    (e is Num) -> e.value
    (e is Sum) -> e.left + e.right
    else -> throw IllegalArgumentException("Unknown expression")
}

//when检查实参值得类型
//fun eval(e : Expr) : Int = when(e){
//    is Num -> e.value
//    is Sum -> eval(e.right) + eval(e.left)
//    else -> throw IllegalArgumentException("Unknown expressions")
//}

fun fizzBuzz(i: Int) = when {
    i % 3 == 0 -> "FizzBuzz"
    i % 4 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}

//判断是否（不）在该区间
fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'

fun readNumber(reader: BufferedReader) {
    val number = try { //try catch作为表达式
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        return
    }
    println(number)
}
