package com.wlp.myanjunote.customview.sample.easy.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wlp.myanjunote.R;
import com.wlp.myanjunote.customview.sample.easy.entity.DoubleBarEntity;
import com.wlp.myanjunote.customview.sample.easy.widget.DoubleBarChart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chs
 */
public class DoubleBarCharActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_bar_char);
        DoubleBarChart doubleBarChart = findViewById(R.id.barchart);
        List<DoubleBarEntity> list = new ArrayList<>();
        list.add(new DoubleBarEntity("1月", 1000, 2000));
        list.add(new DoubleBarEntity("2月", 1000, 1500));
        list.add(new DoubleBarEntity("3月", 1000, 1600));
        list.add(new DoubleBarEntity("4月", 1000, 1800));
        list.add(new DoubleBarEntity("5月", 1000, 2400));
        list.add(new DoubleBarEntity("6月", 1000, 1200));
        list.add(new DoubleBarEntity("7月", 1000, 1300));
        list.add(new DoubleBarEntity("8月", 1000, 1500));
        list.add(new DoubleBarEntity("9月", 1000, 1700));
        list.add(new DoubleBarEntity("10月", 1000, 2000));
        doubleBarChart.setData(list, Color.parseColor("#6FC5F4"), Color.parseColor("#78DA9F"));
        doubleBarChart.setOnItemBarClickListener(new DoubleBarChart.OnItemBarClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(DoubleBarCharActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
