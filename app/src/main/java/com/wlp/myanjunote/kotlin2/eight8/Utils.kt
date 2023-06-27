package com.wlp.myanjunote.kotlin2.eight8


//类型参数是一个判断式的函数，若为true则执行
fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) sb.append(element)
    }
    return sb.toString()
}


/**
 * 声明一个返回函数的函数
 * 参数是 delivery: Delivery
 * 返回值 (Order) -> Double
 */
fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}


//使用扩展函数，但还不够开放
fun List<SiteVisit>.averageDurationFor(os: OS) = filter { it.os == os }
    .map { it.duration }
    .average()

//使用高阶函数来增强它的扩展性（把条件下放给调用者）
fun List<SiteVisit>.getAverageDuration(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()

fun <T> Collection<T>.joinToString( // T 泛型函数
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.joinToString2( // T 泛型函数
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: ((T) -> String)? = null
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(element) ?: element.toString()
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}

//这里是声明一个可空的函数类型
fun foo(callback: (() -> Unit)?) {
    //
    if (callback != null) {
        //....
    }
}