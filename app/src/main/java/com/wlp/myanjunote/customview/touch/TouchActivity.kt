package com.wlp.myanjunote.customview.touch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wlp.myanjunote.R

class TouchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)

        findViewById<View>(R.id.view).setOnClickListener {
            Toast.makeText(this@TouchActivity, "点击了！", Toast.LENGTH_SHORT).show()
        }
    }
}