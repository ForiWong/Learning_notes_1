package com.wlp.myanjunote.ot.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

//MyParentView.java 即 外围的自定义LinearLayout，由它进行外部拦截
public class MyParentView extends LinearLayout {
    private int mMove;//滑动距离
    private int yDown, yMove;//按下、滑动
    private int i = 0;

    public MyParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        // 默认不拦截,这个变量只能放在方法内部作为局部变量，因为如果作为全局变量的话，子组件内部有可
        // 能划不动;至于是啥原因，我还没想明白
        boolean isIntercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = y;
                break;

            //重写onInterceptTouchEvent，拦截所有下滑的事件，释放所有上滑的事件。
            case MotionEvent.ACTION_MOVE:
                yMove = y;
                if (yMove - yDown < 0) {// 上滑动作直接放行
                    isIntercept = false;
                // 因为先会走子ScrollView的onTouchEvent() -> requestDisallowInterceptTouchEvent,后面才会到这里 yMove - yDown >0
                } else if (yMove - yDown > 0) { // 下滑动作拦截住，不往下发
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        Log.d("1..Parent......onInterceptTouchEvent", ev.getAction() + "->isIntercept:" + isIntercept + ":" + yMove + ":"+yDown);

        return isIntercept;
    }

    /**
     * 重写onTouchEvent获取屏幕事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();// 取得Y轴坐标值
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = y;
                break;
            case MotionEvent.ACTION_MOVE:
                yMove = y;
                if ((yMove - yDown) > 0) {// 如果是向下拉，因为向下拉的话，yMove总是比yDown要大
                    mMove = yMove - yDown;// 计算出拖动的距离
                    i += mMove;//记录一共拖动了多长距离，累加的  ///不对吧
                    layout(getLeft(), getTop() + mMove, getRight(), getBottom() + mMove);// 调用layout进行重新布局，只是改变布局的相对于父组件的位置
                }
                break;
            case MotionEvent.ACTION_UP:
                layout(getLeft(), getTop() - i, getRight(), getBottom() - i);//滑动-->布局
                i = 0;
                break;
        }
        Log.d("2..Parent......", event.getAction() + " -> onTouchEvent" + ":" + yMove + ":"+yDown + ":"+ mMove);
        return true;
    }
}