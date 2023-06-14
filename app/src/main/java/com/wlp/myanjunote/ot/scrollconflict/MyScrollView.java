package com.wlp.myanjunote.ot.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 解决此问题的前提是 ：熟练掌握 事件分发机制的原理。
 解决此问题的方法步骤为：
 1）外部拦截：在自定义LinearLayout中，重写onInterceptTouchEvent，拦截所有下滑的事件，释放所有上滑的事件。
 2）内部拦截：在子组件,也就是自定义ScrollView中，重写OnTouchEvent，判定是不是当前scroll到了顶部，如果是到了
 顶部，那么就允许父组件进行拦截。如果是滑到中间位置，不是顶部，就不允许父组件进行拦截，事件就会在子组件这里
 消耗掉，父组件就不会执行onTouchEvent.

 最终就达成了上面说的预期效果；
 **/
public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    //todo 当到了0的时候再滑，MotionEvent.ACTION_Cancel
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int scrollY = getScrollY();//纵向滑动的顶端Y轴坐标值
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (scrollY == 0) {//如果已经scroll到了顶端
                    //允许父View进行事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);//是否禁止父组件拦截事件. false表示不禁止，也就是允许
                } else {
                    //禁止父View进行事件拦截
                    getParent().requestDisallowInterceptTouchEvent(true);//true表示禁止，不允许
                }
                break;
        }
        Log.d("ScrollView......onInterceptTouchEvent", ev.getAction() + " -> MyScrollView:onTouchEvent：" + scrollY );
        return super.onTouchEvent(ev);

    }
}