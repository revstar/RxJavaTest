package com.revstar.rxjavatest;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.revstar.rxjavatest.api.GetRequest_Iciba;
import com.revstar.rxjavatest.bean.Iciba;
import com.revstar.rxjavatest.utils.GsonUtil;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    //设置最大重连次数
    private int maxConnectCount=10;
    //当前已重连次数
   private int currentRetryCount=0;
   //重连等待时间
    private int waitRetryTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Logger初始化
        Logger.addLogAdapter(new AndroidLogAdapter());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GetRequest_Iciba request_iciba = retrofit.create(GetRequest_Iciba.class);

        Observable<Iciba> observable = request_iciba.getCall();
        observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "发生异常" + throwable.toString());
                        if (throwable instanceof IOException) {
                            Log.d(TAG, "属于IO异常，需要重试");
                            if (currentRetryCount<maxConnectCount){
                                currentRetryCount++;
                                Log.d(TAG,"重连次数"+currentRetryCount);
                                //设置等待时间
                                waitRetryTime=1000+currentRetryCount*1000;
                                Log.d(TAG,"等待时间="+waitRetryTime);
                                return Observable.just(1).delay(waitRetryTime, TimeUnit.SECONDS);
                            }else {
                                return Observable.error(new Throwable("重连次数超过设置次数="+currentRetryCount));
                            }
                        }else {
                            return Observable.error(new Throwable("发生了非网络异常"));
                        }

                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Iciba>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Iciba iciba) {

                        Log.d(TAG,"发送成功");
                        Logger.json(GsonUtil.toJson(iciba));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"发送失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
