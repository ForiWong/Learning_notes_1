package com.wlp.myanjunote.kotlin.base

/**
 * Created by wlp on 2023/3/23
 * Description:

## 01 | Kotlin 基础语法
    基础语法，包括变量、基础类型、函数和流程控制。
    Kotlin 和 Java 的基础语法是比较相似的，它们都是基于 JVM 的语言。
    但其实不然，Kotlin 作为一门新的语言，它包含了许多新的特性，由此也决定着 Kotlin 的代码风格。
    目前 Java 虚拟机已经可以支持很多除 Java 语言以外的语言了，如 Kotlin、 Groovy、JRuby、Jython、Scala 等。
     之所以可以支持，就是因为这些语言也可以被编译 成 字 节 码 。

Kotlin 在这方面都做了哪些改进：
    支持类型推导；
    代码末尾不需要分号；
    字符串模板；
    原始字符串，支持复杂文本格式；
    单一表达式函数，简洁且符合直觉；
    函数参数支持默认值，替代 Builder 模式的同时，可读性还很强；
    if 和 when 可以作为表达式。

    强制区分“可为空变量类型”和“不可为空变量类型”，规避空指针异常；
    推崇不可变性（val），对于没有修改需求的变量，IDE 会智能提示开发者将“var”改为“val”；
    基础类型不支持隐式类型转换，这能避免很多隐藏的问题；
    数组访问行为与集合统一，不会出现 array.length、list.size 这种恼人的情况；
    函数调用支持命名参数，提高可读性，在后续维护代码的时候不易出错；
    when 表达式，强制要求逻辑分支完整，让你写出来的逻辑永远不会有漏洞。


todo 虽然 Kotlin 在语法层面摒弃了“原始类型”，但有时候为了性能考虑，我们确实需要用“原始类型”。这时候我们应该怎么办？
只要基础类型的变量可能为空，那么这个变量就会被转换成 Java 的包装类型。
反之，只要基础类型的变量不可能为空，那么这个变量就会被转换成 Java 的原始类型

 */
class Grammar01 {
    /**
     * 变量
     * var val
     * val 声明的变量，我们叫做不可变变量，它的值在初始化以后就无法再次被修改，它相当于 Java 里面的 final 变量。
     * var 声明的变量，我们叫做可变变量，它对应 Java 里的普通变量。
     **/
    /*
    关键字     变量类型
     ↓          ↓         */
    var price: Int = 100;   /*
     ↑            ↑
   变量名        变量值   */
    var price2: Int = 100 //省略分号
    var price3      = 100 //支持类型推导

    /**
     * 基础类型
     * 在kotlin中，一切都是对象，安全面向对象的。
     *
     * 在 Java 里面，基础类型分为原始类型（Primitive Types）和包装类型（Wrapper Type）。
     * 比如，整型会有对应的 int 和 Integer，前者是原始类型，后者是包装类型。
     *
     * Type     Bit width
     * Double   64
     * Float    32
     * Long     64
     * Int      32
     * Short    16
     * Char     16
     * Byte     8
     * Boolean  8
     *
     **/
    val temp = 1
    val dd : Double = temp.toDouble()//显示转换类型方法

    /**
     * 空安全
     * 一切皆对象，那么对象就有可能为空
     * 默认是不可空的
     * 可空类型，需要加 ？
     */
    //val empty: Double = null // 编译器报错
    val empty: Double? = null // 编译通过

    fun test1(){
        var m: Double = 1.0
        val n: Double? = null
        if (n != null) {
            m = n  // 加个判断，也是可以编译通过
        }
    }

    /**
     * 数字类型
     *
     * 整数默认会被推导为“Int”类型；
     * Long 类型，我们则需要使用“L”后缀；
     * 小数默认会被推导为“Double”，我们不需要使用“D”后缀；
     * Float 类型，我们需要使用“F”后缀；
     * 使用“0x”，来代表十六进制字面量；
     * 使用“0b”，来代表二进制字面量。
     *
     * 但是，对于数字类型的转换，Kotlin 与 Java 的转换行为是不一样的。
     * Java 可以隐式转换数字类型，而 Kotlin 更推崇显式转换。
     */
    val int = 1
    val long = 1234567L
    val double = 13.14
    val float = 13.14F
    val hexadecimal = 0xAF
    val binary = 0b01010101

    val i = 100
    val j: Long = i.toLong() // 显示转换，编译通过

    /**
     * 布尔类型
     *
     * 我们再来了解下 Kotlin 中布尔类型的变量，它只有两种值，分别是 true和false。布尔类型支持一些逻辑操作，比如说：
     * “&”代表“与运算”；
     * “|”代表“或运算”；
     * “!”代表“非运算”；
     * “&&”和“||”分别代表它们对应的“短路逻辑运算”。
     *
     * */
    val i1 = 1
    val j1 = 2
    val k1 = 3
    val isTrue: Boolean = i1 < j1 && j1 < k1

