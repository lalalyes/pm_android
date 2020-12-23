package com.fudan.project.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fudan.project.HttpClient;
import com.fudan.project.R;
import com.fudan.project.data.model.User;
import com.google.gson.Gson;

import java.io.IOException;


import okhttp3.Response;

public class MyInformationActivity extends AppCompatActivity {
    TextView name, workNum, selfIntro;
    Context context;
    static final String TAG = "MyInformationActivity";
    User user = new User();
    private static final int COMPLETED = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        setContentView(R.layout.activity_my_information);
        name = findViewById(R.id.name);
        workNum = findViewById(R.id.work_number);
        selfIntro = findViewById(R.id.self_intro);
        getMyInfo();
    }
    public void getMyInfo(){
        String url = "my_info";
        HttpClient client = HttpClient.getInstance(context);

        client.get(url, new HttpClient.MyCallback() {
            @Override
            public void success(Response res) throws IOException {
                if (res.code()==200){
                    String response = res.body().string();
                    Log.w(TAG, response);
                    Gson gson = new Gson();
                    user = gson.fromJson(response,User.class);
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void failed(IOException e) {
                Log.w(TAG, e.fillInStackTrace());
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED){
                render();
            }
        }

        private void render() {
            name.setText(user.getUsername());
            workNum.setText(user.getWorkNumber());
            selfIntro.setText(user.getIntroduction());
        }
    };

}
