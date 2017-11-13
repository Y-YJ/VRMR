package com.example.qingchen.vrmr;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.qingchen.vrmr.DataBase.InfoDataBase;
import com.example.qingchen.vrmr.DataBase.NewsBean;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.qingchen.vrmr", appContext.getPackageName());
    }
    @Test
    public void testSqlLit(){
        InfoDataBase infoDataBase=InfoDataBase.getInstance(InstrumentationRegistry.getTargetContext());
        NewsBean newsBean=new NewsBean();
        newsBean.setPicUrl("ssssss");
        newsBean.setCtime("sbbbb");
        infoDataBase.infoDao().save(newsBean);
    }
}
