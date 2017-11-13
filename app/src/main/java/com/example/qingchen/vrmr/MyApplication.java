package com.example.qingchen.vrmr;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * @author qingchen
 * @date 17-11-12
 */

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Room.databaseBuilder(this,InfoDataBase.class,"NewsBean").build();
        context=this;
    }
}
