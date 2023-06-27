package com.wlp.myanjunote.kotlin2

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.wlp.myanjunote.kotlin2.databinding.ActivityMainBinding

import kotlinx.coroutines.GlobalScope//coroutines 协程，协同程序，并发 GlobalScope 全局范围
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import com.wlp.myanjunote.kotlin2.newClazz.User
import com.wlp.myanjunote.kotlin2.lam5.LambdaDemo

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //lambda
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.fab.setOnClickListener { Toast.makeText(this, "haha", Toast.LENGTH_LONG).show() }
        binding.fab.setOnClickListener { view -> Toast.makeText(this, "haha", Toast.LENGTH_LONG).show() }

        //testKt()
        doTest()

        val message :String
        if (0==0){
            message = "SUC"
        }else{
            message = "FAL"
        }
        //message = "DEF" //这里是会报错的

        println("Hello, $message")//字符串模版

        println("Hello, \$message") //对$ 进行转义

        println("Hello, ${message.length}") //{ } 内部是表达式

        println("Hello, ${if (message.length > 3) "abc" else "def"}") //{ } 内部是表达式 是可以嵌套 双引号的 "

        println(User("myName"))
        println(User("myName").toString())
        println(User("myName") == User("myName"))

        val  wlp = User("wlp")
        println(wlp.toString())
        println(wlp.copy().toString())

        println(LambdaDemo().test(10,
            {num1: Int, num2: Int ->  num1 * num2}))


        postCompu(100){ println(42)}
        postCompu(1000, {println(43)})

        val listener = View.OnClickListener{ view ->
            val text = when (view.id){
                R.id.fab -> "FirstButton"
                else -> "Other"
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }

        //object定义一个匿名内部类
        binding.fab.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    println("click--1")
                }
            }
        )

        //sam转换为 lambda
        binding.fab.setOnClickListener(View.OnClickListener { v: View? ->
            println("${v?.id} click}")
        })

        // Kotlin 的 Lambda 表达式是不需要 SAM Constructor的，所以它也可以被删掉
        binding.fab.setOnClickListener({ v: View? ->
            println("${v?.id} click}")
        })

        // Kotlin 支持类型推导，所以 View? 可以被删掉：
        binding.fab.setOnClickListener({ v ->
            println("${v?.id} click}")
        })

        //当 Kotlin Lambda 表达式只有一个参数的时候，它可以被写成 it
        binding.fab.setOnClickListener({ it ->
            println("${it?.id} click}")
        })

        //it -> 也是可以被省略的
        binding.fab.setOnClickListener({
            println("${it?.id} click}")
        })

        //当 Kotlin Lambda 作为函数的最后一个参数时，Lambda 可以被挪到外面
        binding.fab.setOnClickListener(){
            println("${it?.id} click}")
        }

        //当 Kotlin 只有一个 Lambda 作为函数参数时，() 可以被省略
        binding.fab.setOnClickListener{
            println("${it?.id} click}")
        }

    }


    fun postCompu(delay:Int, comp:Runnable){

    }

    //协程
    /**
     * GlobalScope.launch
     * 开启一个全局协程，生命周期伴随着App。
     * 非阻塞，不会阻塞当前线程。
     * 一般不建议使用
     *
     * runBlocking
     * 开启一个顶级协程，并阻塞当前线程
     * 存在性能问题，只推荐在测试中使用
     */
    private fun testKt() {
        log("--start--")
        GlobalScope.launch {
            delay(2000L)
            log("hello GlobalScope")
        }
        log("--end--")
        Thread.sleep(10_000L)
    }

    private fun testKt2() {
        log("--start--")
        runBlocking {
            delay(2000L)
            log("hello runBlocking")
        }
        log("--end--")
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}