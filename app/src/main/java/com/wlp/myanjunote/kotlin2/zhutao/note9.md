

Kotlin 协程的核心竞争力在于：它能简化异步并发任务。
作为 Java 开发者，我们很清楚线程并发是多么的危险，写出来的异步代码是多么的难以维护。 
在Java中异步代码如果遇到复杂的一些业务，就会出现回调地狱。

4. 地狱到天堂：协程
   今天的主角是协程，上面的代码用协程应该写？很简单，核心就是三行代码：
   val user = getUserInfo()
   val friendList = getFriendList(user)
   val feedList = getFeedList(friendList)
   
   是不是简洁到了极致？这就是 Kotlin 协程的魅力：以同步的方式完成异步任务。
   
   4-1 使用协程的要点
   上面的代码之所以能写成类似同步的方式，关键还是在于那三个请求函数的定义。与普通函数不同的地方在于，它们都被 suspend 
   修饰，这代表它们都是：挂起函数。

// delay(1000L)用于模拟网络请求

//挂起函数
// ↓
suspend fun getUserInfo(): String {
withContext(Dispatchers.IO) {
delay(1000L)
}
return "BoyCoder"
}

//挂起函数
// ↓
suspend fun getFriendList(user: String): String {
withContext(Dispatchers.IO) {
delay(1000L)
}
return "Tom, Jack"
}

//挂起函数
// ↓
suspend fun getFeedList(list: String): String {
withContext(Dispatchers.IO) {
delay(1000L)
}
return "{FeedList..}"
}

那么，挂起函数到底是什么？

4-2 挂起函数
挂起函数(Suspending Function)，从字面上理解，就是可以被挂起的函数。suspend 有：挂起，暂停的意思。在这个语境下，也有
点暂停的意思。暂停更容易被理解，但挂起更准确。

挂起函数，能被挂起，当然也能恢复，他们一般是成对出现的。
我们来看看挂起函数的执行流程，注意动画当中出现的闪烁，这代表正在请求网络。
一定要多看几遍，确保没有遗漏其中的细节。

从上面的动画，我们能知道：

表面上看起来是同步的代码，实际上也涉及到了线程切换。
一行代码，切换了两个线程。
=左边：主线程
=右边：IO线程
每一次从主线程到IO线程，都是一次协程挂起(suspend)
每一次从IO线程到主线程，都是一次协程恢复(resume)。
挂起和恢复，这是挂起函数特有的能力，普通函数是不具备的。
挂起，只是将程序执行流程转移到了其他线程，主线程并未被阻塞。
如果以上代码运行在 Android 系统，我们的 App 是仍然可以响应用户的操作的，主线程并不繁忙，这也很容易理解。

挂起函数的执行流程我们已经很清楚了，那么，Kotlin 协程到底是如何做到一行代码切换两个线程的？
这一切的魔法都藏在了挂起函数的suspend关键字里。

5. suspend 的本质

   suspend 的本质，就是 CallBack。
   
   suspend fun getUserInfo(): String {
   withContext(Dispatchers.IO) {
   delay(1000L)
   }
   return "BoyCoder"
   }
   
   有的小伙伴要问了，哪来的 CallBack？明明没有啊。确实，我们写出来的代码没有 CallBack，但 Kotlin 的编译器检测到 
    suspend 关键字修饰的函数以后，会自动将挂起函数转换成带有 CallBack 的函数。
   如果我们将上面的挂起函数反编译成 Java，结果会是这样：
   //                              Continuation 等价于 CallBack
   //                                         ↓         
   public static final Object getUserInfo(Continuation $completion) {
   ...
   return "BoyCoder";
   }
   
   从反编译的结果来看，挂起函数确实变成了一个带有 CallBack 的函数，只是这个 CallBack 的真实名字叫 Continuation。
   毕竟，如果直接叫 CallBack 那就太 low，对吧？
   我们看看 Continuation 在 Kotlin 中的定义：
   public interface Continuation<in T> {
   public val context: CoroutineContext
   //      相当于 onSuccess     结果   
   //                 ↓         ↓
   public fun resumeWith(result: Result<T>)
   }
   
   对比着看看 CallBack 的定义：
   interface CallBack {
   void onSuccess(String response);
   }
   
   从上面的定义我们能看到：Continuation 其实就是一个带有泛型参数的  CallBack，除此之外，还多了一个 CoroutineContext，
   它就是协程的上下文。对于熟悉 Android 开发的小伙伴来说，不就是 context 嘛！也没什么难以理解的，对吧？
###**CPS转换同步代码编译成异步代码的过程
   以上这个从挂起函数转换成CallBack 函数的过程，被称为：CPS 转换(Continuation-Passing-Style Transformation)。
   看，Kotlin 官方用 Continuation 而不用 CallBack 的原因出来了：Continuation 道出了它的实现原理。当然，为了理解挂起函数，
   我们用 CallBack 会更加的简明易懂。
   
下面用动画演示挂起函数在 CPS 转换过程中，函数签名的变化：

