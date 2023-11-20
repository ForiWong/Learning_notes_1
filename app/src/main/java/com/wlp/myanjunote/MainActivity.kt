package com.wlp.myanjunote

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myanjunote.customview.CustomViewActivity
import com.wlp.myanjunote.customview.Utils
import com.wlp.myanjunote.customview.sample.LineChart
import com.wlp.myanjunote.customview.sample.easy.MainActivity
import com.wlp.myanjunote.ot.scrollconflict.ScrollConflictActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

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

        findViewById<TextView>(R.id.to_ot).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        initView()
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

    }

    private fun initView() {
        val options = chart.getOptions()
        //X轴
        val xAxisOptions = options.xAxisOptions
        xAxisOptions.isEnableLine = false
        xAxisOptions.textColor = Color.parseColor("#999999")
        xAxisOptions.textSize = Utils.dp2px(12f)
        xAxisOptions.textMarginTop = Utils.dp2px(12f).toInt()
        xAxisOptions.textMarginBottom = Utils.dp2px(12f).toInt()
        xAxisOptions.isEnableGrid = false
        xAxisOptions.isEnableRuler = false

        //Y轴
        val yAxisOptions = options.yAxisOptions
        yAxisOptions.isEnableLine = false
        yAxisOptions.textColor = Color.parseColor("#999999")
        yAxisOptions.textSize = Utils.dp2px(12f)
        yAxisOptions.textMarginLeft = Utils.dp2px(12f).toInt()
        yAxisOptions.textMarginRight = Utils.dp2px(12f).toInt()
        yAxisOptions.gridColor = Color.parseColor("#999999")
        yAxisOptions.gridWidth = Utils.dp2px(0.5f)
        val dashLength = Utils.dp2px(8f)
        yAxisOptions.gridPathEffect = DashPathEffect(floatArrayOf(dashLength, dashLength / 2), 0f)
        yAxisOptions.isEnableRuler = false

        //数据
        val dataOptions = options.dataOptions
        dataOptions.pathColor = Color.parseColor("#79EBCF")
        dataOptions.pathWidth = Utils.dp2px(1f)
        dataOptions.circleColor = Color.parseColor("#79EBCF")
        dataOptions.circleRadius = Utils.dp2px(3f)
        chart.setOnClickListener {
            initChartData()
        }
    }

    private fun initChartData() {
        val random = 1000
        val list = mutableListOf<LineChart.ChartBean>()
        list.add(LineChart.ChartBean("05-01", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-02", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-03", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-04", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-05", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-06", Random.nextInt(random)))
        list.add(LineChart.ChartBean("05-07", Random.nextInt(random)))
        chart.setData(list)

        //文本
        val text = list.joinToString("\n") {
            "x : ${it.xAxis}  y:${it.yAxis}"
        }
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}