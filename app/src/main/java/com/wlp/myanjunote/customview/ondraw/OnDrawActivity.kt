package com.wlp.myanjunote.customview.ondraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.dp

class OnDrawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_draw)

        val testView = findViewById<TestView>(R.id.testView)

        findViewById<Button>(R.id.btn).setOnClickListener {//改变控件的大小
            val params = testView.layoutParams
            params?.apply {
                params.width = 200f.dp.toInt()
                params.height = 300f.dp.toInt()
            }
            testView.layoutParams = params
        }
    }
}