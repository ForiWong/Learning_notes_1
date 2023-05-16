package com.wlp.myanjunote.jvm;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 作者：彭旭锐 JVM 系列（5）吊打面试官：说一下 Java 的四种引用类型
 链接：https://juejin.cn/post/7130652285817847844

 1.1 Java 四大引用类型
 Java 引用是 Java 虚拟机为了实现更加灵活的对象生命周期管理而设计的对象包装类，一共有四种引用类型，分别
 是强引用、软引用、弱引用和虚引用。我将它们的区别概括为 3 个维度：

 ## 维度 1 - 对象可达性状态的区别：
 强引用指向的对象是强可达的，而其他引用指向的对象都是弱可达的。当一个对象存在到 GC Root 的引用链时，
 该对象被认为是强可达的。只有强可达的对象才会认为是存活的对象，才能保证在垃圾收集的过程中不会被回收；

 ## 维度 2 - 垃圾回收策略的区别：
 除了影响对象的可达性状态，不同的引用类型还会影响垃圾收集器回收对象的激进程度：

 强引用： 强引用指向的对象不会被垃圾收集器回收；
 软引用： 软引用是相对于强引用更激进的策略，软引用指向的对象在内存充足时会从垃圾收集器中豁免，起到类
 似强引用的效果，但在内存不足时还是会被垃圾收集器回收。那么软引用通常是用于实现内存敏感的缓存，当有足
 够空闲内存时保留内存，当空闲内存不足时清理缓存，避免缓存耗尽内存；

 弱引用和虚引用： 弱引用和虚引用是相对于软引用更激进的策略，弱引用指向的对象无论在内存是否充足的时候，
 都会被垃圾收集器回收；

 ## 维度 3 - 感知垃圾回收时机：
 虚引用主要的作用是提供了一个感知对象被垃圾回收的机制。在虚拟机即将回收对象之前，如果发现对象还存在虚
 引用，则会在回收对象后会将引用加入到关联的引用队列中。程序可以通过观察引用队列的方式，来感知到对象即
 将被垃圾回收的时机，再采取必要的措施。
 例如 Java Cleaner 工具类，就是基于虚引用实现的回收工具类。需要
 特别说明的是，并不是只有虚引用才能与引用队列关联，软引用和弱引用都可以与引用队列关联，只是说虚引用唯
 一的作用就是感知对象垃圾回收时机。


 2.1 使用引用对象
 1、创建引用对象： 直接通过构造器创建引用对象，并且直接在构造器中传递关联的实际对象和引用队列。引用队
 列可以为空，但虚引用必须关联引用队列，否则没有意义；

 2、获取实际对象： 在实际对象被垃圾收集器回收之前，通过 Reference#get() 可以获取实际对象，在实际对象
 被回收之后 get() 将返回 null，而虚引用调用 get() 方法永远是返回 null；

 3、解除关联关系： 调用 Reference#clear() 可以提前解除关联关系。

 get() 和 clear() 最终是调用 native 方法，我们在后文分析。

 */
public class ReferenceTest {

    /**
     以下为 ReferenceQueue 的使用模板，主要分为 2 个阶段：

     阶段 1： 创建引用队列实例，并在创建引用对象时关联该队列；
     阶段 2： 对象在被垃圾回收后，引用对象会被加入引用队列（根据下文源码分析，引用对象在进入引用队列
     的时候，实际对象已经被回收了）。通过观察 ReferenceQueue#poll() 的返回值可以感知对象垃圾回收的时机。
     */
    public static void main(String[] args) {
        // 阶段 1：
        // 创建对象
        String strongRef = new String("abc");
        // 1、创建引用队列
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        // 2、创建引用对象，并关联引用队列
        WeakReference<String> weakRef = new WeakReference<>(strongRef, referenceQueue);
        System.out.println("weakRef 1:" + weakRef);

        // 3、断开强引用
        strongRef = null;

        System.gc();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 阶段 2：
        // 延时 5000 是为了提高 "abc" 被回收的概率
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(weakRef.get()); // 输出 null
                // 观察引用队列
                Reference<? extends String> ref = referenceQueue.poll();
                if (null != ref) {
                    System.out.println("weakRef 2:" + ref);
                    // 虽然可以获取到 Reference 对象，但无法获取到引用原本指向的对象
                    System.out.println(ref.get()); // 输出 null
                }
            }
        }).start();
    }
}
