package com.wlp.myanjunote.kotlin2.eight8

import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

/**
 * 高阶函数：Lambda作为参数和返回值的函数
 * 简化代码，去除重复代码。
 * 内联函数——消除lambda带来的性能开销，使得lambda内的控制流更加灵活。
 * 标准库中的filter函数将一个判断式函数作为参数
 * map, with 等等
 */
fun main(args : Array<String>){
    //把lambda表达式保存在局部变量中
    val sum = {x : Int, y : Int -> x + y}
    //val sum:(Int, Int) -> Int = (x, y -> x + y)   //有两个Int参数和Int返回值的函数
    val action = { println(42)}
    //val action: () -> Unit = {println(42)} //没有参数和返回值的函数

    /**
     * 声明函数类型：（函数参数类型） -> Unit（返回类型）
     * Unit表示函数不返回任何值，Unit类型的返回值是可以省略的
     * 但是在函数类型声明中，需要一个显示的返回类型，这个场景下Unit是不能省略的的
     */
    //下面的代码是编译器推导出的
    val sumFull : (Int, Int) -> Int = {x , y -> x + y}
    //函数类型的返回值可以为空
    val canReturnNull : (Int, Int) -> Int? = {x, y -> null}
    //函数变量本身可以为空
    val funOrNull : ((Int, Int) -> Int)? = null

    val str: String = "abc"
    println(str.filter { it -> it != 'b' }) //ac  //fun String.filter(predicate : (Char) -> Boolean) : String

    //kt
    fun processTheAnswer(f: (Int) -> Int){
        println(f(42))
    }

    //java
    //processTheAnswer { number -> number + 1 }

    val letters = listOf("Alpha", "Beta")
    println(letters.joinToString())

    //高阶函数是一个改进代码结构和减少重复代码的利器
    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)//将返回的函数保存在变量中
    println("Shipping costs ${calculator(Order(3))}")//调用返回的函数

    val log = listOf(
            SiteVisit("/", 34.0, OS.WINDOWS),
            SiteVisit("/", 22.0, OS.MAC),
            SiteVisit("/", 12.0, OS.WINDOWS),
            SiteVisit("/", 8.0, OS.IOS),
            SiteVisit("/", 16.3, OS.ANDROID)
    )

    val averageWindowsDuration = log.filter { it.os in setOf(OS.ANDROID, OS.IOS) }
            .map { it.duration }.average()
    println(averageWindowsDuration)

    println(log.getAverageDuration { it.os in setOf(OS.IOS, OS.ANDROID) })

    twoAndThree{a,b -> a + b}   //The result is 5
    twoAndThree(sum)            //The result is 5
    twoAndThree{a,b -> a * b}   //The result is 6

//    println("before sync")
//    sync(l){
//        println("action")
//    }
//    println("after sync")
    val list = listOf("cde", "abc", "dd")
    lookForAlice(list)
    lookForAlice2(list)
}

fun twoAndThree(operation: (Int, Int) -> Int){
    val result = operation(2, 3)
    println("The result is $result")
}

/**
 * 内联函数：消除lambda带来的运行时开销
 * lambda表达式会被正常地编译成匿名类，这表示每调用一次lambda表达式，一个额外的类就会被创建。
 * 现在使用inline修饰符标记函数。
 * 它的函数体是内联的，函数体会被直接替换到函数被调用的地方。
 * （1）内联函数的限制
 * 如果lambda的参数被调用，这样的代码能被容易地内联。
 * 如果参数在某个地方被保存起来，以便后后继续使用，lambda表达式的代码不能被内联。
 *
 * （2）内联集合操作
 * 一般都是内联的
 * 还有序列替代的集合的操作，都是可以减小开销的
 *
 * （3）什么时候才考虑使用内联呢？
 * 使用inline只能提高带有lambda参数的函数的性能，其他情况需要考虑。
 *
 * JVM支持强大的内联
 * 在字节码在转化成机器码时，自动完成是否要内联。
 * 在使用inline时，应该注意代码的长度，尽量减少拷贝的开销。
 *
 * （4）使用内联lambda管理资源
 * lambda可以去除重复代码的一个常见模式是资源管理：先获取一个资源，完成一个操作，然后释放资源。
 * 比如，一个文件、一个锁、一个数据库事物等。
 *
 * lock.withLock(action)
 *
 * public inline fun <T> Lock.withLock(action: () -> T): T { ... }
 *
 */
inline fun<T> sync(lock: Lock, action:() -> T) : T{
    lock.withLock(action)
    lock.lock();
    try {
        return action()
    }finally {
        lock.unlock()
    }
}


/**
 * 高阶函数的控制流
 * （1）lambda中的返回语句
 * 非局部返回
 *
 * （2）使用标签返回
 * 局部返回
 *
 *
 **/
