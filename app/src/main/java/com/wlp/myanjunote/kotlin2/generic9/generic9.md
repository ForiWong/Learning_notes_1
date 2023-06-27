https://www.jianshu.com/p/fd1594dfa05d
Kotlin 之 泛型

Kotlin 泛型内容包括：

    泛型函数与类
    类型擦除与实化类型参数
    声明点变形与使用点型变
    实化类型参数允许再运行时的内联函数调用中引用作为类型实参的具体类型；
    声明点变型说明一个带类型参数的泛型类型，是否是另一个泛型类型的子或超类型；
    使用点变型在具体使用一个泛型类型时，达到与Java通配符一样的效果；

1. 泛型类型参数

可以定义带类型形参的类型,当此类型的实例被创建时，类型形参被替换成类型实参的具体类型，如：List<String>, Map<K, V> 等；

在使用时，Kotlin 编译器能推导出类型实参:

val list = listOf("ABC", “DEF”)  // 等价于 val list = listOf<String>("ABC", “DEF”)

1.1 泛型函数和属性

这个概念跟Java一样，泛型函数有自己的类型形参，泛型函数使用时，在调用初，会被替换成具体的类型实参；
比如：

fun  <T> List<T>.slice(incides:IntRange):List<T>
泛型形参声明 <T> 放在 fun 关键字之后，使用跟Java类似；

泛型的扩展属性

val <T> List<T>.penultimate: T
get() = this[size - 1]
注意： 不能声明泛型非扩展属性，不能在一个类的属性中存储多个不同类型的值；如要这么做，需考虑泛型类

1.3 泛型类、接口

与Java一样，在类声明时，可指定泛型，一旦声明之后，就可以在类的主体中像其他类型一样使用类型参数了；如：

interface List<T> {
operator fun get(index: Int) : T // ....
}
如果类继承自泛型类（接口），就得为基础类型的泛型形参提供具体类型实参或者另外的类型形参
比如：

interface List<T>
class StringList : List<String> // 具体类型实参
class MyList<T> : List<T>       // 泛型类型形参
1.4 类型参数约束

约束用来说明，只能使用什么样的类型实参;

上界约束：
在泛型类型具体的初始化中，其对应的类型实参，必须是 具体类型，或者子类型;
如下：（Java 使用 <T extends Number>）

fun <T: Number> List<T>.sum():T    // : Number 表示上界

// java
public  <T extends Number> void test(T t) {
}
注意：这里不涉及到 out、in 生产者，消费者关系，在参数位置，才涉及到，他们在后面；
我们也可以指定多个约束，如同Java 中 (<T extends Number & Appendable>)，但Kotlin语法有点奇怪，使用where

fun <T> List<T>.sum2():T where T : Number , T: Appendable {}
1.5 让类型形参非空

没有指定上界的类型形参将会使用 Any? 这个默认上界；
如下：

class Processor<T> {
fun process(value: T) {
value?.hashCode()
}
}

fun main(args: Array<String>) {
// 可空类型String?被用来替换T
val nullableString = Processor<String?>()
// 可传递null
nullableString.process(null)
}
如何不允许null呢，使用<T:Any>来确保类型T是非空类型；

class Processor<T : Any> {
fun process(value: T) {
value.hashCode()    // ?. 可以去掉了
}
}
2. 运行时的泛型：擦除和实化类型参数

Java中，泛型通过类型擦除实现；
在Kotlin可通过声明一个inline函数实现类型实参，不被擦除（Kotlin称实化）；

2.1 运行时的泛型：类型检查和转换

跟Java类似，Kotlin中的泛型在运行时也被擦除了；擦除是有好处的，这样保存在内存中类型信息就少了；

在Kotlin中，不允许使用没有指定类型实参的泛型类型，如果想判断一个变量是否是列表，可传递 * 星投影；

val list = listOf(1,2,3)
if(list is List<*>) {  // 星投影，类似Java的 <?>
}
使用 as 、 as? 进行转换：

fun printTest(c: Collection<*>) {
val intList = c as? kotlin.collections.List<Int> ?:
throw IllegalArgumentException("转换失败")
println(intList)
}
2.2 声明带实化类型参数的函数

因为泛型会被擦除，比如下面的代码是报错的：

fun <T> isA(value: Any) = value is T   // 不能确定T
但通过inline 内联函数，会把每一个的函数调用换成实际的代码调用，lambda 也是一样，并结合 reified 标记类型参数，上面的 value is T 就可以通过编译了

inline fun <reified T> isA(value: Any) = value is T
fun main(args: Array<String>) {
println(isA<Int>(1))
}
为什么实化只对内联函数有效

