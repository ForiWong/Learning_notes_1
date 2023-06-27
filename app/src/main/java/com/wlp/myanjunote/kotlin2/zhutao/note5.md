Kotlin Jetpack 实战｜05. Kotlin 泛型
https://juejin.cn/post/6856553487598256141

泛型
泛型的不变性、协变、逆变
使用处型变：使用处协变、使用处逆变

##---------------------------------------

本文以故事的形式介绍 Kotlin 泛型及其不变性，声明处型变，使用处型变，最后再搭配一个实战环节，将泛型应用到我们的 Demo 当中来。

正文
1. 遥控器的故事：泛型

女朋友：好想要一个万能遥控器啊。
我：要不我教你用 Kotlin 的泛型实现一个吧！
女朋友：切，又想忽悠我学 Kotlin。[白眼]
我：真的很简单，保证你一看就会。

1-1 泛型类

我：这是一个万能遥控器，它带有一个泛型参数

//          类的泛型参数(形参)
//               ↓
class Controller<T>() {
    fun turnOn(obj: T){ ... }
    fun turnOff(obj: T){ ... }
}

我：它用起来也简单，想控制什么，把对应的泛型传进去就行，就跟选模式一样：

//                    电视机作为泛型实参
//                           ↓
val tvController: Controller<TV> =  Controller<TV>()
val tv = TV()
// 控制电视机
tvController.turnOn(tv)
tvController.turnOff(tv)

//                      电风扇作为泛型实参
//                             ↓
val fanController: Controller<Fan> =  Controller<Fan>()
val fan = Fan()
// 控制电风扇
fanController.turnOn(fan)
fanController.turnOff(fan)

借助 Kotlin 的顶层函数，Controller 类甚至都可以省掉，直接用泛型函数：
1-2 泛型函数
//     函数的泛型参数
//   ↓              ↓
fun <T> turnOn(obj: T){ ... }
fun <T> turnOff(obj: T){ ... }

泛型函数用起来也简单：
// 控制电视
val tv = TV()
turnOn<TV>(tv)
turnOff<TV>(tv)

// 控制风扇
val fan = Fan()
turnOn<Fan>(fan)
turnOff<Fan>(fan)


女朋友：我知道怎么用啦！是不是这样？

val boyFriend = BoyFriend()
turnOff<BoyFriend>(boyFriend)


我：……


2. 招聘的故事：泛型的不变性(Invariant)

女朋友：我想招几个大学生做兼职，你推荐几个大学吧。
我：好嘞，不过我要通过 Kotlin 泛型来给你推荐。
女朋友：呃……刚才你讲的泛型还挺简单，这次有什么新花样吗？
我：你看下去就知道了。
我：先来点准备工作：

// 学生
open class Student()
// 女学生
class FemaleStudent: Student()

// 大学
class University<T>(val name: String) {
// 往外取，代表招聘
fun get(): T { ... }
fun put(student: T){ ... }
}


我：你的招聘需求可以用这样的代码描述：

//                                  注意这里
// 女朋友需要一个大学（变量声明）           ↓
lateinit var university: University<Student>

//                      注意这里
// 我随便推荐一个大学         ↓
university = University<Student>("某大学")
val student: Student = university.get()// 招聘


女朋友：原来 Kotlin 也没那么难……
女朋友：能赋值一个"女子大学"吗？
我：不行，会报错。

//                                  注意这里
//                                     ↓
lateinit var university: University<Student>
//                      这是报错的原因
//                           ↓
university = University<FemaleStudent>("女子大学")
val student: Student = university.get()

// 编译器报错！！
/*
Type mismatch.
Required:   University<Student>
Found:      University<FemaleStudent>
*/


女朋友：什么鬼。。。
我：虽然 Student 和 FemaleStudent 之间是父子关系，但是 University<Student> 和 University<FemaleStudent> 之间没有任何关系。
这叫泛型的不变性。
女朋友：这不合理！女子大学招聘出来的学生，难道就不是学生？
我：招聘当然符合逻辑，但别忘了 University 还有一个 put 方法。
我：你怎么防止别人把一个男学生放到女子大学里去？
我：让我们看看如果可以将“女子大学”当作“普通大学”用，会出现什么问题：

