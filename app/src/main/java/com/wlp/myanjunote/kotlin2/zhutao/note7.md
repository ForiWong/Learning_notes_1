作者：朱涛的自习室
链接：https://juejin.cn/post/6859172099680894989
来源：稀土掘金


委托类
委托属性
自定义委托属性
使用实例
##---------------------------------------
1. 前言
   委托(Delegation)，可能是 Kotlin 里最容易被低估的特性。
   提到 Kotlin，大家最先想起的可能是扩展，其次是协程，再要不就是空安全，委托根本排不上号。但是，在一些特定场景中，委托的作用是无比犀利的。
   本文将系统介绍 Kotlin 的委托，然后在实战环节中，我会尝试用委托 + 扩展函数 + 泛型，来封装一个功能相对完整的 SharedPreferences 框架。

2. 前期准备

将 Android Studio 版本升级到最新
将我们的 Demo 工程 clone 到本地，用 Android Studio 打开：
github.com/chaxiu/Kotl…
切换到分支：chapter_07_delegate
强烈建议各位小伙伴小伙伴跟着本文一起实战，实战才是本文的精髓

3. 委托类(Class Delegation)
   委托类，通过关键字 by 可以很方便的实现语法级别的委托模式。看个简单例子：
   interface DB {
   fun save()
   }

class SqlDB() : DB {
override fun save() { println("save to sql") }
}

class GreenDaoDB() : DB {
override fun save() { println("save to GreenDao") }
}

//               参数  通过 by 将接口实现委托给 db
//                ↓            ↓
class UniversalDB(db: DB) : DB by db

fun main() {
   UniversalDB(SqlDB()).save()
   UniversalDB(GreenDaoDB()).save()
}

/*
输出：
save to sql
save to GreenDao
*/

这种委托模式在我们实际编程中十分常见，UniversalDB 相当于一个壳，它提供数据库存储功能，但并不关心它怎么实现。具体是用 Sql
还是 GreenDao，传不同的委托对象进去就行。
以上委托类的写法，等价于以下 Java 代码：
class UniversalDB implements DB {
DB db;
public UniversalDB(DB db) { this.db = db; }
//  手动重写接口，将 save 委托给 db.save()
@Override//            ↓
public void save() { db.save(); }
}

各位可不要小看这个小小的by，上面的例子中，接口只有一个方法，所以 Java 看起来也不怎么麻烦，但是，当我们想委托的接口方法很
多的时候，这个by能极大的减少我们的代码量。
我们看一个复杂点的例子，假设我们想对MutableList进行封装，并且增加一个方法，借助委托类的 by，几行代码就搞定了：
//                               这个参数才是干活的，所有接口实现都被委托给它了，实现这一切只需要  ↓
//                                       ↓                                            
class LogList(val log: () -> Unit, val list: MutableList<String>) : MutableList<String> by list{
fun getAndLog(index: Int): String {
log()
return get(index)
}
}
     
如果是在 Java 里，那就不好意思了，呵呵，我们必须 implements 这么多方法：

想想要写那么多的重复代码就心累，是不是？

##组合大于继承

注：Effective Java 里面提到过：组合优于继承(Favor composition over inheritance)，所以在 Java 中，我们也会尽可
能多使用接口(interface)。借助 Kotlin 提供的委托类，我们使用组合类会更方便。
结合上面的例子，如果需要实现的接口有很多个，委托类真的可以帮我们省下许多的代码量。

4. 委托属性(Property Delegation)
   委托属性，它和委托类虽然都是通过 by 来使用的，但是它们完全不是一回事。委托类委托出去的是它的接口实现；委托属性，委托
   出去的是属性的 getter，setter。我们前面经常提到的 val text = by lazy{}，其实就是将 text 的 getter 委托给了 lazy{}。
   val text: String = by lazy{}
   // 它的原理其实跟下面是一样的

// 语法层面上是等价的哈，实际我们不能这么写
val text: String
get() { lazy{} }
     
5. 自定义委托属性
###自定义属性的使用！！！！
   Kotlin 的委托属性用起来很神奇，那我们怎么根据需求实现自己的属性委托呢？看看下面的例子：
   class Owner {
   var text: String = “Hello”
   }
        
   我想为上面的 text 属性提供委托，应该怎么做？请看下面例子的注释：
   class StringDelegate(private var s: String = "Hello") {
   //                           对应 text 所处的类                 对应 text 的类型
   //      ⚡                         👇                               ↓
   operator fun getValue(thisRef: Owner, property: KProperty<*>): String {
   return s
   }
   //                           对应 text 所处的类                        对应 text 的类型
   //      ⚡                         👇                                      ↓
   operator fun setValue(thisRef: Owner, property: KProperty<*>, value: String) {
   s = value
   }
   }

//     👇
class Owner {
↓     
var text: String by StringDelegate()
}
     
小结：

var —— 我们需要提供getValue和setValue
val —— 则只需要 getValue
operator —— 是必须的，这是编译器识别委托属性的关键。注释中已用 ⚡ 标注了。
property —— 它的类型一般固定写成 KProperty<*>
value —— 的类型必须是委托属性的类型，或者是它的父类。也就是说例子中的 value: String 也可以换成 value: Any。注释中已用↓标注了。
thisRef —— 它的类型，必须是属性所有者的类型，或者是它的父类。也就是说例子中的thisRef: Owner 也可以换成 thisRef: Any。注释中已用 👇 标注了。

