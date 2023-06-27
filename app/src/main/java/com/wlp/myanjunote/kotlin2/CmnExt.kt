package com.wlp.myanjunote.kotlin2

import java.text.SimpleDateFormat

fun date() = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())

fun log(msg: String) = println("${date()} - [${Thread.currentThread().name}]  $msg")

