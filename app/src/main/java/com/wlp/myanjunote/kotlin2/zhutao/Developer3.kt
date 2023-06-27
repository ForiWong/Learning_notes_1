package com.wlp.myanjunote.kotlin2.zhutao

//数据类的使用
data class Developer3(var name: String)

/*
上面这一行简单的代码，完全能替代前面我们的写的那一大堆模板 Java 代码，甚至额外多出了一些功能。
可以看到，Kotlin 的数据类不仅为我们提供了 getter、setter、equals、hashCode、toString，还额外的帮我们实现了 copy 方法！
这也体现了 Kotlin 的简洁特性。

//序列化的坑
对于上面的情况，由于 Gson 最初是为 Java 语言设计的序列化框架，并不支持 Kotlin 不可为空、默认值这些特性，从而导致原本不可为空的
属性变成null，原本应该有默认值的变量没有默认值。
对于这种情，市面上已经有了解决方案:
kotlinx.serialization.moshi

*/