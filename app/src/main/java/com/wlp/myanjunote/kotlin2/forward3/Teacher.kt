package com.wlp.myanjunote.kotlin2.forward3

class Teacher(name: String, var age: Int, school: String) {
    var name: String? = null
        get() = if (field == null) "" else field

    var school: String? = null
        get() = if (field == null) "" else field

    init {
        this.name = name
        this.school = school
    }
}
