package com.wlp.myanjunote.kotlin2.seven7

/**
 *  7章 运算符重载及其他约定
 *
 *  7.1
 *  1、重载算术运算符
 *  point1 + point2 ->  operator plus
 *  或者也可以通过扩展函数来实现
 *  a*b   times
 *  a/b   div
 *  a➗b  mod
 *  a+b  plus
 *  a-b  minus
 *
 * 2、重载复合赋值运算符
 * +=  -=
 *
 * 3、重载一元运算符
 * +a   unaryPlus
 * -a   unaryMinus
 * !a   not
 * ++a,a++  inc
 * --a,a--  dec
 *
 * 7.2 重载比较运算符
 * equals  ==
 *
 * === 恒等运算符 对象是否相同
 *
 * 排序运算符
 * compareTo
 *
 * 7.3集合与区间的约定
 * 1.通过下标来访问原属：get  set
 *
 * 2.in 的约定。用于检查某个对象是否属于集合
 *
 * 3.rangTo的约定。 1..10  从1到10的数字
 *
 * 4.for循环中使用iterator的约定
 *
 * 7.4 建构声明和组件函数
 *
 *
 * 7.5 属性委托
 *
 *
 */
data class Point(val x : Int, val y: Int): Comparable<Point>{
    override fun compareTo(other: Point): Int {
        return compareValuesBy(this, other, Point::x, Point::y)
    }

    operator fun plus(other : Point) : Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun times(scale : Double) : Point {
        return Point((x * scale).toInt(), (y * scale).toInt())
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Point) return false
        return other.x == x && other.y == y
    }

    operator fun get(index : Int) : Int{
        return when(index){
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
        }
    }

}