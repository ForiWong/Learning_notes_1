package com.wlp.myanjunote.customview.touchdrag

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.wlp.myanjunote.R

class DragToCollectLayout(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {
    private var dragStarter = OnLongClickListener { v ->
        val imageData = ClipData.newPlainText("name", v.contentDescription)
        //可以跨进程拖拽
        ViewCompat.startDragAndDrop(v, imageData, DragShadowBuilder(v), null, 0)
    }
    private var dragListener: OnDragListener = CollectListener()

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<ImageView>(R.id.avatarView).setOnLongClickListener(dragStarter)
        findViewById<ImageView>(R.id.logoView).setOnLongClickListener(dragStarter)
        findViewById<LinearLayout>(R.id.collectorLayout).setOnDragListener(dragListener)
    }

    inner class CollectListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DROP -> if (v is LinearLayout) {
                    val textView = TextView(context)
                    textView.textSize = 16f
                    textView.text = event.clipData.getItemAt(0).text
                    v.addView(textView)
                }
            }
            return true
        }
    }
}
