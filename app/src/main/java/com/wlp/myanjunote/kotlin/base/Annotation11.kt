package com.wlp.myanjunote.kotlin.base

/**
注解，它们存在的意义是什么呢？提高代码的灵活性。
在生活当中也有类似的概念，那就是便利贴。
Kotlin 当中的注解，其实就是“程序代码的一种补充”。

元注解，即它本身是注解的同时，还可以用来修饰其他注解。

Kotlin 常见的元注解有四个：
@Target，这个注解是指定了被修饰的注解都可以用在什么地方，也就是目标；
@Retention，这个注解是指定了被修饰的注解是不是编译后可见、是不是运行时可见，也就是保留位置；
@Repeatable，这个注解是允许我们在同一个地方，多次使用相同的被修饰的注解，使用场景比较少；
@MustBeDocumented，指定被修饰的注解应该包含在生成的 API 文档中显示，这个注解一般用于 SDK 当中。
这里，你需要注意的是 Target 和 Retention 的取值。

 */

public enum class AnnotationTarget {
    // 类、接口、object、注解类
    CLASS,
    // 注解类
    ANNOTATION_CLASS,
    // 泛型参数
    TYPE_PARAMETER,
    // 属性
    PROPERTY,
    // 字段、幕后字段
    FIELD,
    // 局部变量
    LOCAL_VARIABLE,
    // 函数参数
    VALUE_PARAMETER,
    // 构造器
    CONSTRUCTOR,
    // 函数
    FUNCTION,
    // 属性的getter
    PROPERTY_GETTER,
    // 属性的setter
    PROPERTY_SETTER,
    // 类型
    TYPE,
    // 表达式
    EXPRESSION,
    // 文件
    FILE,
    // 类型别名
    TYPEALIAS
}

public enum class AnnotationRetention {
    // 注解只存在于源代码，编译后不可见
    SOURCE,
    // 注解编译后可见，运行时不可见
    BINARY,
    // 编译后可见，运行时可见
    RUNTIME
}

class Annotation11 {

}

/**
在架构设计的时候，反射却可以极大地提升架构的灵活性。
很多热门的开源库，也都喜欢用反射来做一些不同寻常的事情。因此，反射也是极其重要的一个语法特性。

Kotlin 反射具备这三个特质：
感知程序的状态，包含程序的运行状态，还有源代码结构；
修改程序的状态；
根据程序的状态，调整自身的决策行为。

首先，是 obj::class，这是 Kotlin 反射的语法，我们叫做类引用，通过这样的语法，我们就可以读取一个变量的
“类型信息”，并且就能拿到这个变量的类型，它的类型是 KClass。

在前面的几个案例当中，我们用到了 Kotlin 反射的几个关键的反射 Api 和类：KClass、KCallable、KParameter、KType。
现在，我们来进一步看看它们的关键成员。
KClass 代表了一个 Kotlin 的类，下面是它的重要成员：
simpleName，类的名称，对于匿名内部类，则为 null；
qualifiedName，完整的类名；
members，所有成员属性和方法，类型是Collection>；
constructors，类的所有构造函数，类型是Collection>>；
……
……

KCallable 代表了 Kotlin 当中的所有可调用的元素，比如函数、属性、甚至是构造函数。

KParameter，代表了KCallable当中的参数。

KType，代表了 Kotlin 当中的类型。

归根结底，反射，其实就是 Kotlin 为我们开发者提供的一个工具，通过这个工具，我们可以让程序在运行的时候“自我反省”。
这里的“自我反省”一共有三种情况，其实跟我们的现实生活类似。
第一种情况，程序在运行的时候，可以通过反射来查看自身的状态。
第二种情况，程序在运行的时候，可以修改自身的状态。
第三种情况，程序在运行的时候，可以根据自身的状态调整自身的行为。

 **/

//fun readMembers(obj: Any) {
//    obj::class.memberProperties.forEach {
//        println("${obj::class.simpleName}.${it.name}=${it.getter.call(obj)}")
//    }
//}
