package com.wlp.myanjunote.kotlin2.withinterface6

class MyUser(val name: String) {
    //声明了一个可变属性并且在每次setter访问时执行额外的代码
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent())
            field = value
        }

    /**
      field 访问支持字段
     */

    var counter: Int = 0
        private set  //不能在类外步修改这个属性
        //访问器的可见性默认和属性的可见性相同的，可以在get或set前放置可见性修饰符

    fun addWord(word: String){
        counter += word.length
    }
}