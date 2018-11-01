package com.revstar.rxjavatest.api;

import com.revstar.rxjavatest.bean.Iciba;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Create on 2018/9/29 11:51
 * author revstar
 * Email 1967919189@qq.com
 */
public interface GetRequest_Iciba {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Observable<Iciba> getCall();
}
