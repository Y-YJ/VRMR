package com.example.qingchen.vrmr.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * @author qingchen
 * @date 17-11-10
 */
@Dao
public interface InfoDao {
    @Query("SELECT * FROM newsbean")
    LiveData<List<NewsBean>> getAll();

    @Query("SELECT * FROM newsbean")
    List<NewsBean> getAl();

    @Insert(onConflict = REPLACE)
    void save(NewsBean info);

    @Query("SELECT * FROM newsbean WHERE ctime = :ctime")
    LiveData<NewsBean> load(String ctime);

    @Insert
    void insertAll(List<NewsBean> list);
    @Delete
    void delete(NewsBean list);
}
