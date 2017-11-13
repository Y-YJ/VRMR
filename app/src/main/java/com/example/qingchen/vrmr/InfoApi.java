package com.example.qingchen.vrmr;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author qingchen
 * @date 17-11-10
 */

public interface InfoApi {
    /**
     * 获取信息
     * @return
     */
    @GET("/startup/?key=7fed97d2186ea83c78d3e4fd0b58ab56&num=30")
    Observable<InfoBean> getInfo();
}
