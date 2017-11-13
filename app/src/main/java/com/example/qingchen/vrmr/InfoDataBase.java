package com.example.qingchen.vrmr;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * @author qingchen
 * @date 17-11-10
 */
@Database(entities = {NewsBean.class},version = 1)
public abstract class InfoDataBase extends RoomDatabase {
    public abstract InfoDao infoDao();
    private static InfoDataBase INSTANCE;
    private static final Object sLock = new Object();
    public static InfoDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        InfoDataBase.class, "newsbean")
                        .build();
            }
            return INSTANCE;
        }
    }
}
