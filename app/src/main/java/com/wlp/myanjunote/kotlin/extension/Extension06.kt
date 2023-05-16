package com.wlp.myanjunote.kotlin.extension

import android.view.View
import android.view.ViewGroup

/**
 * Created by wlp on 2023/3/27
 * Description:
1、Kotlin 的扩展（Extension）
主要分为两种语法：第一个是扩展函数，第二个是扩展属性。
扩展函数，就是从类的外部扩展出来的一个函数，这个函数看起来就像是类的成员函数一样。

Kotlin 编写的扩展函数调用代码，最终会变成静态方法的调用。

Kotlin 的扩展表面上看起来是为一个类扩展了新的成员，但是本质上，它还是静态方法。
而且，不管是扩展函数还是扩展属性，它本质上都会变成一个静态的方法。
那么，到底什么时候该用扩展函数，什么时候该用扩展属性呢？
其实，我们只需要看扩展在语义上更适合作为函数还是属性就够了。

2、扩展能做什么？
当我们想要从外部为一个类扩展一些方法和属性的时候，我们就可以通过扩展来实现了。在 Kotlin 当中，几乎
所有的类都可以被扩展，包括普通类、单例类、密封类、枚举类、伴生对象，甚至还包括第三方提供的 Java 类。
唯有匿名内部类，由于它本身不存在名称，我们无法指定“接收者类型”，所以不能被扩展，当然了，它也没必
要被扩展。

可以说，Kotlin 扩展的应用范围还是非常广的。它最主要的用途，就是用来取代 Java 当中的各种工具类，比
如 StringUtils、DateUtils 等等。

3、扩展不能做什么？
第一个限制，Kotlin 扩展不是真正的类成员，因此它无法被它的子类重写。
第二个限制，扩展属性无法存储状态。就如前面代码当中的 isAdult 属性一般，它的值是由 age 这个成员属性
决定的，它本身没有状态，也无法存储状态。
第三个限制，扩展的访问作用域仅限于两个地方。第一，定义处的成员；第二，接收者类型的公开成员。

所以，针对扩展的第三个限制来说：如果扩展是顶层的扩展，那么扩展的访问域仅限于该 Kotlin 文件当中的所有成员，
以及被扩展类型的公开成员，这种方式定义的扩展是可以被全局使用的。
如果扩展是被定义在某个类当中的，那么该扩展的访问域仅限于该类当中的所有成员，以及被扩展类型的公开成员，
这种方式定义的扩展仅能在该类当中使用。

4、Kotlin 扩展主要有两个核心使用场景。
主动使用扩展，通过它来优化软件架构。对复杂的类进行职责划分，关注点分离。让类的核心尽量简单易懂，
而让类的功能性属性与方法以扩展的形式存在于类的外部。比如我们的String.kt与Strings.kt。

被动使用扩展，提升可读性与开发效率。当我们无法修改外部的 SDK 时，对于重复的代码模式，我们将其以扩展
的方式封装起来，提供给对应的接收者类型，比如 view.updateMargin()。

 */

/**
 顶层扩展
 **/
fun String.lastElement(): Char? { //String 接收者类型
    //    ⑤
    //    ↓
    if (this.isEmpty()) {//this
        return null
    }

    return this[length - 1]
}

// 使用扩展函数
fun main() {
    val msg = "Hello Wolrd"
    // lastElement就像String的成员方法一样可以直接调用
    val last = msg.lastElement() // last = d

    // lastElement就像String的成员属性一样可以直接调用
    val last2 = msg.lastElement // last = d
}

class Extension06 {


}

// 扩展属性
// 接收者类型
//     ↓
val String.lastElement: Char?
    get() = if (isEmpty()) {
        null
    } else {
        get(length - 1)
    }


inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(block: T.() -> Unit) {
    val params = layoutParams as T
    block(params)
    layoutParams = params
}

fun View.updateMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let { param ->
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            left?.let {
                marginStart = left
            }

            right?.let {
                marginEnd = right
            }

            top?.let {
                topMargin = top
            }

            bottom?.let {
                bottomMargin = bottom
            }
        }
    }
}