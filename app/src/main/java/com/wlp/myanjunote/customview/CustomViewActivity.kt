package com.wlp.myanjunote.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.ondraw.OnDrawActivity
import com.wlp.myanjunote.customview.xfermode.XfermodeActivity

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

        findViewById<TextView>(R.id.on_draw).setOnClickListener {
            startActivity(Intent(this, OnDrawActivity::class.java))
        }

        findViewById<TextView>(R.id.xfer_mode).setOnClickListener {
            startActivity(Intent(this, XfermodeActivity::class.java))
        }
    }
}