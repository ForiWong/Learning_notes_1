package com.wlp.myanjunote.kotlin2.newClazz

class Person(val firstName:String, val lastName: String) {

    override fun equals(o: Any?): Boolean{
        val otherPerson = o as? Person ?: return false

        return otherPerson.firstName == firstName &&
               otherPerson.lastName == lastName
    }

    override fun hashCode(): Int = firstName.hashCode()*37 + lastName.hashCode()

}