fun lookForAlice(people: List<String>){
    people.forEach {
        println(it)
        if(it == "abc") {
            println("dui")
            return@forEach //这个会遍历完所有
        }
    }
    println("end")
}

fun lookForAlice2(people: List<String>){
    people.forEach {
        println(it)
        if(it == "abc") {
            println("dui")
            return //直接返回
        }
    }
    println("end")
}


/**
kotlin语法复习--->局部返回,inline,noinline和crossinline

----inline关键字----

inline关键字是作用:

在函数上的一个关键字，其作用就是在class文件中，kotlin的函数体代码会直接放在调用的位置

另外就是，当函数类型参数含有函数类型参数时，添加inline关键字会提高程序的性能，但是，该函数参数不能作用一般的参数进行参数调用。

fun test() {
    hasInlineFunc {
        println("hasInlineFunc")
    }
    noInlineFunc {
        println("noInlineFunc")
    }
}

inline fun hasInlineFunc(action: () -> Unit) {
    action()
}

fun noInlineFunc(action: () -> Unit) {
    action()
}

相应的字节码文件:
public static final void test() {
    // hasInlineFunc: 函数体内容
    String var2 = "hasInlineFunc";
    System.out.println(var2);
    // noInlineFunc: 函数调用
    noInlineFunc((Function0)null.INSTANCE);
}

public static final void hasInlineFunc(@NotNull Function0 action) {
    Intrinsics.checkNotNullParameter(action, "action");
    action.invoke();
}

public static final void noInlineFunc(@NotNull Function0 action) {
    Intrinsics.checkNotNullParameter(action, "action");
    action.invoke();
}

接下来就是解释上面第二句的意思,由于inline修饰函数会直接将函数体内容放在相应调用的位置，所以其函数类型参数就不能作为一个引用来进行使用。

fun func(action: () -> Unit) {

}

inline fun hasInlineFunc(action: () -> Unit) {
    action()
    func(action) // 在这里会出现编译错误(可以在参数action前面添加noinline,这个在后面说)
}

----noline----
上面也已经说了，inline修饰的函数，其函数类型参数不能够作用一般的参数进行调用，那么noinline关键字就是解决这个问题。还是接着上面的代码案例

fun func(action: () -> Unit) {

}

inline fun hasInlineFunc(action: () -> Unit) {
    action()
    func(action) // 在这里会出现编译错误(可以在参数action前面添加noinline,这个在后面说)
}

// 解决方式一: 添加 noinline 关键字
inline fun hasInlineFunc(noinline action: () -> Unit) { // 这里会出现黄色的警告(可以在添加一个没有noinline关键字修饰的函数类型参数)
    action()
    func(action)
}

// 解决方式二: 删除inline 关键字
fun hasInlineFunc(action: () -> Unit) {
    action()
    func(action)
}

----crossinline----

再介绍crossinline之前需要提一下局部返回和非局部返回。

普通函数，没有inline，局部返回return@，直接结束。
inline修饰的函数可以进行非局部返回(也就是可以使用return)，还是会继续遍历。
局部返回:

fun test(): Int{
    func {
        if (it == 1) {
            return@func // 局部返回: 只是当前的函数体结束，该函数体作用域是在(func函数中的)
            // return 1 非局部返回,test方法直接结束，并返回1
        }
        println("action$it 执行中")
    }
    return 1
}

fun func(action: (Int) -> Unit) {
    println("func 开始执行")
    for (i in 0..2) {
        println("action$i 开始执行")
        action(i)
        println("action$i 执行结束")
    }
    println("func 执行结束")
}

// 输出结果
func 开始执行
action0 开始执行
action0 执行中
action0 执行结束
// action1未执行,因为存在局部返回
action1 开始执行
action1 执行结束

action2 开始执行
action2 执行中
action2 执行结束

func 执行结束

inline修饰的函数可以进行非局部返回(也就是可以使用return),没有inline修饰的函数只能进行局部返回(return@局部函数域名)

inline fun hasInlineFunc(action: () -> Unit) {
    // 添不添加crossinline都一样，添加后可以提示我们，action的lambda中不要出现return，因为该函数有可能会在其他的lambda中进行调用
    action()
}

fun noInlineFunc(action: () -> Unit) {
    action()
}

fun test(): Int{
// 可以进行函数域自定义命名: 名字@
    noInlineFunc func1@{ // 不能进行非局部返回
    println("noInlineFunc")
// 但是hasInlineFunc函数是inline函数,可以进行非局部返回,也就是return
// 在下面使用return还是会出错, 这里就会出现矛盾，所以为了给我们程序员提示，在hasInlineFunc的参数action前面添加crossinline就是告诉我们action的lambda中不能出现return语句，只能进行局部返回
    hasInlineFunc func2@{
// return 2 还是编译出错
}
// return 2 编译出错
}
return 1
}
 */