1、什么是内存泄露？
传统定义的内存泄露：申请的内存忘记释放了。
Android （或 JVM）的内存泄露：短⽣命周期的对象被⻓⽣命周期的对象持有，从⽽导致短⽣命周期的对象不能被释放。

2、垃圾回收机制
垃圾回收机制分为「引⽤计数法」和「可达性分析法」：
（1）「引⽤计数法」 Python , Object-C , Swift ⽤⼀个计数器记录⼀个对象被引⽤的次数，
如果引⽤的次数被减少到 0 那么说明这个对象是垃圾对象。
都是引⽤计数（引⽤计数有循环引⽤的问题）

（2）「可达性分析法」 Java
Jvm 通过⼀些 GC Roots 向下搜索，如果可以被 Gc Roots 引⽤到的对象，说明这个对象不是垃圾对象，反之
这个对象就算互相引⽤了也是垃圾对象。
那些对象会被作为 GC Roots 呢？
在线程栈中的局部变量，也就是正在被调⽤的⽅法，它⾥⾯的参数和局部变量
存活的线程对象
JNI的引⽤
Class对象，因为 Android 加载 Class 后不会卸载 Class
引⽤类型的静态变量

3、内存泄露的问题
内存泄漏并不会⻢上把程序搞挂掉。但是随着应⽤的使⽤，不能回收的垃圾对象会越来越多，就导致了可⽤的内存
越来越少，到最后应⽤就有可能在任何位置抛出OutOfMemoryError ，这种情况下，每次 OOM 错误堆栈都不同，就
很难定位问题。

4、四⼤引⽤
强⼀点的引⽤
    强引⽤——不会被垃圾回收
弱⼀点的引⽤
    弱引⽤——可以通过 get() 获得引⽤对象，会被垃圾回收
    软引⽤——可以通过 get() 获得引⽤对象，内存不⾜会被垃圾回收
    虚引⽤——不能通过 get() 获得引⽤对象，会被垃圾回收

弱引⽤在引⽤对象被垃圾回收之前，会将引⽤放⼊它关联的队列中。所以可以通过队列中是否有对 应的引⽤来判断对象是否被垃圾回收了。

todo 队列的使用

5、触发GC的正确姿势
runGC(){
    Runtime.getRuntime().gc();
    enqueueReferences();
    System.runFinalization();
}

6、2.0 不需要主动初始化的原理
ContentProvider#onCreate 会在 Application#onCreate 之前先执⾏，在这个 onCreate 中就可以进⾏初始化了。
LeakCanary 2.0 利⽤了这个原理，所以不需要我们⼿动进⾏初始化

7、LeakCanary检测内存泄漏的开源库。
LeakCanary的源码追踪还是要一边看文档，一边看源码。
LeakCanary针对那些对象进行了监听


LeakCanary 通过以下 2 点实现内存泄漏监控：
1、在 Android Framework 中注册无用对象监听： 通过全局监听器或者 Hook 的方式，在 Android Framework 上
监听 Activity 和 Service 等对象进入无用状态的时机（例如在 Activity#onDestroy() 后，产生一个无用 Activity 对象）;

2、利用引用对象可感知对象垃圾回收的机制判定内存泄漏： 为无用对象包装弱引用，并在一段时间后（默认为五秒）
观察弱引用是否如期进入关联的引用队列，是则说明未发生泄漏，否则说明发生泄漏（无用对象被强引用持有，导致无法回收，即泄漏）

