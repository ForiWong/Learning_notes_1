package com.wlp.myanjunote.kotlin.generics10;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlp on 2023/3/17
 * Description:泛型的理解，来自扔物线。
 * https://rengwuxian.com/kotlin-generics/

 （1）泛型基本概念
 泛型是JDK5.0以后增加的新特性。
 泛型的本质就是“数据类型的参数化”，处理的数据类型不是固定的，而是可以作为参数传入。
 泛型的本质是参数化类型，即给类型指定一个参数，然后在使用时再指定此参数具体的值，那样这个类型就可以在使用时决定了。
 这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。

 （2）使用泛型主要是两个好处：泛型主要是方便了程序员的代码编写，以及更好的安全性检测。
 代码可读性更好【不用强制转换】
 程序更加安全  【只要编译时期没有警告，运行时期就不会出现ClassCastException异常】

 （3）类型擦除
 编码时采用泛型写的类型参数，编译器会在编译时去掉泛型类型，这称之为“类型擦除”。
 泛型主要用于编译阶段，编译后生成的字节码class文件不包含泛型中的类型信息，涉及类型转换仍然是普通的强制类型转换。
 类型参数在编译后会被替换成Object，运行时虚拟机并不知道泛型。

 泛型只存在代码编译阶段，在进入JVM 之前，泛型相关的信息会被擦除掉，我们称之为类型擦除
 比如，字节码文件这样：

 public static void main(String[] args) {
     HashMap map = new HashMap();//...
     map.put("hello", "hello");
     map.put("world", "world");
     System.out.println((String)map.get("hello"));//...
     System.out.println((String)map.get("world"));
 }

 class Controller<T: TV> { ... }

 */
public class Test {

