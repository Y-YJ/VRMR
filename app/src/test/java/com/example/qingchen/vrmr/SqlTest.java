package com.example.qingchen.vrmr;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author qingchen
 * @date 17-11-12
 */

public class SqlTest {
    private Context context;
    @Before
    public void setUp() throws Exception {
        context=MyApplication.getContext();
    }
    @Test
    public void testSqlLit(){
        if (context==null){
            System.out.println("--->isNull");
        }
        InfoDataBase infoDataBase=InfoDataBase.getInstance(context);
        NewsBean newsBean=new NewsBean();
        newsBean.setPicUrl("sss");
        newsBean.setCtime("sbb");
        infoDataBase.infoDao().save(newsBean);
    }
}
