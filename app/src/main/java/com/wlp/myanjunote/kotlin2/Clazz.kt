package com.wlp.myanjunote.kotlin2

interface Clickable{
    fun click()

    fun showOff() = println("I'm clickable") //带默认实现的方法
}

open class Clazz : Clickable { //这个类是open的，其他类才可以继承它
//public final class com/example/mydemo/Clazz { ... }
    /**
     * Kotlin 接口可以包含属性声明
     * Kotlin的接口和类默认是final public的
     * 嵌套的类默认并不是内部类，他们并没有包含对外部类的隐式引用。
     *
     * 1、Kotlin的接口
     * 一个类开渔实现任意多个接口，但是只能继承一个类
     * 使用冒号 ： ，而不是关键字
     * override 关键字是气质要求的
     *
     * 接口的方法可以有一个默认的实现，类似于Java8
     *
     *
     * 如果你实现了两个接口包含相同的默认方法，那么你只能显示实现这个方法了
     *
     *
     * Java的类和方法默认是open的，而Kotlin中默认的都是final的。
     * open修饰类，才可以被继承
     * open修饰属性和方法，才可以被重写
     *
     * 如果你重写了一个基类或者接口的成员，重写的成员同样是默认open的，
     * 除非你把它改为final的
     *
     * abstract 抽象的，默认是open的
     *
     * 总结：
     * 类中成员的访问修饰符
     * final 默认使用，不能被重写
     * open 可以被重写
     * abstract 必须被重写，该关键字只能在抽象类中使用
     * override 重写父类或接口中的成员，如果没有使用final，重写的成员默认就是开放的
     *
     */
    override fun click() { //这个函数重写了一个open函数，并且他本身同样是open的
        println("clicked")
    }

    override fun showOff() {
        super.showOff()
        //super<Clickable>.showOff()// 这里是指定调用的是Clickable的默认方法
    }

    fun disable(){//这个函数的final：不能在子类中重写它

    }

    open fun disable2(){//这个函数的open：可以在子类中重写它

    }


    /**
     * 可见性修饰符
     *
     * 修饰符        类成员               顶层声明
     * public默认    所有地方可见          所有地方可见
     * internal     只能在模块内部可见     模块中可见
     * protected    子类中可见            ----
     * private      类中可见              文件中可见
     *
     *
     * 内部类和嵌套类：默认是嵌套类
     *                           Java                Kotlin
     * 嵌套类(不存储外部类的引用)    static class A      class A
     * 内部类(存储外部类的引用)      class A             inner class A
     */


    //密封类：定义受限的类继承结构
    /**
     * sealed 类
     * 为父类添加一个sealed添加一个sealed修饰符，对可能创建的子类做出严格的限制。
     * 所有的直接子类必须嵌套在父类中
     *
     * 使用见于 demoSealed
     */
}

