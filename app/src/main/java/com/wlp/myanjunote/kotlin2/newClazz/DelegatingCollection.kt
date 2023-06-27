package com.wlp.myanjunote.kotlin2.newClazz

//by 类委托关键字 省去大量的样本代码
class DelegatingCollection<T> (innerList:Collection<T> = ArrayList<T>()) : Collection<T> by innerList{

}