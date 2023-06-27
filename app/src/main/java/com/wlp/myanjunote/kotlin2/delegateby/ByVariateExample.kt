package com.wlp.myanjunote.kotlin2.delegateby

// 定义包含属性委托的类
class ByVariateExample {
    var p: String by Delegate("defg")
}