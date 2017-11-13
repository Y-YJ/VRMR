package com.example.qingchen.vrmr;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Dao;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.Component;

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