在内联函数中 可以写 value is T，普通类、普通函数中却不可以；
因为编译器将内联函数的字节码直接添加调用处，当每次调用带实化类型参数的函数时，编译器知道了类型实参的确切类型；而kotlin 中调用，不能省略类型实参, 上面的 不能写成 isA(1)，编译直接报错
注意 带 reified的内联函数不能再Java中调用，普通的 inline 函数可以；
3. 变型：泛型和子类型化

变型描述了拥有相同基础类型和不同类型实参（泛型）类型之间是如何关联的：如，List<String> 与 List<Any>;
变型的意义在于设计的API，不会以不方便的方式限制用户，也不会破坏用户所期望的类型安全；

3.1 为什么存在变型： 给函数传递实参

把一个 List<String 类型的变量传给 List<Any> 这样是允许的，如：

fun printContents(c: List<Any>) {
println(c.joinToString(""))
}
fun main(args: Array<String>) {
printContents(listOf("a","b"))
}
但是下面的代码

fun addContent(list: MutableList<Any>) {
list.add(1234)
}
// 下面的代码调用，明显有问题
val list = mutableListOf<String>("cccc")
addContent(list)    // 编译通不过
3.2 类、类型与子类型

变量的类型，规定了该变量的可能值，类型和类不是相同的概念；

非泛型类 类的名称可以直接当做类型使用；如：

var s : String
var s : String?
每个Kotlin的类都可以用于构造至少2种类型;
泛型类 情况复杂，要得到一个合法的类型，需要用类型实参替换（泛型）类的类型形参；
如：List不是一个类型（它是一个类），List<Int>、List<String?>是合法的类型；

子类型、超类型用来描述类型之间的关系，
如果需要类型A的值,都能够使用类型B的值（当做A的值），则类型B就称为类型A的子类型；超类型反之；
比如：Number 是 Int 的超类型，Int 是Number 的子类型；

这样的情况下，子类型与子类本质上是同一回事;
当涉及到泛型类型时，子类型与子类就有差异了；List<String> 是 List<Any> 的子类型吗？对于只读List接口，是的，而：MutableList<String> 当做Mutable<Any>的子类型是不安全的；
一个泛型类 - 如：MutableList 如果任意2种类型A和B，MutableList<A> 既不是MutableList<B>的子类型，也不是它的超类型，称为在该类型参数上是不变型的；

对于List,Kotlin 中的List接口表示的是只读集合，如果A是B的子类型，那List<A> 是 List<B> 的子类型，这样的类or接口被称为协变；

3.3 协变：保留子类型化关系

协变说明子类型化被保留了, 在Kotlin中，要声明类在某个类型参数上是协变的，在该类型参数的名称上添加 out 关键字；

interface Producter<out T> {
fun produce() : T
}
有什么用？看例子

open class Animal {   
fun feed() {
}
}

// 泛型类，接收Animal子类
class Herd<T : Animal> {
val size: Int get() = 20
operator fun get(i:Int) : T { ... }   // 操作符重载
}
// 具体动物
class Cat : Animal() {
fun clean() {}
}
// 喂方法，不好意思，我只认 Animal，不然他的子类
fun feedAll(animals : Herd<Animal>) {
for(i in 0 until animals.size) {
animals[i].feed()
}
}

fun takeCareOfCats(cats: Herd<Cat>) {
feedAll(cats)   // 期望 Herd<Animal>，很遗憾报错了    
}
怎么办？使用out关键字，改成协变

