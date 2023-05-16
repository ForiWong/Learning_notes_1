package com.wlp.myanjunote.kotlin.generics10;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 Created by wlp on 2023/4/25
 1、泛型
 泛型即参数化类型，即实际使用时再确定参数的类型。就像我们在定义方法时可以声明形参，在实际调用方法时传
 入实参，那么我们同样可以把类型也参数化，只有在实际使用时才确定数据的类型 。

 泛型优点
 在编译时提供了类型检查的机制防止非法类型，同时避免了显示的类型转换。

 //先检查，后擦除，再编译
 Java编译器是通过先检查代码中泛型的类型，然后在进行类型擦除，再进行编译。

 //类型检查就是针对引用的
 ArrayList<String> list1 = new ArrayList();
 list1.add("1"); //编译通过
 list1.add(1); //编译错误
 String str1 = list1.get(0); //返回类型就是String

 ArrayList list2 = new ArrayList<String>();
 list2.add("1"); //编译通过
 list2.add(1); //编译通过
 Object object = list2.get(0); //返回类型就是Object

 2、泛型擦除
 Java的泛型只在编译时存在，编译完成后会进行擦除。即在字节码文件中是没有泛型存在的。

 Java中的泛型类型在代码编译时会被擦除掉(一般情况下会被擦成Object类型，如果使用了上限通配符的话会被擦
 成extends右边的类型，如T extends View则最终会被擦成View类型)。

 泛型擦除的原因：出于兼容性的考虑，泛型是JDK1.5以后才引入的，为了兼容之前的JDK版本所以在运行时将泛型
 类型都擦掉以保证和之前JDK版本的java字节码相同。

 https://blog.csdn.net/weixin_37549458/article/details/109653091
 ## 与泛型有关的类型不能和原始类型统一到Class的原因
 ## 产生泛型擦除的原因
 原始类型和新产生的类型都应该统一成各自的字节码文件类型对象。但是由于泛型不是最初Java中的成分。
 如果真的加入了泛型，涉及到JVM指令集的修改，这是非常致命的（简单的说就是Java要向下兼容，所以它是假泛型，不像C#那样）
 Java 引入泛型擦除的原因是避免因为引入泛型而导致运行时创建不必要的类。那我们其实就可以通过定义类的方式，在类信息中保
 留泛型信息，从而在运行时获得这些泛型信息。简而言之，Java 的泛型擦除是有范围的，即类定义中的泛型是不会被擦除的。
 匿名内部类和父类范型，可以避免范型的擦除。

 ## Java中如何引入泛型
 为了使用泛型又不真正引入泛型，Java采用泛型擦除机制来引入泛型。Java中的泛型仅仅是给编译器javac使用的，校验类型，
 确保数据的安全性和免去强制类型转换的麻烦。但是，一旦编译完成，所有的和泛型有关的类型全部擦除。
 虽然全部擦除，但是会保存泛型信息。具体怎么保存呢？？？

 ## Class不能表达与泛型有关的类型
 因此，与泛型有关的参数化类型（ParameterizedType）、类型变量类型(TypeVariable)、限定符类型(WildcardType) 、泛型数组
 类型(GenericArrayType)这些类型编译后全部被打回原形，在字节码文件中全部都是泛型被擦除后的原始类型，并不存在和自身类
 型对应的字节码文件。所以和泛型相关的新扩充进来的类型不能被统一到Class类中。
 那么怎么获取呢？ ↓

 ## 与泛型有关的类型在Java中的表示
 为了通过反射操作这些类型以迎合实际开发的需要，Java就新增了ParameterizedType, TypeVariable< D >, GenericArrayType,
 WildcardType几种类型来代表不能被归一到Class类中的类型但是又和原始类型齐名的类型。

 3、Type
 Type包括：GenericArrayType（带泛型的数组类型）、ParameterizedType（参数化类型，即泛型）、
 WildcardType（ 泛型表达式类型/通配符类型）、TypeVariable（类型变量，比如泛型中的T）、Class（原始/基本类型）。

 Class（原始/基本类型，也叫raw type）：不仅仅包含我们平常所指的类、枚举、数组(String[]、byte[])、注解，
 还包括基本类型（原始类型（primitive type 虚拟机创建 8种int 、float加void）对应的包装引用类型Integer、Float等等

 Type的直接子类只有一个，也就是Class，代表着类型中的原始类型以及基本类型。Class —— 反射基石。其意义为：类的抽象，
 即对“类”做描述：比如类有修饰、字段、方法等属性，有获得该类的所有方法、所有公有方法等方法。同时，Class也是Java类
 型中最重要的一种，表示原始类型、引用类型及基本类型、数组、注解等。


 (List<String>[] pTypeArray, T[] vTypeArray, List<String> list, List<T> typeVariableList,
 List<? extends Number> wildcardList, String[] strings, Test
 test, int a)

 //List<String>[] pTypeArray
 ---------------
 带有泛型数组类型GenericArrayType type [pTypeArray vTypeArray]: java.util.List<java.lang.String>[]
 数组的泛型元素genericComponentType [pTypeArray vTypeArray's component Type]:java.util.List<java.lang.String>
 ---------------
 ---------------
 //T[] vTypeArray
 GenericArrayType type [pTypeArray vTypeArray]: T[]
 genericComponentType [pTypeArray vTypeArray's component Type]:T
 ---------------
 **************
 //List<String> list 参数化类型
 ParameterizedType type [list typeVariableList wildcardList]: java.util.List<java.lang.String>
 ParameterizedType's rawType: interface java.util.List
 ParameterizedType's ownerType is null
 ParameterizedType's actualType is  class java.lang.String
 ParameterizedType's actualType is TypeVariable false
 ParameterizedType's actualType is WildcardType false
 ParameterizedType's actualType is Class true //类Class
 **************
 **************
 //List<T> typeVariableList
 ParameterizedType type [list typeVariableList wildcardList]: java.util.List<T>
 ParameterizedType's rawType: interface java.util.List
 ParameterizedType's ownerType is null
 ParameterizedType's actualType is  T
 ParameterizedType's actualType is TypeVariable true //类型变量
 ParameterizedType's actualType is WildcardType false
 ParameterizedType's actualType is Class false
 **************
 **************
 //List<? extends Number> wildcardList
 ParameterizedType type [list typeVariableList wildcardList]: java.util.List<? extends java.lang.Number>
 ParameterizedType's rawType: interface java.util.List
 ParameterizedType's ownerType is null
 ParameterizedType's actualType is  ? extends java.lang.Number
 ParameterizedType's actualType is TypeVariable false
 ParameterizedType's actualType is WildcardType true//通配符类型
 ParameterizedType's actualType is Class false
 **************
 //String[] 数组 -> Class
 type [strings test]: class [Ljava.lang.String;
 //Test 最平常的类
 type [strings test]: class com.wlp.myanjunote.kotlin.generics10.Test
 //int  基本数组类型
 type [strings test]: int

 */
public class TypeTest<T> {

    private Class<T> t;

    public TypeTest(Class<T> t){
        this.t = t;
    }

    @SuppressWarnings("unchecked")
    T[] create(int size){
        return (T[])Array.newInstance(t, size);
    }

    /* 这里面有各种各样的数组：各有不同 方便看测试效果
     * 含有泛型数组的才是GenericArrayType
     *
     * 泛型出现之后，扩充了数据类型。从只有原始类型扩充了参数化类型（ParameterizedType）、类型变量（TypeVariable）、
     * 泛型数组类型(GenericArrayType)，通配符类型（WildcardType）。
     *
     * 没有泛型的时候，只有原始类型。此时，所有的原始类型都通过字节码文件类Class类进行抽象。Class类的一个具体对象就
     * 代表一个指定的原始类行。
     * 泛型出现之后，扩充了数据类型。从只有原始类型(Class)扩充了参数化类型（ParameterizedType）、类型变量（TypeVariable）、
     * 泛型数组类型(GenericArrayType)，通配符类型（WildcardType）。
     * 以上五种类型的父类为Type接口，它位于反射包java.lang.reflect内。
     */
    public void testGenericArrayType(List<String>[] pTypeArray, T[] vTypeArray, List<String> list, List<T> typeVariableList,
                                     List<? extends Number> wildcardList, String[] strings, Test test, int a) {
    }

    public static void main(String[] args) {
        //如何获取泛型参数的Class类型
        /*
         * A<T ,E>
         * ["T", "E"]  //这个就很奇怪了
         */
        //new B<String, String>();//会报错
        /*
         * B<java.lang.String, java.lang.Integer>
         * [java.lang.String, java.lang.Integer]
         */
        new C();

        //泛型数组类型  GenericArrayType
        //ArrayList<String>[] aa = new ArrayList<String>[10];//error
        ArrayList<String>[] bb = new ArrayList[10];//success
        System.out.println(bb.getClass());

        List<String> list = new ArrayList<>();

        if(list.getClass().getGenericSuperclass() instanceof ParameterizedType){
            System.out.println(bb.getClass() + "is ParameterizedType" );
        }

        List<?>[] cc = new ArrayList<?>[10];

        TypeTest<Integer> tt = new TypeTest<Integer>(Integer.class);
        Integer[] integers = tt.create(10);
        System.out.println(integers.length);
        System.out.println(integers.getClass());//class [Ljava.lang.Integer;

        Method[] declaredMethods = TypeTest.class.getDeclaredMethods();

        for (Method method : declaredMethods) {
            // main方法不用处理
            if (method.getName().startsWith("main")) {
                continue;
            }

            // 开始处理该方法===打印出此方法签名
            System.out.println("declare Method:" + method); //declare Method:public void com.fsx.maintest.GenericArrayTypeTest.testGenericArrayType(java.util.List[],java.lang.Object[],java.util.List,java.lang.String[],com.fsx.maintest.GenericArrayTypeTest[])

            // 该方法能获取到该方法所有的实际的参数化类型，比如本例中有五个参数，那数组长度就是5
            Type[] types = method.getGenericParameterTypes();

            // 分组打印出来
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    System.out.println("**************");
                    System.out.println("ParameterizedType type [list typeVariableList wildcardList]: " + parameterizedType);
                    System.out.println("ParameterizedType's rawType: " + parameterizedType.getRawType());
                    System.out.println("ParameterizedType's ownerType is " + (parameterizedType.getOwnerType()));
                    System.out.println("ParameterizedType's actualType is  " + parameterizedType.getActualTypeArguments()[0]);
                    System.out.println("ParameterizedType's actualType is TypeVariable " + (parameterizedType.getActualTypeArguments()[0] instanceof TypeVariable));
                    System.out.println("ParameterizedType's actualType is WildcardType " + (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType));
                    System.out.println("ParameterizedType's actualType is Class " + (parameterizedType.getActualTypeArguments()[0] instanceof Class));
                    System.out.println("**************");
                }
                if (type instanceof GenericArrayType) {//T[]
                    System.out.println("---------------");
                    GenericArrayType genericArrayType = (GenericArrayType) type;
                    System.out.println("GenericArrayType type [pTypeArray vTypeArray]: " + genericArrayType);
                    Type genericComponentType = genericArrayType.getGenericComponentType();
                    System.out.println("genericComponentType [pTypeArray vTypeArray's component Type]:" + genericComponentType);
                    System.out.println("---------------");
                }
                if (type instanceof WildcardType) {
                    WildcardType wildcardType = (WildcardType) type;
                    System.out.println("WildcardType type [wildcardList]: " + wildcardType);
                }
                if (type instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) type;
                    System.out.println("TypeVariable type [typeVariable]:" + typeVariable);
                }
                if (type instanceof Class) {
                    Class clazz = (Class) type;
                    System.out.println("type [strings test]: " + clazz);
                }
            }
        }
    }

}
