package com.wlp.myanjunote.customview.sample.easy.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wlp.myanjunote.R;
import com.wlp.myanjunote.customview.sample.easy.entity.PieDataEntity;
import com.wlp.myanjunote.customview.sample.easy.widget.HollowPieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：chs on 2016/9/6 16:07
 * 邮箱：657083984@qq.com
 */
public class HollowPieChartActivity extends AppCompatActivity {
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_hpllow_chart);
        HollowPieChart pieChart = (HollowPieChart) findViewById(R.id.chart);
        List<PieDataEntity> dataEntities = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            PieDataEntity entity = new PieDataEntity("name" + i, i + 1, mColors[i]);
            dataEntities.add(entity);
        }
        pieChart.setDataList(dataEntities);

        pieChart.setOnItemPieClickListener(new HollowPieChart.OnItemPieClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(HollowPieChartActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
