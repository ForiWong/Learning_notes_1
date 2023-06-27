package com.wlp.myanjunote.kotlin2.object4

interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

/**
 * kotlin代码调用：Person.fromJSON()
 * java代码调用：Person.Companion.fromJSON();
 *
 */
class Person(val name: String) {

    companion object: JSONFactory<Person>{
        override fun fromJSON(jsonText: String): Person {
            return Person(jsonText)
        }
    }

}