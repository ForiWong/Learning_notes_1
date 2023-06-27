https://www.jianshu.com/p/63da6197913b

什么是反射
JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；
对于任意一个对象，都能够调用它的任意一个方法和属性。

反射的用途
在运行时判断任意一个对象所属的类；
在运行时构造任意一个类的对象；
在运行时判断任意一个类所具有的成员变量和方法；
在运行时调用任意一个对象的方法；


kotlin反射

Kotlin 的反射需要集成 org.jetbrains.kotlin:kotlin-reflect 仓库,版本保持与 kotlin 一致。

implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
在Kotlin中，字节码对应的类是kotlin.reflect.KClass，因为Kotlin百分之百兼容Java，所以Kotlin中可以使用Java中的反射，
但是由于Kotlin中字节码.class对应的是KClass类，所以如果想要使用Java中的反射，需要首先获取Class的实例，在Kotlin中可
以通过以下两种方式来获取Class实例。

//1.通过实例.javaClass
var hello = HelloWorld()
hello.javaClass

//2.通过类Kclass类的.java属性
HelloWorld::class.java
获取了Class实例，就可以调用上面介绍的方法，获取各种在Java中定义的类的信息了。


