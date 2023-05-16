1、JVM虚拟机
Java Virtual Machine
和JVM绑定的是编译后的class文件，即字节码文件。
而并非和Java语言绑定。

2、字节码结构

开头先标记长度
LV length + value
magic + 版本号 + 类名 + 属性 + 方法 + ....... 

字节码结构:
magic 魔数
minor_version 次要版本
major_version 重大版本
constant_pool_count 常量池的数量
constant_pool[] 常量池数组
        {
            tag 常量类型
            length 常量长度
            value 常量的值
        }
access_flags 访问标志
this_class 类名
super_class 父类类名
interfaces_count 接口数量
interfaces[interfaces_count] 接口数组
fields_count 属性
fields[fields_count]
methods_count 方法
methods[methods_count]
attributes_count 附加属性
attributes[attributes_count]

结构：特别的紧凑
官方文档： https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html

3、虚拟机字节码指令表
https://segmentfault.com/a/1190000008722128

monitorenter
monitorexit

3、JVM内存结构
1）JVM怎么判断对象是否可以被回收？ GCRoot可达性
2）知道了哪些对象不可达之后，如何进行回收？
GC回收方法：
1）标记-清除法
会出现很多不连续的内存碎片
2）标记-整理法
效率不高
3）复制算法
只使用了一半的内存，很浪费
4）JVM将堆分成三个区域
   新生代      老年代      永久代
    Eden
  from  to    

所以，GC的时机是不确定的。
学这个有什么用呢？
