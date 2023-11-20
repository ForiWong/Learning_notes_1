package com.wlp.myanjunote.customview.sample.easy.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wlp.myanjunote.R;
import com.wlp.myanjunote.customview.sample.easy.widget.ScaleView;

/**
 * 作者：chs on 2016/9/6 16:07
 * 邮箱：657083984@qq.com
 */
public class ScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        ScaleView scaleView = (ScaleView) findViewById(R.id.scale_view);
        //实际显示是跟传入的数值反序
        scaleView.setScales(new double[]{0.4, 0.3, 0.15, 0.15});
    }
}
