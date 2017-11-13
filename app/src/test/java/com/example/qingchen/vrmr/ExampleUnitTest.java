package com.example.qingchen.vrmr;

import android.test.mock.MockContext;

import com.example.qingchen.vrmr.DataBase.InfoDataBase;
import com.example.qingchen.vrmr.DataBase.NewsBean;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSqlLit(){
        if (MyApplication.getContext()==null){
           System.out.println("--->isNull");
        }
        InfoDataBase infoDataBase=InfoDataBase.getInstance(new MockContext());
        NewsBean newsBean=new NewsBean();
        newsBean.setPicUrl("sss");
        newsBean.setCtime("sbb");
        infoDataBase.infoDao().save(newsBean);
    }
}