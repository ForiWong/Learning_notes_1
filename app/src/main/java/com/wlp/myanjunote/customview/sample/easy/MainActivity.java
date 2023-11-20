package com.wlp.myanjunote.customview.sample.easy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wlp.myanjunote.R;
import com.wlp.myanjunote.customview.sample.easy.activity.BarAndLineActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.BarChartActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.CombineChartActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.DoubleBarCharActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.HollowPieChartActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.HollowPieChartNewActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.LineChartActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.PercentActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.PieChartActivity;
import com.wlp.myanjunote.customview.sample.easy.activity.ScaleActivity;
import com.wlp.myanjunote.customview.sample.easy.entity.MainBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<MainBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initData();
        setRecyclerView();
    }

    private void initData() {
        mList.add(new MainBean("线性图", new Intent(this, LineChartActivity.class)));
        mList.add(new MainBean("柱状图", new Intent(this, BarChartActivity.class)));
        mList.add(new MainBean("双柱状图", new Intent(this, DoubleBarCharActivity.class)));
        mList.add(new MainBean("饼状图", new Intent(this, PieChartActivity.class)));
        mList.add(new MainBean("空心饼", new Intent(this, HollowPieChartActivity.class)));
        mList.add(new MainBean("空心饼1", new Intent(this, HollowPieChartNewActivity.class)));
        mList.add(new MainBean("直线比例", new Intent(this, ScaleActivity.class)));
        mList.add(new MainBean("组合图", new Intent(this, CombineChartActivity.class)));
        mList.add(new MainBean("线和柱", new Intent(this, BarAndLineActivity.class)));
        mList.add(new MainBean("比例图", new Intent(this, PercentActivity.class)));
    }

    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(mList);
        adapter.setOnItemClickListener(position -> {
            startActivity(mList.get(position).getIntent());
        });
        mRecyclerView.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<MainBean> list = new ArrayList<>();
        private OnItemClickListener mListener;

        public MyAdapter(List<MainBean> data) {
            list = data;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.mTextView.setText(list.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv_name);
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
