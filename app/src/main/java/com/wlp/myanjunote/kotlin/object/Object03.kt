package com.wlp.myanjunote.kotlin.`object`

/**
object 关键字，却有三种迥然不同的语义，分别可以定义：
匿名内部类；
单例模式；
伴生对象。

之所以会出现这样的情况，是因为 Kotlin 的设计者认为，这三种语义本质上都是在定义一个类的同时还创建了
对象。在这样的情况下，与其分别定义三种不同的关键字，还不如将它们统一成 object 关键字。
 */

interface A {
    fun funA()
}

interface B {
    fun funB()
}

abstract class Man {
    abstract fun findMan()
}

/**
object 定义匿名内部类
（1）当 Kotlin 的匿名内部类只有一个需要实现的方法时，我们可以使用 SAM 转换，最终使用
Lambda 表达式来简化它的写法。

（2）Java 和 Kotlin 相同的地方就在于，它们的接口与抽象类，都不能直接创建实例。想要创建接口
和抽象类的实例，我们必须通过匿名内部类的方式。
在 Kotlin 中，匿名内部类还有一个特殊之处，就是我们在使用 object 定义匿名内部类的时候，其实还
可以在继承一个抽象类的同时，来实现多个接口。

 * */
fun main() {
    // 这个匿名内部类，在继承了Man类的同时，还实现了A、B两个接口
    val item = object : Man(), A, B{
        override fun funA() {
            // do something
        }
        override fun funB() {
            // do something
        }
        override fun findMan() {
            // do something
        }
    }
}

/**
object： 单例模式
在 Kotlin 当中，要实现单例模式其实非常简单，我们直接用 object 修饰类即可。

可以看到，当我们使用 object 关键字定义单例类的时候，Kotlin 编译器会将其转换成静态代码块的单例模式。
因为 static{}代码块当中的代码，由虚拟机保证它只会被执行一次，因此，它在保证了线程安全的前提下，同
时也保证我们的 INSTANCE 只会被初始化一次。

这种方式定义的单例模式，虽然具有简洁的优点，但同时也存在两个缺点：
（1）不支持懒加载。
（2）不支持传参构造单例。
 * */
object UserManager {
    fun login() {}
}



class Person {
    object InnerSingleton {
        @JvmStatic//用于声明静态的方法或属性。 在Java 和 kotlin中调用的方式是一样的
        fun foo() {}
    }
}
/**
object： 伴生对象
Kotlin 当中没有 static 关键字，所以我们没有办法直接定义静态方法和静态变量。不过，Kotlin 还是为我们提
供了伴生对象，来帮助实现静态方法和变量

 **/

class Person2 {
    //  改动在这里
    //     ↓
    companion object InnerSingleton {
        @JvmStatic
        fun foo() {}
    }
}

//  工厂模式
//  私有的构造函数，外部无法调用
//            ↓
class User private constructor(name: String) {
    companion object {
        @JvmStatic
        fun create(name: String): User {
            // 统一检查，比如敏感词过滤
            return User(name)
        }
    }
}

/**
 * Kotlin 里功能更加全面的 4 种单例模式，分别是
 * 懒加载委托单例模式、Double Check 单例模式、抽象类模板单例，以及接口单例模板。
 *
 * （1）在它内部的属性上使用 by lazy 将其包裹起来，这样我们的单例就能得到一部分的懒加载效果。
 * */
object UserManager2 {
    // 对外暴露的 user
    val user by lazy { loadUser() }

    private fun loadUser(): User {
        // 从网络或者数据库加载数据
        return User.create("tom")
    }

    fun login() {}
}

/**
 * （2）伴生对象 Double Check
 *
 **/
class UserManager3 private constructor(name: String) {
    companion object {
        @Volatile private var INSTANCE: UserManager3? = null

        fun getInstance(name: String): UserManager3 =//我们就实现了整个 UserManager 的懒加载，相比于上面的 by lazy
            // 第一次判空
            INSTANCE?: synchronized(this) {
                // 第二次判空
                INSTANCE?:UserManager3(name).also { INSTANCE = it }
            }
    }
}

class Object03 {

    fun test(){
        UserManager.login()

        Person.InnerSingleton.foo()
        Person2.foo()//看到没，可以直接调用foo()，像静态方法一样

        // 使用
        UserManager3.getInstance("Tom")

        PersonManager.getInstance("abc")

    }
}


//注释①：abstract 关键字，代表了我们定义的 BaseSingleton 是一个抽象类。我们以后要实现单例类，就只需要继承这个 BaseSingleton 即可。
// 注释②：in P, out T 是 Kotlin 当中的泛型，P 和 T 分别代表了 getInstance() 的参数类型和返回值类型。注意，这里的 P 和 T，是在具体的单例子类当中才需要去实现的。
//  ①                          ②
//  ↓                           ↓
abstract class BaseSingleton<in P, out T> {
    @Volatile
    private var instance: T? = null

    //注释③：creator(param: P): T 是 instance 构造器，它是一个抽象方法，需要我们在具体的单例子类当中实现此方法。
    //                       ③
    //                       ↓
    protected abstract fun creator(param: P): T

    //注释④：creator(param) 是对 instance 构造器的调用。
    fun getInstance(param: P): T =
        instance ?: synchronized(this) {
            //            ④
            //            ↓
            instance ?: creator(param).also { instance = it }
        }
}

//改成高阶函数
abstract class BaseSingleton2<in P, out T> {
    @Volatile
    private var instance: T? = null
    //               变化在这里，函数类型的属性
    //                  ↓              ↓
    protected abstract val creator: (P)-> T

    fun getInstance(param: P): T =
        instance ?: synchronized(this) {
            instance ?: creator(param).also { instance = it }
        }
}

class PersonManager private constructor(name: String) {
    //               ①                  ②
    //               ↓                   ↓
    companion object : BaseSingleton<String, PersonManager>() {
        //                  ③
        //                  ↓
        override fun creator(param: String): PersonManager = PersonManager(param)
    }
}

//改成高阶函数方式
class PersonManager2 private constructor(name: String) {
    companion object : BaseSingleton2<String, PersonManager2>() {
        //                             函数引用，引用了构造函数
        //                                ↓
        override val creator = ::PersonManager2
    }
}


class UserManager4 private constructor(name: String) {
    companion object : BaseSingleton<String, UserManager4>() {
        override fun creator(param: String): UserManager4 = UserManager4(param)
    }
}

//这种方式是不被推荐的，
interface ISingleton<P, T> {
    // ①
    var instance: T?

    fun creator(param: P): T

    fun getInstance(p: P): T =
        instance ?: synchronized(this) {
            instance ?: creator(p).also { instance = it }
        }
}
