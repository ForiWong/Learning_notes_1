package com.wlp.myanjunote.kotlin2.seven7

import java.math.BigDecimal

/**
 *  Function:
 *  <br/>
 *  Describe:
 *  <br/>
 *  Author: chris on 2019/3/21.
 *  <br/>
 *  Email: chrissen0814@gmail.com
 */

fun main(args : Array<String>){
    val point1 = Point(10, 20)
    val point2 = Point(20, 30)
    println(point1 + point2)
    println(point1.plus(point2))

    println(point1 * 1.5)

    var point11 = Point(1,2)
    point11 += Point(3,4)
    println(point11)

    val numbers = ArrayList<Int>()
    numbers += 43
    println(numbers[0].toString() + ":" + numbers.size)

    val list = arrayListOf(1,2)
    list += 3
    println(list)
    val newList = list + listOf(3,4)
    println(newList)

    var bd = BigDecimal.ZERO
    println(bd++)
    println(++bd)

    val person1 = Person("Alice", "Smith")
    val person2 = Person("Bob", "Johnson")
    println(person1 < person2)

    val point = Point(10, 20)
    println(point[0])

    var point3 = MutablePoint(200, 300)
    point3[0] = 300
    println("${point1[0]}")

    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rect)

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val now = LocalDate.now()
//        val vacations = now..now.plusDays(10)//今天开始的10天区间
//        println(now.plusWeeks(1) in vacations)
//    } else {
//        throw IllegalArgumentException("The version is lower than O")
//    }

    val p = Point(10, 20)
    val (x, y) = p
    println(x)

    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")

    printEntries(map)

}