以上是委托属性中比较重要的细节，把握好这些细节，我们写自定义委托就没什么问题了。
6. 实战
   又到了我们熟悉的实战环节，让我们来做点有意思的事情吧。
7. 热身
   前面的章节我们实现过一个简单的 HTML DSL，一起看看如何使用委托来优化之前的代码吧！如果仔细看的话，各位应该能发现这一处代码看着非常不爽，明显的模版代码：
   class IMG : BaseElement("img") {
   var src: String
   get() = hashMap["src"]!!
   set(value) {
   hashMap["src"] = value
   }
   //            ↑
   //      看看这重复的模板代码
   //            ↓
   var alt: String
   get() = hashMap["alt"]!!
   set(value) {
   hashMap["alt"] = value
   }
   }
        
   要是能用委托属性来写就好了：
   // 这代码看着真舒服
   class IMG : BaseElement("img") {
   var src: String by hashMap
   var alt: String by hashMap
   }
        
   按照前面讲的自定义委托属性的要求，我们很容易就能写出这样的代码：
   //                                                  对应 IMG 类
   //                                                      👇
   operator fun HashMap<String, String?>.getValue(thisRef: IMG, property: KProperty<*>): String? =
   get(property.name)

operator fun HashMap<String, String>.setValue(thisRef: IMG, property: KProperty<*>, value: String) =
put(property.name, value)
     
按照前面讲的，thisRef 的类型可以是父类，所以写成这样问题也不大：
//                                                  变化在这里
//                                                      👇
operator fun HashMap<String, String?>.getValue(thisRef: Any, property: KProperty<*>): String? =
get(property.name)

operator fun HashMap<String, String>.setValue(thisRef: Any, property: KProperty<*>, value: String) =
put(property.name, value)
     
改成 thisRef: Any 的好处是，以后在任意类里面的 String 属性，我们都可以用这种方式去委托了，比如：
class Test {
var src: String by hashMap
var alt: String by hashMap
}
     
思考题1
请问：上面的 thisRef: Any 改成 thisRef: Any? 是否会更好？为什么？
思考题2
官方其实有 map 委托的实现，官方的写法好在哪里？(答案藏在 GitHub Demo 代码注释里。)
8. 委托属性 + SharedPreferences
   在上一章 扩展函数，我们使用高阶函数+扩展函数，简化了 SharedPreferences，但那个用法仍然不够简洁，那时候我们是这么用的，说实话，还不如我们 Java 封装的 PreferenceUtils 呢。
   // MainActivity.kt
   private val preference: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
   getSharedPreferences(SP_NAME, MODE_PRIVATE)
   }

// 读取缓存
private val spResponse: String? by lazy(LazyThreadSafetyMode.NONE) {
preference.getString(SP_KEY_RESPONSE, "")
}

private fun display(response: String?) {
// 更新缓存
preference.edit { putString(SP_KEY_RESPONSE, response) }
}
     
假如我们能这么做呢：
private var spResponse: String by PreferenceString(SP_KEY_RESPONSE, "")

// 读取，展示缓存
display(spResponse)

// 更新缓存
spResponse = response
     
这就很妙了!
这样一个委托属性其实也很容易实现对不对？
operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
return prefs.getString(name, "") ?: default
}

operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
prefs.edit().apply()
}
     
为了让它支持默认值，commit()，我们加两个参数：
class PreferenceString(
private val name: String,
private val default:String ="",
private val isCommit: Boolean = false,
private val prefs: SharedPreferences = App.prefs) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return prefs.getString(name, default) ?: default
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        with(prefs.edit()){
            putString(name, value)
            if (isCommit) {
                commit()
            } else {
                apply()
            }
        }
    }
}
     
这也很简单，对不对？
以上代码仅支持 String 类型，为了让我们的框架支持不同类型的参数，我们可以引入泛型：
class PreDelegate<T>(
private val name: String,
private val default: T,
private val isCommit: Boolean = false,
private val prefs: SharedPreferences = App.prefs) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getPref(name, default) ?: default
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        value?.let {
            putPref(name, value)
        }
    }

    private fun <T> getPref(name: String, default: T): T? = with(prefs) {
        val result: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type is not supported")
        }

        result as? T
    }

    private fun <T> putPref(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type is not supported")
        }

        if (isCommit) {
            commit()
        } else {
            apply()
        }
    }
}
     
以上所有代码都在 GitHub，欢迎 star fork：github.com/chaxiu/Kotl…。
Delegation 测试代码的细节看这个 GitHub Commit
Delegation HTML 的细节看这个 GitHub Commit
Delegation SharedPreferences 的细节看这个 GitHub Commit
思考题3：
以上代码仅支持了几个基础类型，能否扩展支持更多的类型？
思考题4:
以这样的封装方式，下次我们想为 PreDelegate 增加其他框架支持，比如腾讯的 MMKV，应该怎么做？
思考题5:
有没有更优雅的方式封装 SharedPreferences？

9. 结尾：
委托，分为委托类，委托属性
委托类，可以方便快捷的实现 委托模式，也可以配合接口来实现类组合
委托属性，既可以提高代码的复用率，还能提高代码的可读性。
委托类，它的原理其实就是编译器将委托者，被委托者两者对应的接口方法绑定。
委托属性，它的原理是因为编译器会识别特定的 getter，setter，如果它们符合特定的签名要求，就会被解析成 Delegation
