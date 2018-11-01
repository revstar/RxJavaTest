package com.revstar.rxjavatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ThrottleActivity extends AppCompatActivity {

    Button btn;
    public static final String TAG="ThrottleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttle);
        btn=findViewById(R.id.btn);
        RxView.clicks(btn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG,"开始订阅");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG,"发送请求事件");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"出错");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"事件完成");
                    }
                });

    }
}