    /**
     * 字符：CharChar 用于代表单个的字符，比如'A'、'B'、'C'，字符应该用单引号括起来。
     *
     */
    val char: Char = 'A'
    //val ii: Int = char // 编译器报错
    val ii = char.toInt() //编译通过

    /**
     * 字符串：String
     *
     * $ 字符串模板
     * */
    val s = "Hello Kotlin!"

    fun test2() {
        val name = "Kotlin"
        print("Hello $name!")//Hello Kotlin !
        /*            ↑
        直接在字符串中访问变量
        */

        val array = arrayOf("Java", "Kotlin")
        print("Hello ${array.get(1)}!")
        /*            ↑
          复杂的变量，使用${}
        */

        //Kotlin 还新增了一个原始字符串，是用三个引号来表示的。
        val s = """
           当我们的字符串有复杂的格式时
           原始字符串非常的方便
           因为它可以做到所见即所得。 """
        print(s)
    }

    //数组
    val arrayInt = arrayOf(1, 2, 3)
    val arrayString = arrayOf("apple", "pear")

    //虽然 Kotlin 的数组仍然不属于集合，但它的一些操作是跟集合统一的。
    fun test3(){
        val array = arrayOf("apple", "pear")
        println("Size is ${array.size}")
        println("First element is ${array[0]}")
    }

    /**
     * 函数声明
     *
     * 使用了 fun 关键字来定义函数；
     * 函数名称，使用的是驼峰命名法（大部分情况下）；
     * 函数参数，是以 (name: String) 这样的形式传递的，这代表了参数类型为 String 类型；
     * 返回值类型，紧跟在参数的后面；
     * 最后是花括号内的函数体，它代表了整个函数的逻辑。
     */
    /*
    关键字    函数名          参数类型   返回值类型
     ↓        ↓                ↓       ↓      */
    fun helloFunction(name: String): String {
        return "Hello $name !"
    }/*   ↑
     花括号内为：函数体
    */

    // 它的函数体实际上只有一行代码。那么针对这种情况，我们其实就可以省略函数体的花括号，直接使用“=”
    // 来连接，将其变成一种类似变量赋值的函数形式：
    // 简化
    fun helloFunction2(name: String): String = "Hello $name !"
    // 再简化
    fun helloFunction3(name: String) = "Hello $name !"

    fun test4(){
        //函数调用
        helloFunction3("123")
        helloFunction3( name = "123")//命名参数

        //这个用起来就很方便了。如果是Java还得使用 Builder 设计模式
        createUser("abc", 23)
        createUser(age = 11, name = "abc")
    }

    //还支持参数默认值
    fun createUser(
        name: String,
        age: Int,
        gender: Int = 1,
        friendCount: Int = 0,
        feedCount: Int = 0,
        likeCount: Long = 0L,
        commentCount: Int = 0
    ) {
        //..
    }

    /**
     * 流程控制
     * 在 Kotlin 当中，流程控制主要有 if、when、for、 while，这些语句可以控制代码的执行流程。
     *
     *
     * */
    fun test5(){
        val i = 1
        if (i > 0) {
            print("Big")
        } else {
            print("Small")
        }
        //输出结果：Big

        val i2 = 1
        val message = if (i2 > 0) "Big" else "Small" //作为表达式使用
        print(message)


        val i3: Int = 1
        when(i3) {
            1 -> print("一")
            2 -> print("二")
            else -> print("i 不是一也不是二")
        }
        //输出结果：一


        val i4: Int = 1
        val message4 = when(i4) {
            1 -> "一"
            2 -> "二"
            else -> "i 不是一也不是二" // 如果去掉这行，会报错
        }
        print(message4)


        var i5 = 0
        while (i5 <= 2) {
            println(i5)
            i5++
        }

        var j = 0
        do {
            println(j)
            j++
        } while (j <= 2)

        val array = arrayOf(1, 2, 3)
        for (i in array) {
            println(i)
        }

        //“..”来连接数值区间的两端
        val oneToThree = 1..3 // 代表 [1, 3]

        for (i in oneToThree) {
            println(i)
        }

        //从 6 到 0，每次迭代的步长是 2
        for (i in 6 downTo 0 step 2) {
            println(i)
        }


    }

    fun getLength(text: String?): Int {
        return if (text != null) text.length else 0
    }

    fun getLength2(text: String?): Int {
        return text?.length ?: 0 //Elvis 表达式。
    }
}
