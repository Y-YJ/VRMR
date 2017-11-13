package com.example.qingchen.vrmr.mainactivity.module;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.qingchen.vrmr.DataBase.NewsBean;
import com.example.qingchen.vrmr.mainactivity.repository.InfoRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @author qingchen
 * @date 17-11-10
 */

public class ProfileViewModel extends ViewModel {
    private InfoRepository infoRepository;
    @Inject
    public ProfileViewModel(InfoRepository infoRepository){
        this.infoRepository= infoRepository;
    }
    public LiveData<List<NewsBean>> getInfos(){
        if (infoRepository==null){
            Log.e("---->","isNull");
        }
        return  infoRepository.getInfo();
    }
}
