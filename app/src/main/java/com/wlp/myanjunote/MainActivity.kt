package com.wlp.myanjunote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myanjunote.customview.CustomViewActivity

class MainActivity : AppCompatActivity() {
    lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.view)

        // 传入我们上面创建的AppBlockCanaryContext
        //BlockCanary.install(this, AppBlockCanaryContext()).start()

        //SystemClock.sleep()

        findViewById<TextView>(R.id.to_custom_view).setOnClickListener {
            startActivity(Intent(this, CustomViewActivity::class.java))
        }
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