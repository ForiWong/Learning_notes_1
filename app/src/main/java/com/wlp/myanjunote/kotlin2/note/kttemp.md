#1、函数和变量

kotlin鼓励你使用不可变的数据。

val value 不可引用，使用val声明的变量不能在初始化之后再赋值。对应与Java的final 变量
var variable 可变引用

val只能进行唯一一次初始化。

val message :String
if (0==0){
    message = "SUC"
}else{
    message = "FAL"
}
//message = "DEF" //这里是会报错的


val 引用自身是不可变的，但是它指向的对象可能是可变的。

var 关键字允许变量改变自己的值，它的类型是不可变的。

字符串模版

println("Hello, $message")//字符串模版
        
println("Hello, \$message") //对$ 进行转义
        
println("Hello, ${message.length}") //{ } 内部是表达式
        
println("Hello, ${if (message.length > 3) "abc" else "def"}") //{ } 内部是表达式 是可以嵌套 双引号的 "

#2、类和属性
class Person2(
val name:String, //只读属性 getter
var isMarried: Boolean //可写属性 setter / getter
)

//如果有需要，也可以声明自定义的访问器

class Rect(val height: Int, val width: Int){
//    val isSquare: Boolean
//        get() {
//            return height == width
//        }

    val isSquare: Boolean
        get() = height == width //上面的也可以简写
}

//kotlin可以导入类和顶层函数