    public static void main(String[] args) {
        //泛型是编译时的一种类型，运行时无效
        ArrayList<String> list=new ArrayList<String>();
        list.add("apple");
        list.add("pear");
        try {
            //获取类对象
            Class<?> c = list.getClass();
            //获取Method对象
            Method m = c.getDeclaredMethod("add", Object.class);
            //执行方法，并不会报错
            m.invoke(list, 200);
            System.out.println(list); //[apple, pear, 200]
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> a = new ArrayList<String>();
        ArrayList b = new ArrayList();
        Class c1 = a.getClass();  //获取泛型的类型
        Class c2 = b.getClass();
        System.out.println(c1 + " : " + c2);// class java.util.ArrayList : class java.util.ArrayList
        System.out.println(c1 == c2); // 输出true
        // 在编译过程中，正确检验泛型结果后，会将泛型的相关信息擦出，并且在对象进入和离开方法的
        // 边界处添加类型检查和类型转换的方法。也就是说，泛型信息不会进入到运行时阶段。
    }

    public void test1() {
        List<A> list = new ArrayList<A>();//这里是多态
        // List<A> 表示这是一个泛型类型为 A 的 List。

        /**
         * 泛型 宽泛的，不确定的类型
         * 将类型参数化，变得更加灵活。
         *
         * 泛型，即 "参数化类型"，将类型参数化，可以用在类，接口，函数上。
         *
         * 「泛型」，它的意思是把具体的类型泛化，编码的时候用符号来指代类型，在使用的时候，再确定它的类型。
         * 前面那个例子，List<A> 就是泛型类型声明。
         **/

        //那么泛型类型是不是也适用类型的多态呢？
        //这是通常使用的多态
        A a = new B();

        //这也是多态，类本身的继承关系，而不是里面的泛型的类型
        List<A> list1 = new ArrayList<A>();
        List<B> list2 = new ArrayList<B>();

        //这里就会提示不兼容类型了  types. Found: 'ArrayList<B>', required: 'List<A>'
        //List<A> list3 = new ArrayList<B>();
        //即使改成如下面，也是不可以的
        //ArrayList<A> list4 = new ArrayList<B>();

        /**
         * （1）Java泛型的不可变性
         *
         * 这是因为 Java 的泛型本身具有「不可变性 Invariance」，
         * Java 里面认为 List<A> 和 List<B> 类型并不一致，
         * 也就是说，子类的泛型（List<B>）不属于泛型（List<A>）的子类。
         *
         * Java 的泛型类型会在编译时发生类型擦除，为了保证类型安全，不允许这样赋值。
         */

        //List<A> list5 = new ArrayList<B>();
        List<? extends A> list6 = new ArrayList<B>();
        /**
         * （2）? extends 叫做「上界通配符」， 协变性，本身是生产者。
         *
         * 这个 ? extends 叫做「上界通配符」，可以使 Java 泛型具有「协变性 Covariance」，协变就是允许上面的赋值是合法的。
         *
         * 在继承关系树中，子类继承自父类，可以认为父类在上，子类在下。extends 限制了泛型类型的父类型，所以叫上界。
         *
         * ? extends 它有两层意思：
         *
         * 其中 ? 是个通配符，表示这个 List 的泛型类型是一个未知类型。
         * extends 限制了这个未知类型的上界，也就是泛型类型必须满足这个 extends 的限制条件，这里和定义 class 的 extends 关键字有点不一样：
         * 它的范围不仅是所有直接和间接子类，还包括上界定义的父类本身，也就是 A。
         * 它还有 implements 的意思，即这里的上界也可以是 interface。
         */
        List<? extends TextView> textViews1 = new ArrayList<TextView>(); // 👈 本身
        List<? extends TextView> textViews2 = new ArrayList<Button>(); // 👈 直接子类
        List<? extends TextView> textViews3 = new ArrayList<RadioButton>(); // 👈 间接子类

        TextView textView = textViews3.get(0); // 👈 get 可以
        //textViews3.add(textView);
        //'add(capture<? extends android.widget.TextView>)' in 'java.util.List' cannot be applied to '(android.widget.TextView)'
        //👆 add 会报错，no suitable method found for add(TextView)

        /**
         * 怎么理解呢？
         *
         * 前面说到 List<? extends TextView> 的泛型类型是个未知类型 ?，编译器也不确定它是啥类型，只是有个限制条件。
         *
         * 由于它满足 ? extends TextView 的限制条件，所以 get 出来的对象，肯定是 TextView 的子类型，根据多态的特性，
         * 能够赋值给 TextView，啰嗦一句，赋值给 View 也是没问题的。
         *
         * 到了 add 操作的时候，我们可以这么理解：
         *
         * List<? extends TextView> 由于类型未知，它可能是 List<Button>，也可能是 List<TextView>。
         * 对于前者，显然我们要添加 TextView 是不可以的。
         * 实际情况是编译器无法确定到底属于哪一种，无法继续执行下去，就报错了。
         *
         * 使用了 ? extends 泛型通配符的 List，只能够向外提供数据被消费，从这个角度来讲，向外提供数据的一方
         * 称为「生产者 Producer」。
         **/


        List<? super Button> buttons = new ArrayList<TextView>();
        /**
         * ? super
         * 这个 ? super 叫做「下界通配符」，可以使 Java 泛型具有「逆变性 Contravariance」。
         *
         * 与上界通配符对应，这里 super 限制了通配符 ? 的子类型，所以称之为下界。
         *
         * 它也有两层意思：
         *
         * 通配符 ? 表示 List 的泛型类型是一个未知类型。
         * super 限制了这个未知类型的下界，也就是泛型类型必须满足这个 super 的限制条件。
         * super 我们在类的方法里面经常用到，这里的范围不仅包括 Button 的直接和间接父类，也包括下界 Button 本身。
         * super 同样支持 interface。
         * 上面的例子中，TextView 是 Button 的父类型 ，也就能够满足 super 的限制条件，就可以成功赋值了。
         * */
        List<? super Button> buttons1 = new ArrayList<Button>(); // 👈 本身
        List<? super Button> buttons2 = new ArrayList<TextView>(); // 👈 直接父类
        List<? super Button> buttons3 = new ArrayList<Object>(); // 👈 间接父类

        List<? super C> cc = new ArrayList<A>();
        List<? super C> cc2 = new ArrayList<B>();
        Object object = cc.get(0); // 👈 get 出来的是 Object 类型
        //C c = cc.get(0);//不行
        //B b = cc.get(0);//不行
        cc.add(new C()); // 👈 add 操作是可以的，元素一定是C或者C的父类，添加进去肯定没问题
        //cc.add(new B()); // 不行

        /**
         * 解释下，首先 ? 表示未知类型，编译器是不确定它的类型的。
         *
         * 虽然不知道它的具体类型，不过在 Java 里任何对象都是 Object 的子类，所以这里能把它赋值给 Object。
         *
         * Button 对象一定是这个未知类型的子类型，根据多态的特性，这里通过 add 添加 Button 对象是合法的。
         *
         * 使用下界通配符 ? super 的泛型 List，只能读取到 Object 对象，一般没有什么实际的使用场景，通常也只拿它来添加数据，也就是消费已有的 List<? super Button>，往里面添加 Button，因此这种泛型类型声明称之为「消费者 Consumer」。
         *
         *
         * 小结下，Java 的泛型本身是不支持协变和逆变的。
         *
         * 可以使用泛型通配符 ? extends 来使泛型支持协变，但是「只能读取不能修改」，这里的修改仅指对泛型集合添加元素，如果是 remove(int index) 以及 clear 当然是可以的。
         * 可以使用泛型通配符 ? super 来使泛型支持逆变，但是「只能修改不能读取」，这里说的不能读取是指不能按照泛型类型读取，你如果按照 Object 读出来再强转当然也是可以的。
         *
         * 根据前面的说法，这被称为 PECS 法则：「Producer-Extends, Consumer-Super」。
         * 只能使用不能修改
         *  或者
         * 只能修改不能使用
         *
         * */

    }

    /**
     * 由于 Java 中的泛型存在类型擦除的情况，任何在运行时需要知道泛型确切类型信息的操作都没法用了。
     *
     * 这个问题，在 Java 中的解决方案通常是额外传递一个 Class<T> 类型的参数，然后通过 Class#isInstance 方法来检查：
     **/
//    <T> void printIfTypeMatch(Object item) {
//        if (item instanceof T) { // 👈 IDE 会提示错误，illegal generic type for instanceof
//            System.out.println(item);
//        }
//    }

    //判断T是哪个类的
    <T> void check(Object item, Class<T> type) {
        if (type.isInstance(item)) {
            System.out.println(item);
        }
    }

    //父类A
    class A {

    }

    interface D{

    }

    //子类B
    class B extends A{

    }

    //B类的子类C
    class C extends B{

    }

    //T 的类型必须是 A  的子类型
    class Monster<T extends A>{

    }

    //T 的类型必须是 A 、D 的子类型
    class Monster2<T extends A & D>{

    }

}
