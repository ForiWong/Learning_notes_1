package com.wlp.myanjunote.kotlin2.reflex10

fun main(args : Array<String>){
    val person = Person("Alice", 28)
    val kClass = person.javaClass.kotlin
    println(kClass.simpleName)
    //kClass.memberProperties.forEach { println(it.name) }
    val memberProperty = Person::age
    println(memberProperty.get(person))

    val kFunction = ::sum
    println(kFunction.call(1, 2) + kFunction.invoke(3, 4))

    val personClazz = Person :: class //KClass<Person>

}

/**
 * 注解与反射
 * 1、注解
 * 1）应用注解
 * 2）注解目标
 * 3）使用注解定制json序列化
 * 4）声明注解
 * 5）元注解：控制如何处理一个注解
 * 6）使用类做注解参数
 * 7）使用泛型类做注解参数
 *
 * 2、反射：
 * 在运行时对kotlin对象进行自省
 * 1）KClass\KCallable\KFunction\KProperty
 * 2)使用反射实现对象序列化
 * 3）用注解定制序列化
 * 4）json解析和对象反序列化
 * 5）反序列化对最后一步：callBy()和使用反射创建对象
 *
 */

/**

注解的本质
注解实际上就是一种代码标签，它作用的对象是代码。它可以给特定的注解代码标注一些额外的信息。然而这些信息可以选择不同保留时
期，比如源码期、编译期、运行期。然后在不同时期，可以通过某种方式获取标签的信息来处理实际的代码逻辑，这种方式常常就是我们
所说的反射。

注解的定义
在Kotlin中注解核心概念和Java一样，注解就是为了给代码提供元数据。并且注解是不直接影响代码的执行。一个注解允许你把额外的
元数据关联到一个声明上，然后元数据就可以被某种方式(比如运行时反射方式以及一些源代码工具)访问。

Kotlin中的元注解
和Java一样在Kotlin中，一个Kotlin注解类自己本身也可以被注解，可以给注解类加注解。我们把这种注解称为元注解，可以把它理解
为一种基本的注解，可以把它理解为一种特殊的标签，用于标注标签的标签。

Kotlin中的元注解类定义于kotlin.annotation包中，主要有: @Target、@Retention、@Repeatable、@MustBeDocumented
4种元注解相比Java中5种元注解: @Target、@Retention、@Repeatable、@Documented、@Inherited少了 @Inherited元注解。

@Target元注解
1、介绍
Target顾名思义就是目标对象，也就是这个标签作用于哪些代码中目标对象，可以同时指定多个作用的目标对象。

2、源码定义
@Target(AnnotationTarget.ANNOTATION_CLASS)//可以给标签自己贴标签
@MustBeDocumented
//注解类构造器参数是个vararg不定参数修饰符，所以可以同时指定多个作用的目标对象
public annotation class Target(vararg val allowedTargets: AnnotationTarget)

3、@Target元注解作用的目标对象
在@Target注解中可以同时指定一个或多个目标对象，那么到底有哪些目标对象呢？这就引出另外一个AnnotationTarget枚举类

public enum class AnnotationTarget {
    CLASS, //表示作用对象有类、接口、object对象表达式、注解类
    ANNOTATION_CLASS,//表示作用对象只有注解类
    TYPE_PARAMETER,//表示作用对象是泛型类型参数(暂时还不支持)
    PROPERTY,//表示作用对象是属性
    FIELD,//表示作用对象是字段，包括属性的幕后字段
    LOCAL_VARIABLE,//表示作用对象是局部变量
    VALUE_PARAMETER,//表示作用对象是函数或构造函数的参数
    CONSTRUCTOR,//表示作用对象是构造函数，主构造函数或次构造函数
    FUNCTION,//表示作用对象是函数，不包括构造函数
    PROPERTY_GETTER,//表示作用对象是属性的getter函数
    PROPERTY_SETTER,//表示作用对象是属性的setter函数
    TYPE,//表示作用对象是一个类型，比如类、接口、枚举
    EXPRESSION,//表示作用对象是一个表达式
    FILE,//表示作用对象是一个File
    @SinceKotlin("1.1")
    TYPEALIAS//表示作用对象是一个类型别名
}


@Retention元注解
1、介绍
Retention对应的英文意思是保留期，当它应用于一个注解上表示该注解保留存活时间，不管是Java还是Kotlin一般都有三种时期:
源代码时期(SOURCE)、编译时期(BINARY)、运行时期(RUNTIME)。

2、源码定义
@Target(AnnotationTarget.ANNOTATION_CLASS)//目标对象是注解类
public annotation class Retention(val value: AnnotationRetention = AnnotationRetention.RUNTIME)
//接收一个参数，该参数有个默认值，默认是保留在运行时期

3、@Retention元注解的取值
@Retention元注解取值主要来源于AnnotationRetention枚举类

public enum class AnnotationRetention {
    SOURCE,//源代码时期(SOURCE): 注解不会存储在输出class字节码中
    BINARY,//编译时期(BINARY): 注解会存储出class字节码中，但是对反射不可见
    RUNTIME//运行时期(RUNTIME): 注解会存储出class字节码中，也会对反射可见, 默认是RUNTIME
}


@MustBeDocumented元注解
1、介绍
该注解比较简单主要是为了标注一个注解类作为公共API的一部分，并且可以保证该注解在生成的API文档中存在。

2、源码定义
@Target(AnnotationTarget.ANNOTATION_CLASS)//目标对象只能是注解类
public annotation class MustBeDocumented


@Repeatable元注解
1、介绍
这个注解决定标注的注解在一个注解在一个代码元素上可以应用两次或两次以上。

2、源码定义
@Target(AnnotationTarget.ANNOTATION_CLASS)//目标对象只能是注解类
public annotation class Repeatable

----------------------------------------------------------------------------------------------------
目录
1. kotlin 定义注解
2. kotlin 元注解
3. kotlin 使用注解

1. kotlin 定义注解
注解属性在使用时指定，其后不会再变，只能声明为只读属性

annotation class Annotation1(val name: String, val desc: String)

annotation class Annotation2(val field1: Int, val field2: String)

2. kotlin 元注解
@Retention : 修饰注解可以保留多长时间 SOURCE, BINARY, RUNTIME
@Target : 指定注解可以修饰的程序目标
@Repeatable : 可重复修饰的注解

// @Retention(value = AnnotationRetention.SOURCE) // 注解只保留在源代码中，注解会被编译器丢弃
// @Retention(value = AnnotationRetention.BINARY) // 注解将被记录在 class 文件中，JVM无法获取
@Retention(value = AnnotationRetention.RUNTIME) // 注解将被记录在 class 文件中，JVM可以获取，程序可以通过反射获取。默认值
annotation class Annotation3()

// 注解只能修饰类
@Target(AnnotationTarget.CLASS)
annotation class Annotation4()

// 注解只能修饰注解
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Annotation5()

// 注解只能修饰函数或方法，不含构造方法
@Target(AnnotationTarget.FUNCTION)
annotation class Annotation6()

// 注解只能修饰构造方法
@Target(AnnotationTarget.CONSTRUCTOR)
annotation class Annotation7()

// 注解只能修饰属性
@Target(AnnotationTarget.PROPERTY)
annotation class Annotation8()

// 注解只能修饰字段，包括属性的支持字段
@Target(AnnotationTarget.FIELD)
annotation class Annotation9()

// 注解只能修饰局部变量
@Target(AnnotationTarget.LOCAL_VARIABLE)
annotation class Annotation10()

// 注解只能修饰函数或构造函数的值参数
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Annotation11()

// 注解只能修饰表达式
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.EXPRESSION)
annotation class Annotation12()

// 注解只能修饰类型别名
@Target(AnnotationTarget.TYPEALIAS)
annotation class Annotation13()

// 指定多个可修饰的程序目标
@Target(allowedTargets = [AnnotationTarget.CLASS, AnnotationTarget.FUNCTION])
annotation class Annotation14()

// 2.3 可重复修饰的注解
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
annotation class RepeatAnnotation(val field1: Int, val field2: String)

class DemoAnnotation {
    @RepeatAnnotation(1, "2")
    @RepeatAnnotation(3, "4")
    fun method() {
    }
}


3. kotlin 使用注解

class TestAnnotation {

@Annotation1(name = "method", desc = "this is a normal method")
@Annotation2(field1 = 1001, field2 = "test annotation")
fun method() {
    println("invoke method")
    }
}

fun main() {
    // 获取 TestAnnotation 的方法定义的注解信息
    val annotationList = TestAnnotation::method.annotations

    annotationList.forEach { annotation ->

        if (annotation is Annotation1) {

            println("name: ${annotation.name}, desc: ${annotation.desc}")

        } else if (annotation is Annotation2) {

            println("field1: ${annotation.field1}, field2: ${annotation.field2}")

        }
   }
}


 **/