package com.example.kaldijava.cafe.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.kaldijava.R;
import com.example.kaldijava.cafe.http.Http_API;
import com.example.kaldijava.cafe.http.Menu;
import com.example.kaldijava.cafe.http.RES;
import com.example.kaldijava.cafe.utils.MenuAdp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import marty_library.ration.com.library.utils.BaseActivity;
import marty_library.ration.com.library.utils.BaseCallback;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.kaldijava.cafe.utils.Props.FCM_TOKEN_KEY;
import static com.example.kaldijava.cafe.utils.Props.HIDDEN_MAX_COUNT;
import static com.example.kaldijava.cafe.utils.Props.HIDDEN_MAX_TIME;


public class MainActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView list;

    Http_API.Apis apis = new Http_API().getApis();

    MenuAdp adapter;
    @BindView(R.id.hidden_btn)
    TextView hiddenBtn;

    long Hidden_Timer = 0;
    int Hidden_count = 0;

    BaseCallback<RES<Object>> callback = new BaseCallback<RES<Object>>(this, new BaseCallback.MartyCall<RES<Object>>() {
        @Override
        public void onResponse(Call<RES<Object>> call, Response<RES<Object>> response) {

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        adapter = new MenuAdp(this,callback);
        list.setAdapter(adapter);
        // 서버에서 메뉴를 불러오기
        apis.getMenus().enqueue(new BaseCallback<RES<ArrayList<Menu>>>(this, new BaseCallback.MartyCall<RES<ArrayList<Menu>>>() {
            @Override
            public void onResponse(Call<RES<ArrayList<Menu>>> call, Response<RES<ArrayList<Menu>>> response) {
                if (response.body().getCode() == 200) {
                    adapter.arrayList = response.body().getResult();
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("메뉴를 불러오기를 실패했습니다.");
                }
            }
        }));

    }


    @OnClick(R.id.hidden_btn)
    public void onViewClicked() {

        long current_timemiillis = Calendar.getInstance().getTimeInMillis();

        // 2초를 초과한경우
        if (current_timemiillis >= (Hidden_Timer + HIDDEN_MAX_TIME) || Hidden_count > HIDDEN_MAX_COUNT){
            Hidden_Timer = current_timemiillis;
            Hidden_count = 0;
            return;

        }else {
            if (++Hidden_count == HIDDEN_MAX_COUNT){
                if (getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE).getString(FCM_TOKEN_KEY,"").isEmpty()){
                    FirebaseApp.initializeApp(this);

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        showToast("관리자 등록 실패!");
                                        return;
                                    }
                                    String token = task.getResult().getToken();
                                    getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE).edit().putString(FCM_TOKEN_KEY,token).commit();
                                    sendToken();
                                }
                            });
                }else{
                    sendToken();
                }


            }
        }


    }

    void sendToken(){
        apis.sendToken(getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE).getString(FCM_TOKEN_KEY,""))
                .enqueue(new BaseCallback<RES<String>>(MainActivity.this, new BaseCallback.MartyCall<RES<String>>() {
                    @Override
                    public void onResponse(Call<RES<String>> call, Response<RES<String>> response) {
                        if (response.body().getCode() == 200)
                            showToast("관리자 등록 성공!");
                        else
                            showToast("관리자 등록 실패!");

                    }
                }));
    }
}
