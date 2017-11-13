package com.example.qingchen.vrmr.mainactivity;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.example.qingchen.vrmr.DataBase.NewsBean;
import com.example.qingchen.vrmr.R;
import com.example.qingchen.vrmr.mainactivity.module.DaggerMainActivityComponent;
import com.example.qingchen.vrmr.mainactivity.module.ProfileViewModel;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author qingchen
 */

public class MainActivity extends FragmentActivity {

    @Inject
    public ProfileViewModel viewModel;
    public EasyRecyclerView easyRecyclerView;
    private RecyclerArrayAdapter<NewsBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerMainActivityComponent.create().inject(this);

        viewModel.getInfos().observe(this, new Observer<List<NewsBean>>() {
            @Override
            public void onChanged(@Nullable List<NewsBean> infoBean) {
                adapter.clear();
                adapter.addAll(infoBean);
            }
        });
        if (adapter==null){
            adapter=new RecyclerArrayAdapter<NewsBean>(this) {
                @Override
                public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                    return new MyViewHolder(parent);
                }
            };
        }

        easyRecyclerView=findViewById(R.id.easy);
        easyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        easyRecyclerView.setAdapter(adapter);
    }
}
