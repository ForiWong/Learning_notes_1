package com.wlp.myanjunote.kotlin2.delegateby

class Site(val map: Map<String, Any?>) {
    val name: String by map
    val url: String  by map
}
