package com.example.qingchen.vrmr;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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
