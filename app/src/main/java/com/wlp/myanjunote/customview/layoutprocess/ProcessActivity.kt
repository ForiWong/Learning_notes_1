package com.wlp.myanjunote.customview.layoutprocess

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myanjunote.R
import com.wlp.myanjunote.customview.sample.CircleProgress
import com.wlp.myanjunote.customview.sample.GoodProgressView
import com.wlp.myanjunote.customview.sample.MyTextView
import java.util.*

class ProcessActivity : AppCompatActivity() {
    private lateinit var progress1: GoodProgressView
    private lateinit var progress2: GoodProgressView
    var mRandom = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process)

        val mt = findViewById<MyTextView>(R.id.myTextView)
        ObjectAnimator.ofFloat(mt, "progress", 0f, 1f).setDuration(5000).start()

        progress1 = findViewById(R.id.good_progress_view1)
        progress2 = findViewById(R.id.good_progress_view2)
        progress1.setProgressValue(90)
        progress1.setColors(intArrayOf(Color.RED, Color.MAGENTA))
        progress2.setProgressValue(60)
        progress2.setColors(intArrayOf(Color.RED, Color.MAGENTA))

        val mCircleProgress = findViewById<CircleProgress>(R.id.circle_progress)
        mCircleProgress.setOnClickListener(View.OnClickListener {
            mCircleProgress.setValue(mRandom.nextFloat() * mCircleProgress.getMaxValue());
        })
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