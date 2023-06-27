package com.wlp.myanjunote.kotlin2.delegateby

import kotlin.properties.Delegates

class User {
    var myName: String by Delegates.observable("初始值") { prop, old, new ->
        println("prop: ${prop.name}, 旧值：$old -> 新值：$new")
    }
}