//              声明的类型是：普通大学，然而，实际类型是：女子大学。
//                             ↓                      ↓
var university: University<Student> = University<FemaleStudent>("女子大学")

val maleStudent: Student = Student()
// 男学生被放进女子大学！不合理。
university.put(maleStudent)


女朋友：明白了，原来这就是泛型不变性的原因，确实能避免不少麻烦。

// 默认情况下，编译器只允许这么做
// 声明的泛型参数与实际的要一致
↓                     ↓
var normalUniversity: University<Student> = University<Student>

                                  ↓                           ↓
var wUniversity: University<FemaleStudent> = University<FemaleStudent>


3. 搞定招聘：泛型的协变(Covariant)

女朋友：如果我把 University 类里面的 put 方法删掉，是不是就可以用“女子大学”赋值了？这样就不用担心把男学生放到女子大学的问题了。
我：这还不够，还需要加一个关键字 out 告诉编译器：我们只会从 University 类往外取，不会往里面放。这时候，University<FemaleStudent>
就可以当作 University<Student> 的子类。
我：这叫做泛型的协变。

open class Student()
class FemaleStudent: Student()

//              看这里
//                ↓
class University<out T>(val name: String) {
fun get(): T { ... }
}


女朋友：我试试，果然好了！

// 不再报错
var university: University<Student> = University<FemaleStudent>("女子大学")
val student: Student = university.get()


我：你不来写代码真浪费了。


4. 填志愿的故事：泛型的逆变(Contravariant)

女朋友：我妹妹刚高考完，马上要填志愿了，你给推荐个大学吧。
我：咱刚看过泛型协变，要不你试试自己解决这个填志愿的问题？正好 University 里有个 put 方法，你就把 put 当作填志愿就行了。
女朋友：那我依葫芦画瓢试试…… 给我妹妹报一个女子大学。

open class Student()
class FemaleStudent: Student()

class University<T>(val name: String) {
fun get(): T { ... }
// 往里放，代表填志愿
fun put(student: T){ ... }
}

val sister: FemaleStudent = FemaleStudent()
val university: University<FemaleStudent> = University<FemaleStudent>("女子大学")
university.put(sister)//填报女子大学


女朋友：完美！
我：厉害。
女朋友：能不能再报一个普通综合大学？
我：不行，你忘记泛型不变性了吗？

val sister: FemaleStudent = FemaleStudent()
//          报错原因：声明类型是：女子大学      赋值的类型是：普通大学
//                               ↓                        ↓  
val university: University<FemaleStudent> = University<Student>("普通大学")
university.put(sister)

// 报错
/*
Type mismatch.
Required:   University<FemaleStudent>
Found:      University<Student>
*/


女朋友：我妹能报女子大学，居然不能报普通的综合大学？这不合理吧！
我：你别忘了 University 还有一个 get 方法吗？普通综合大学 get 出来的可不一定是女学生。
女朋友：哦。那我把 get 方法删了，再加个关键字？
我：对。删掉 get 方法，再加一个关键字：in 就行了。它的作用是告诉编译器：我们只会往 University 类里放，不会往外取。这时候，
University<Student> 就可以当作 University<FemaleStudent> 的子类。
我：这其实就叫做泛型的逆变，它们的继承关系反过来了。

//              看这里
//                ↓
class University<in T>(val name: String) {
fun put(student: T){ ... }
}

val sister: FemaleStudent = FemaleStudent()
// 编译通过
val university: University<FemaleStudent> = University<Student>("普通大学")
university.put(sister)


女朋友：泛型还挺有意思。
我：上面提到的协变和逆变。它们都是通过修改 University 类的泛型声明实现的，所以它们统称为：声明处型变，这是 Kotlin 才有的概念，Java 中没有。


5. 使用处型变(Use-site Variance)

女朋友：万一 University 是第三方提供的，我们无法修改，怎么办？能不能在不修改 University 类的前提下实现同样的目的？
我：可以，这就要用到使用处型变了。他们也分为：使用处协变，使用处逆变。

open class Student()
class FemaleStudent: Student()

// 假设 University 无法修改
class University<T>(val name: String) {
fun get(): T { ... }
fun put(student: T){ ... }
}