这个转换看着简单，其中也藏着一些细节。
函数类型的变化
上面 CPS 转换过程中，函数的类型发生了变化：suspend ()->String 变成了 (Continuation)-> Any?。
这意味着，如果你在 Java 访问一个 Kotlin 挂起函数getUserInfo()，在 Java 看到 getUserInfo() 的类型会是：(Continuation)-> Object。
(接收 Continuation 为参数，返回值是 Object)
在这个 CPS 转换中，suspend () 变成 (Continuation) 我们前面已经解释了，不难。那么函数的返回值为什么会从：String变成Any?
挂起函数的返回值
挂起函数经过 CPS 转换后，它的返回值有一个重要作用：标志该挂起函数有没有被挂起。
这听起来有点绕：挂起函数，就是可以被挂起的函数，它还能不被挂起吗？是的，挂起函数也能不被挂起。
让我们来理清几个概念：
只要有 suspend 修饰的函数，它就是挂起函数，比如我们前面的例子：
suspend fun getUserInfo(): String {
withContext(Dispatchers.IO) {
delay(1000L)
}
return "BoyCoder"
}

当 getUserInfo() 执行到 withContext的时候，就会返回 CoroutineSingletons.COROUTINE_SUSPENDED 表示函数被挂起了。
现在问题来了，请问下面这个函数是挂起函数吗：
// suspend 修饰
// ↓
suspend fun noSuspendFriendList(user: String): String{
// 函数体跟普通函数一样
return "Tom, Jack"
}

答案：它是挂起函数。但它跟一般的挂起函数有个区别：它在执行的时候，并不会被挂起，因为它就是普通函数。当你写出这样的代码后，IDE 也会提示你，suspend 是多余的：

当 noSuspendFriendList() 被调用的时候，不会挂起，它会直接返回 String 类型："no suspend"。这样的挂起函数，你可以把它看作伪挂起函数
返回类型是 Any？的原因

由于 suspend 修饰的函数，既可能返回 CoroutineSingletons.COROUTINE_SUSPENDED，也可能返回实际结果"no suspend"，
甚至可能返回 null，为了适配所有的可能性，CPS 转换后的函数返回值类型就只能是 Any?了。


小结
suspend 修饰的函数就是挂起函数
挂起函数，在执行的时候并不一定都会挂起
挂起函数只能在其他挂起函数中被调用
挂起函数里包含其他挂起函数的时候，它才会真正被挂起

以上就是 CPS 转换过程中，函数签名的细节。
然而，这并不是 CPS 转换的全部，因为我们还不知道 Continuation 到底是什么。

6. CPS 转换
   Continuation 这个单词，如果你查词典和维基百科，可能会一头雾水，太抽象了。
   通过我们文章的例子来理解 Continuation，会更容易一些。
   首先，我们只需要把握住 Continuation 的词源 Continue 即可。Continue 是继续的意思，Continuation 则是继续下去要做的事情，接下来要做的事情。
   放到程序中，Continuation 则代表了，程序继续运行下去需要执行的代码，接下来要执行的代码 或者 剩下的代码。
   以上面的代码为例，当程序运行 getUserInfo() 的时候，它的 Continuation则是下图红框的代码：

Continuation 就是接下来要运行的代码，剩余未执行的代码。
理解了 Continuation，以后，CPS就容易理解了，它其实就是：将程序接下来要执行的代码进行传递的一种模式。
而CPS 转换，就是将原本的同步挂起函数转换成CallBack 异步代码的过程。这个转换是编译器在背后做的，我们程序员对此无感知。

也许会有小伙伴嗤之以鼻：这么简单粗暴？三个挂起函数最终变成三个 Callback 吗？
当然不是。思想仍然是 CPS 的思想，但要比 Callback 高明很多。
接下来，我们一起看看挂起函数反编译后的代码是什么样吧。前面铺垫了这么多，全都是为了下一个部分准备的。

9. 结尾
   回过头看线程和协程之间的关系：
   线程

线程是操作系统级别的概念
我们开发者通过编程语言(Thread.java)创建的线程，本质还是操作系统内核线程的映射
JVM 中的线程与内核线程的存在映射关系，有“一对一”，“一对多”，“M对N”。JVM 在不同操作系统中的具体实现会有差别，“一对一”是主流
一般情况下，我们说的线程，都是内核线程，线程之间的切换，调度，都由操作系统负责
线程也会消耗操作系统资源，但比进程轻量得多
线程，是抢占式的，它们之间能共享内存资源，进程不行
线程共享资源导致了多线程同步问题
有的编程语言会自己实现一套线程库，从而能在一个内核线程中实现多线程效果，早期 JVM 的“绿色线程” 就是这么做的，这种线程被称为“用户线程”

有的人会将线程比喻成：轻量级的进程。
协程

Kotlin 协程，不是操作系统级别的概念，无需操作系统支持
Kotlin 协程，有点像上面提到的“绿色线程”，一个线程上可以运行成千上万个协程
Kotlin 协程，是用户态的(userlevel)，内核对协程无感知
Kotlin 协程，是协作式的，由开发者管理，不需要操作系统进行调度和切换，也没有抢占式的消耗，因此它更加高效
Kotlin 协程，它底层基于状态机实现，多协程之间共用一个实例，资源开销极小，因此它更加轻量
Kotlin 协程，本质还是运行于线程之上，它通过协程调度器，可以运行到不同的线程上

挂起函数是 Kotlin 协程最关键的内容，一定要理解透彻。
