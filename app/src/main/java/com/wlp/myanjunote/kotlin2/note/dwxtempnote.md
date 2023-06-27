kotlin jetpack

var view: View //这里会报错，需要初始化
//Java的字段field在Kotlin里面被隐藏了，取而代之的是属性property.

lateinit var view : View  //lateinit 我现在声明的时候没法初始化，但是使用之前一定会初始化的

var name:String? = null  //这样就没有非空的限制了

//这样就是可空变量了

//但是使用时需要这样，否者会报错
println(name?.length)//这样就可以保证非空了，并且是线程安全的

name!!.length

变量需要手动初始化，所以不初始化会报错
变量默认非空，所以初始化时赋值为null也报错
变量用？号设置为可空，使用的时候又报错

变量声明的时候，不声明类型
叫类型推断
val 只读变量 value
var 普通变量 variable

kotlin 函数返回为空时，用Unit

var name: String? = null
get(){
log(”name got”)
return field
}
set(){
field = value
log(“new name set”)
}





kt类的写法
class User{
var name

constructor(name: String){//构造函数
this.name = name
}

init{//初始化代码块
println(“INitializing”)
}
}

//final
val

kt中的常量
object
companion object

object//替代class，并创建一个对象

object Kaixue{
val site = “kaixue”
fun printSite() = println(site)
}

val siteName = Kailua.site //其实这就是单例例。

顶层声明//top-level
//比如，工具类的使用

companion object与顶层声明

const
//编译期常量

MutableList
List 