5-1 使用处协变

我：在泛型的实参前面增加一个 out 关键字，代表我们只会从 University 往外取，不会往里放。这么做就实现了 使用处协变。

//                                         看这里
//                                           ↓
fun useSiteCovariant(university: University<out Student>) {
val femaleStudent: Student? = university.get()

    // 报错: Require Nothing? found Student?
    // university.put(femaleStudent)
}


女朋友：这也挺容易理解的。那使用处逆变呢？加个 in？

5-2 使用处逆变

我：对。在泛型的实参前面增加一个 in 关键字，代表我们只会从 University 往里放，不会往外取。这么做就实现了 使用处逆变。

//                                               看这里
//                                                 ↓
fun useSiteContravariant(universityIn: University<in FemaleStudent>) {
universityIn.put(FemaleStudent())

    // 报错: Require FemaleStudent? found Any?
    // val femaleStudent: FemaleStudent? = universityIn.get()
}

女朋友：思想是一样的。
女朋友：如果是从 University 招聘学生，就是往外取，这种情况下就是协变，可以用 University<FemaleStudent> 替代 University<Student>，
因为女子大学取出来的女学生，和普通大学取出来的学生，都是学生。
女朋友：如果是 University 要招生，就是往里放，这种情况下，就只能用 University<Student> 替代 University<FemaleStudent>，因为普通
大学的招生范围更广，女子大学能接收的学生，普通大学也接收。
我：你总结的真好。顺便提一句：Kotlin 的使用处型变，还有个名字叫：类型投影(Type Projections)，这名字真烂。

以上代码的具体细节可以看我这个 GitHub Commit。
5-3 Kotlin 和 Java 对比

我：既然你 Kotlin 泛型理解起来毫无压力，那我再给你给加个餐，对比一下 Java 的使用处型变。
女朋友：呃…… Java 是啥玩意？
我：没事，你就当看个乐呵。

使用处协变使用处逆变
Kotlin University<out Student>
       University<in FemaleStudent>

Java University<? extends Student>
     University<? super FemaleStudent>

我：是不是简单明了？
女朋友：还是 Kotlin 的容易理解：out 代表只能往外取(get)，in代表只能往里放(put)。
我：没错。
女朋友：对比起来，Java 的表达方式真是无力吐槽。(-_-)

//     Java 这辣鸡协变语法
//             ↓
University<? extends Student> covariant = new University<FemaleStudent>("女子大学");
Student student = covariant.get();
// 报错
covariant.put(student);

//     Java 这辣鸡逆变语法
//             ↓
University<? super FemaleStudent> contravariant = new University<Student>("普通大学");
contravariant.put(new FemaleStudent())
// 报错
Student s = contravariant.get();

以上代码的具体细节可以看我这个 GitHub Commit。

6. Kotlin 泛型实战

我：这里有一个 Kotlin 的 Demo，要不你来看看有哪些地方能用泛型优化的？
女朋友：过分了啊！你让我学 Kotlin 就算了，还想让我帮你写代码？
女朋友：你来写，我来看。
我：呃……听领导的。

6-1 泛型版本的 apply 函数

我：这是上一个章节里的代码，这个 apply 函数其实可以用泛型来简化，让所有的类都能使用。


//  替代               替代              替代
//   ↓                 ↓                 ↓
fun User.apply(block: User.() -> Unit): User{
block()
return this
}

user?.apply { this: User ->
...
username.text = this.name
website.text = this.blog
image.setOnClickListener { gotoImagePreviewActivity(this) }
}


我：使用泛型替代以后的 apply 函数就是这样：


//   泛型              泛型           泛型
//   ↓  ↓              ↓              ↓
fun <T> T.apply(block: T.() -> Unit): T{
block()
return this
}


女朋友：Kotlin 官方的 apply 函数也是这么实现的吗？
我：几乎一样，它只是多了个 contract，你暂时还不懂。
女朋友：呃……还有其他例子吗？

作者：朱涛的自习室
链接：https://juejin.cn/post/6856553487598256141
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

Kotlin 的声明处型变和使用处型变它们分别有哪些优势和劣势？

