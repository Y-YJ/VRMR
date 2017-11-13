package com.example.qingchen.vrmr.mainactivity;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qingchen.vrmr.DataBase.NewsBean;
import com.example.qingchen.vrmr.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * @author qingchen
 * @date 17-11-12
 */

public class MyViewHolder extends BaseViewHolder<NewsBean> {
    ImageView imageView;
    TextView title;
    TextView content;
    public MyViewHolder(ViewGroup itemView) {
        this(itemView, R.layout.mainactivity_info_item);
    }

    public MyViewHolder(ViewGroup parent, int res) {
        super(parent, res);
        imageView=$(R.id.imageView);
        title=$(R.id.textView);
        content=$(R.id.textView2);
    }

    @Override
    public void setData(NewsBean newsBean){
        Glide.with(getContext()).load(newsBean.getPicUrl()).placeholder(R.drawable.ic_launcher_background).centerCrop().into(imageView);
        title.setText(newsBean.getTitle());
        content.setText(newsBean.getCtime());
    }
}
