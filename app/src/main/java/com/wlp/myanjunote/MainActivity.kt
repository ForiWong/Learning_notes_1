package com.wlp.myanjunote

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.moduth.blockcanary.BlockCanary

class MainActivity : AppCompatActivity() {
    val view = findViewById<View>(R.id.view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 传入我们上面创建的AppBlockCanaryContext
        //BlockCanary.install(this, AppBlockCanaryContext()).start()

        //SystemClock.sleep()
    }

    fun doSomething(v: View?){
        v?.id
    }

    fun test(){
        view.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                doSomething(v)
            }
        })

        view.setOnClickListener(View.OnClickListener { v: View? ->
            doSomething(v)
        })

        view.setOnClickListener({ v: View? ->
            doSomething(v)
        })

        view.setOnClickListener({ v ->
            doSomething(v)
        })

        view.setOnClickListener({ it ->
            doSomething(it)
        })

        view.setOnClickListener({
            doSomething(it)
        })

        view.setOnClickListener() {
            doSomething(it)
        }

        view.setOnClickListener {
            doSomething(it)
        }

    }}