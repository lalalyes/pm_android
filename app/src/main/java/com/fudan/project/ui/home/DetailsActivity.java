package com.fudan.project.ui.home;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fudan.project.HttpClient;
import com.fudan.project.MyImageView;
import com.fudan.project.R;
import com.fudan.project.data.model.Activity;
import com.google.gson.Gson;
import com.mzlion.core.lang.StringUtils;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    private final String TAG = "DetailsActivity";
    private Activity activity = new Activity();
    private String picName = "";
    Button signUpBtn;
    private final String acIDParamName= "activityId";
    Context context;
    String acID;
    MyImageView acImg2;
    MyImageView acImg1;
    TextView acTitle;
    TextView acSponsor;
    TextView acIntroduction;
    TextView acTime;
    TextView acVenue;
    TextView acLimit;
    TextView acParticipateNum;
    TextView acSignupTime;

    private String signupRet = "";

    public static final String picBaseURL= "http://175.24.120.91/images/";

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String convert2String(long time) {
        if (time > 0l) {
            SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_fragment);

        Intent intent = getIntent();
        acID = intent.getStringExtra(HomeFragment.ACIDFORDETAILS);
        picName = intent.getStringExtra(HomeFragment.PICNAME);

        signUpBtn = findViewById(R.id.btnParticipateActivity);

        Log.w(TAG,"id is "+acID);
        Log.w(TAG,"pic "+picName);



        context = getApplicationContext();


        getAcDetails();





        signUpBtn.setOnClickListener(signUpActivity());
    }


    private View.OnClickListener signUpActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity(acID);
            }
        };
    }

    public void signUpActivity(String acID){
        String url = "activityEnrollment";
        HttpClient client = HttpClient.getInstance(context);
        HashMap<String,String> param = new HashMap<>();
        param.put(acIDParamName,acID);
        client.get(url, param, new HttpClient.MyCallback() {
            @Override
            public void success(Response res) throws IOException {
                if (res.code()==200){
                   signupRet = res.message();
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);
                }
                Log.w(TAG,signupRet);
            }

            @Override
            public void failed(IOException e) {
                Log.w(TAG, e.fillInStackTrace());
                Looper.prepare();
                Toast.makeText(context,"发生错误 报名失败 ！",Toast.LENGTH_SHORT).show();;
                Looper.loop();
            }
        });
    }
    private static final int COMPLETED = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED){
                render();
            }
        }
    };

    private Handler toasterHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED){
                if (signupRet.equals("success")){
                    Toast.makeText(context,signupRet +"报名成功 ！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,signupRet,Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public void render(){
        Log.w(TAG,"____________________________");

        acTitle = findViewById(R.id.detailsTitle);
        acSponsor = findViewById(R.id.sponsor);
        acIntroduction = findViewById(R.id.acIntro);
        acTime = findViewById(R.id.acTime);
        acVenue = findViewById(R.id.acVenue);
        acLimit = findViewById(R.id.acLimit);
        acParticipateNum = findViewById(R.id.acParticipate);
        acSignupTime = findViewById(R.id.acSignupTime);

        acImg2 = findViewById(R.id.acCover);
        acImg1 = findViewById(R.id.acPic);
        acTitle.setText(activity.getActivityName());
        acTime.setText(convert2String(activity.getActivityStartTime())+" ~ "+convert2String(activity.getActivityEndTime()));
        acSignupTime.setText(convert2String(activity.getSignUpStartTime())+" ~ "+convert2String(activity.getActivityEndTime()));
        Log.w(TAG,activity.getHost().getUsername());
        acSponsor.setText("活动举办方 ： "+activity.getHost().getUsername());
        acVenue.setText(activity.getActivityVenue());
        acLimit.setText(""+activity.getCapacity());

        acParticipateNum.setText(""+activity. getCurrentNum());

        acIntroduction.setText(activity.getIntroduction());

        acImg1.setImageURL(picBaseURL + picName);
        acImg2.setImageURL(picBaseURL + picName);

    }



    public void getAcDetails(){
        String url = "activityDetails";
        HttpClient client = HttpClient.getInstance(context);

        HashMap<String,String> param = new HashMap<>();
        param.put(acIDParamName,acID);

        client.get(url, param, new HttpClient.MyCallback() {
            @Override
            public void success(Response res) throws IOException {
                if (res.code()==200){
                    String response = res.body().string();
                    Log.w(TAG, response);
                    Gson gson = new Gson();
                    activity = gson.fromJson(response,Activity.class);
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


}
