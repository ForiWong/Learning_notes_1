package com.wlp.myanjunote.kotlin2.zhutao

class Developer2(var name: String?) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val developer = o as Developer?
        return if (name != null) name == developer!!.name else developer!!.name == null
    }

    override fun hashCode(): Int {
        return if (name != null) name!!.hashCode() else 0
    }

    override fun toString(): String {
        return "Developer{" + "name='" + name + '}'.toString()
    }
}