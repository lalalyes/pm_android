package com.fudan.project.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fudan.project.HttpClient;
import com.fudan.project.R;
import com.fudan.project.data.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class PasswordFormActivity extends AppCompatActivity {

    EditText originalpw, newpw, againpw;
    Button btn;
    Context context;
    String opw, npw, apw ="";
    static  final  String TAG = "PasswordFormActivity";

    private static final int COMPLETED = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        context = getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_form);
        originalpw = findViewById(R.id.originPassword);
        newpw = findViewById(R.id.newPassword);
        againpw = findViewById(R.id.newPasswordAgain);
        btn = findViewById(R.id.changePasswordBtn);


        btn.setOnClickListener(changePassword());

    }




    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED){
                originalpw.setText("");
                newpw.setText("");
                againpw.setText("");
                Toast.makeText(context,"密码修改成功！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler error = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED){
                Toast.makeText(context,"操作失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener changePassword() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opw = originalpw.getText().toString();
                npw = newpw.getText().toString();
                apw = againpw.getText().toString();
                if (opw==""||npw==""||apw==""|| npw.length()< 5|| 12 < npw.length()){
                    Toast.makeText(v.getContext(),"请输入合法密码 ！",Toast.LENGTH_SHORT).show();;
                } else if (!npw.equals(apw)){
                    Toast.makeText(v.getContext(),"请确保确认新密码一致 ！",Toast.LENGTH_SHORT).show();;
                }else {
                    doChange();
                }
            }


            private void doChange() {
                String url = "changePassword";
                HttpClient client = HttpClient.getInstance(context);
                HashMap<String,String> param = new HashMap<>();
                param.put("oldPassword",opw);
                param.put("newPassword",npw);

                client.post(url,param,new HttpClient.MyCallback() {
                    @Override
                    public void success(Response res) throws IOException {

                        Log.d(TAG, res.body().string());
                        if (res.code()==200){
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handler.sendMessage(msg);
                        }else {
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            error.sendMessage(msg);
                        }
                    }
                    @Override
                    public void failed(IOException e) {
                        Log.w(TAG, e.fillInStackTrace());
                    }
                });




            }
        };

    }
}
