package com.wlp.myanjunote.customview.layoutprocess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wlp.myanjunote.R

class ProcessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process)
    }
}

/**
com.wlp.myanjunote D/Vg1ViewGroup: onMeasure
com.wlp.myanjunote D/K2View: onMeasure
com.wlp.myanjunote D/Vg2ViewGroup: onMeasure
com.wlp.myanjunote D/K3View: onMeasure
com.wlp.myanjunote D/K1View: onMeasure

com.wlp.myanjunote D/Vg1ViewGroup: onMeasure
com.wlp.myanjunote D/K2View: onMeasure
com.wlp.myanjunote D/Vg2ViewGroup: onMeasure
com.wlp.myanjunote D/K3View: onMeasure
com.wlp.myanjunote D/K1View: onMeasure

com.wlp.myanjunote D/Vg1ViewGroup: onLayout
com.wlp.myanjunote D/K2View: layout
com.wlp.myanjunote D/K2View: onLayout
com.wlp.myanjunote D/Vg2ViewGroup: onLayout
com.wlp.myanjunote D/K3View: layout
com.wlp.myanjunote D/K3View: onLayout
com.wlp.myanjunote D/K1View: layout
com.wlp.myanjunote D/K1View: onLayout

com.wlp.myanjunote D/K2View: onDraw
com.wlp.myanjunote D/K3View: onDraw
com.wlp.myanjunote D/K1View: onDraw
**/