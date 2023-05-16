package com.wlp.myanjunote.kotlin.generics10;

import java.lang.reflect.Type;
import java.util.Date;

/**
 使用侧泛型获取方法
 //实际上就是通过声明侧的方法

 上面讲的相关类的获取泛型类型相关方法都只是针对声明侧的泛型。因为声明侧的泛型被记录在Class文件的Constant
 pool中以Signature的形式保存。所以Java提供了相关方法能获取到这些信息。

 那使用侧的泛型信息怎么获取呢？由于使用侧的泛型信息在编译期的时候就被类型擦除了，所以运行时是没办法获取
 到这些泛型信息的。

 匿名内部类实现获取使用侧的泛型类型
 上文有讲到，在声明侧的泛型中，针对泛型类或泛型接口的声明的泛型，Class类提供了getGenericSuperclass()、
 getGenericInterfaces()来获取其子类（实现类）上声明的具体泛型类型信息。

 而匿名内部类是什么？其本质就是一个继承/实现了某个类(接口,普通类,抽象类)的子类匿名对象。

 匿名内部类实现获取使用侧的泛型类型的原理：
 1)定义泛型类，泛型类中有一个Type类型的字段，用于保存泛型类型的Type
 2)通过匿名内部类的方式创建该泛型类的子类实例（指定了具体的泛型类型）
 3)在创建子类实例的构造方法中，已经通过子类的Class的getGenericSuperclass()获取到了泛型类型信息并赋值给了Type类型的字段中。
 随后任何地方，只要得到了该子类实例，就可以通过实例得到泛型类型的Type，这就得到了使用侧的泛型类信息。

 **/
public class GetTypeUse {

    public static  <T> T get(GetTypeUseSuper<T> tTestClass2) throws IllegalAccessException, InstantiationException {
        Type type = tTestClass2.getType();
        Class clazz = (Class) type;
        return (T)clazz.newInstance();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        String str = get(new GetTypeUseSuper<String>() {});
        Date date = get(new GetTypeUseSuper<Date>() {});
    }
}