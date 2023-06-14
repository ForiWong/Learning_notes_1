package com.wlp.myanjunote.ot.scrollconflict;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wlp.myanjunote.R;

/**
 * 如何解决View的事件冲突？举个开发中遇到的例子？
 * 常见开发中事件冲突的有ScrollView与RecyclerView的滑动冲突、RecyclerView内嵌同时滑动同一方向。
 * 滑动冲突的处理规则：
 * - 对于由于外部滑动和内部滑动方向不一致导致的滑动冲突，可以根据滑动的方向判断谁来拦截事件。
 * - 对于由于外部滑动方向和内部滑动方向一致导致的滑动冲突，可以根据业务需求，规定何时让外部View拦截事件，何时由内部View拦截事件。
 * - 对于上面两种情况的嵌套，相对复杂，可同样根据需求在业务上找到突破点。
 *
 * 滑动冲突的实现方法：
 * - 外部拦截法：指点击事件都先经过父容器的拦截处理，如果父容器需要此事件就拦截，否则就不拦截。具体方法：
 * 需要重写父容器的onInterceptTouchEvent方法，在内部做出相应的拦截。
 * - 内部拦截法：指父容器不拦截任何事件，而将所有的事件都传递给子容器，如果子容器需要此事件就直接消耗，
 * 否则就交由父容器进行处理。具体方法：需要配合requestDisallowInterceptTouchEvent方法。
 * */
public class ScrollConflictActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict);
    }
}