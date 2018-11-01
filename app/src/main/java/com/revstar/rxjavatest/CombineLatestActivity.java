package com.revstar.rxjavatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class CombineLatestActivity extends AppCompatActivity {

    public static final String TAG="CombineLatestActivity";
    EditText name,job,age;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_latest);
        name=findViewById(R.id.name);
        job=findViewById(R.id.job);
        age=findViewById(R.id.age);
        submit=findViewById(R.id.submit);

        Observable<CharSequence>nameObservable= RxTextView.textChanges(name).skip(1);
        Observable<CharSequence>jobObservable= RxTextView.textChanges(job).skip(1);
        Observable<CharSequence>ageObservable= RxTextView.textChanges(age).skip(1);
        
        Observable.combineLatest(nameObservable, ageObservable, jobObservable, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) throws Exception {
                boolean isUserNameValid= !TextUtils.isEmpty(name.getText());
                boolean isUserAgeValid=!TextUtils.isEmpty(age.getText());
                boolean isUserJobValid=!TextUtils.isEmpty(job.getText());
                return isUserAgeValid&&isUserNameValid&&isUserJobValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e(TAG,"提交按钮是否可以点击"+aBoolean);
                submit.setEnabled(aBoolean);
            }
        });

    }
}
