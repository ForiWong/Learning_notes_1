package com.wlp.myanjunote.kotlin2.delegateby

fun main() {
    /**
     * kotlin 委托, Kotlin 通过关键字 by 实现委托。
     * (1)在 Derived 声明中，by 子句表示，将 b 保存在 Derived 的对象实例内部，而且编译器将会生成继承自 Base 接口
     * 的所有方法, 并将调用转发给 b。
     *
     * 接口Base
     * 类BaseImpl 实现接口 Base
     * //通过关键字 by 建立委托类
     * class ByClassExample(b: Base) : Base by b
     *
     * 最后Derived对象的方法实现为BaseImpl的实现
     */
    val b = BaseImpl(10)
    ByClassExample(b).print() // 输出 DaYing:10

    /**
     * (2)属性委托指的是一个类的某个属性值不是在类中直接进行定义，而是将其托付给一个代理类，从而实现对该类的属性统一管理。
     * 属性委托语法格式：
     * val/var <属性名>: <类型> by <表达式>
     *
     * by 关键字之后的表达式就是委托, 属性的 get() 方法(以及set() 方法)将被委托给这个对象的 getValue() 和 setValue()
     * 方法。属性委托不必实现任何接口, 但必须提供 getValue() 函数(对于 var属性,还需要 setValue() 函数)。
     */
    val e = ByVariateExample()
    println(e.p)     // 访问该属性，调用 getValue() 函数
    //com.wlp.myanjunote.kotlin2.delegateby.ByVariateExample@58e1d9d, 这里委托了 p 属性 + a: defg
    //defg
    e.p = "abc" //访问setValue()方法
    //com.wlp.myanjunote.kotlin2.delegateby.ByVariateExample@58e1d9d 的 p 属性赋值为 abc

    /**
     * (3)延迟属性 Lazy
     * lazy() 是一个函数, 接受一个 Lambda 表达式作为参数, 返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延迟属性的委托：
     * 第一次调用 get() 会执行已传递给 lazy() 的 lamda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
     */
    var byLazyExample = ByLazyExample()
    println(byLazyExample.lazyValue)   // 第一次执行，执行两次输出表达式
    println(byLazyExample.lazyValue)   // 第二次执行，只输出返回值
    //computed!
    //Hello
    //Hello

    /**
     * (4)可观察属性 Observable
     * observable 可以用于实现观察者模式。
     * Delegates.observable() 函数接受两个参数: 第一个是初始化值, 第二个是属性值变化事件的响应器(handler)。
     * 在属性赋值后会执行事件的响应器(handler)，它有三个参数：被赋值的属性、旧值和新值：
     */
    val user = User()
    user.myName = "第一次赋值"
    user.myName = "第二次赋值"
    //prop: myName, 旧值：初始值 -> 新值：第一次赋值
    //prop: myName, 旧值：第一次赋值 -> 新值：第二次赋值

    /**
     * (5)把属性储存在映射中
     * 一个常见的用例是在一个映射（map）里存储属性的值。 这经常出现在像解析 JSON 或者做其他"动态"事情的应用中。
     * 在这种情况下，你可以使用映射实例自身作为委托来实现委托属性。
     *
     */
    // 构造函数接受一个映射参数
    val site = Site(
        mapOf(
            "name" to "菜鸟教程",
            "url" to "www.runoob.com"
        )
    )
    // 读取映射值
    println(site.name)
    println(site.url)
    //菜鸟教程
    //www.runoob.com
}