// 泛型类
class Herd<out T : Animal> {    
注意：这里的<out T: Animal> 与上面的提高的1.4 类型参数约束是不一样的,
类型约束,<> 在 fun 之后，这里是在方法 or 类的后面；
out 位置，表示这个类只能生产类型T的值，而不能消费他们；
在类成员的声明中类型参数的使用可分为in 位置 与 out位置

interface MyTranform<T> {
fun tranform(t: T): T   // 参数 t，in 位置，返回值 out位置
}
类的类型参数前的out 、in关键字约束了使用T的可能性，保证了对应子类型关系的安全性；
Out 关键字的2个含义

子类型化会被保留(Producer<Cat>) 是（Producer<Animal>）的子类型；
T 只能用在out位置（生产位置）
Kotlin 中的 List接口

// out 位置
public interface List<out E> : Collection<E> {...}

// T 在 in out 位置
public interface MutableList<E> : List<E>, MutableCollection<E> {...}
3.4 逆变：反转子类型化关系

逆变是协变的镜像：对于逆变类，它的子类型化关系与用作类型实参的类的子类型化关系是相反的；

// in 位置，表示消费
val anyComparator = Comparator<Any> {
e1, e2 ->
e1.hashCode() - e2.hashCode()
}
fun main(args: Array<String>) {
val strings = listOf("B", "A", "War")
println(strings.sortedWith(anyComparator))
}
如需在特定类型的对象比较，可使用能处理该类型或它的超类型的比较器；
Comparator<Any> 是 Comparator<String>的子类型，其中Any是String的超类型；不同类型之间的子类型关系 与 这些类型的比较器间的子类型化关系是相反的;

逆变 如果B是A的子类型，那么Consumer<A> 就是Consumer<B>的子类型，类型参数A与B交换了位置；协变：子类型化关系复制了它的类型实参的子类型化关系，逆变则反过来

in 关键字：对应类型的值是传递进来给这个类的方法，并且被方法消费；
协变	逆变	不变
Producer<T>	Consumer<in T>	MutableList<T>
类的子类型化保留：Producer<Cat> 是 Producer<Animal> 的子类型	子类型反转：Consumer<Animal> 是 Consumer<Cat>的子类型	没有子类型化
T只能在out位置	T只能在in位置	任何位置
表格：协变、逆变和不变

协变	逆变	不变
Producer<T>	Consumer<in T>	MutableList<T>
类的子类型化保留：Producer<Cat> 是 Producer<Animal> 的子类型	子类型反转：Consumer<Animal> 是 Consumer<Cat>的子类型	没有子类型化
T只能在out位置	T只能在in位置	任何位置
类可以在一个类型参数上协变，另一个参数上逆变，比如Function接口；

public interface Function1<in P1, out R> : Function<R> {
public operator fun invoke(p1: P1): R
}
3.5 使用点变型：在类型出现的地方

声明点变型：在类声明的时候，指定变型修饰符，这些修饰符会应用到所有类被使用的地方；
使用点变型：在Java中，使用(super, extends)通配符，处理变型，使用带类型参数时，指定是否可用这个类型参数的子或者超类替换；

Kotlin 声明点变型 vs Java 通配符
声明点变型更加简洁，指定一次变型修饰符，所有这个类的使用者，都会添加约束；Java 中使用：Function(? super T, ? extends R) 来创建约束；
如下代码片段（发现了区别，声明处变型是不是更简洁呢？）：

// Java 使用点变型
public interface Stream<T> {
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
}

// Kotlin 声明处变型
public interface Function1<in P1, out R> : Function<R> {
public operator fun invoke(p1: P1): R
}
Kotlin也支持使用点变型, 直接对应Java的限界通配符；

// (不变型) 从一个集合copy到另一个集合
fun <T> copyData(source: MutableList<T>, destination: MutableList<T>) {
for(item in source) {
destination += item
}
}

// 特定类型
fun <T : R, R> copyData2(source: MutableList<T>, destination: MutableList<R>) {
for (item in source) {
destination += item
}
}

// 使用点变型：给类型参数加上 变型修饰符 （out 投影）
fun <T> copyData3(source: MutableList<out T>, destination: MutableList<T>) {
for (item in source) {
destination += item
}
}

// 测试函数
fun main(args: Array<String>) {
// copyData 不变型
val source = mutableListOf("abc", "efg")
val destination = mutableListOf<String>()
copyData(source, destination)

    // copyData2 
    val source2 = mutableListOf("abc", "efg")
    var destination2 = mutableListOf<Any>()
    copyData2(source2, destination2)

    // copyData3 使用点变型
    val source3 = mutableListOf("better", "cc")
    var destination3 = mutableListOf<Any>()
    copyData3(source3, destination3)
    println(destination3)
}
类型投影
可以为类型声明中类型参数指定变型修饰符，包括：形参类型（方法上的），局部变量类型、函数返回类型等，这称作类型投影；投影即受限；
如上copyData3函数的 source不是一个常规的的MutableList，而是一个投影（受限）的MutableList。只能调用返回类型是泛型类型参数的那些方法，也就是out位置的方法；

3.6 * 星号投影

星号投影，用来表示不知道关于泛型实参的任何信息，跟Java的?问号通配符类似；
注意
MutableList<*> 和 MutableList<Any?> 不一样；
MutableList<T> 在T上是不变型的，

MutableList<Any?>包含的是任何类型的元素；
MutableList<*>是包含某种特定类型元素，但不知是哪个类型，所以不能写入；但可读取；
val list: MutableList<Any?> = mutableListOf('x', 1, "efg")
list.add(5)
val chars = mutableListOf('a', 'b', 'c')

val unkownElems: MutableList<*> = if (Random().nextBoolean()) list else chars
// unkownElems.add(12)  // 不能调用
println(unkownElems.get(0))
上例中，编译器将MutableList<*> 当做out投影的类型 MutableList<out Any?>，不能让她消费任何东西；
用处：
当类型实参的信息并不重要时，可使用星号投影的语法：

不需要使用任何在签名中引用类型参数的方法；
只是读取数据而不关系它的具体类型；
fun <T> getFirst(list: List<*>): T? {    // 星号投影
if (!list.isEmpty()) {
return list.first() as T
}
return null
}
fun <T> getFirst2(list: List<T>): T {
return list.first()
}



