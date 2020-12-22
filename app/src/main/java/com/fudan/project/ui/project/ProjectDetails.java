package com.fudan.project.ui.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fudan.project.HttpClient;
import com.fudan.project.R;
import com.fudan.project.UserActivity;
import com.fudan.project.data.Result;
import com.fudan.project.data.model.LoggedInUser;
import com.fudan.project.data.model.MyProjectResponse;
import com.fudan.project.data.model.ProjectDetailsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class ProjectDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);


        Intent intent = getIntent();
        final String id = intent.getStringExtra(UserActivity.Id);

        getProjectDetails("activityDetails?activityId="+id, this);
        System.out.println("url: --------------------: id" + id);


        Button commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText comment = findViewById(R.id.pj_comment);
                RatingBar rb = findViewById(R.id.ratingBar);
                doComment(id, comment.getText().toString(), (int)rb.getRating(), getBaseContext());
                //提交评论
            }
        });

        Button checkButton = findViewById(R.id.check_in);
        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkInProject("activityCheckIn?activityId="+id, getBaseContext());
                //q签到
            }
        });

        Button applyButton = findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                applyProject("activityEnrollment?activityId="+id, getBaseContext());
                //报名参加
            }
        });

        Button cancelApplyButton = findViewById(R.id.cancel_apply);
        cancelApplyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                retreatProject("retreatEnrollment?activityId="+id, getBaseContext());
                //取消报名参加
            }
        });

    }


    public void getProjectDetails(String url, Context c){

        try {
            HttpClient client = HttpClient.getInstance(c);
            client.get(url, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    if(res.code() == 200) {
                        System.out.println(res.toString());
                        // 怎么获得data
                        Gson gson = new Gson();
                        String s = res.body().string();
                        ProjectDetailsResponse projectDetailsResponse = gson.fromJson(s, ProjectDetailsResponse.class);
                        //Map<String, Object> data = projectDetailsResponse.getActivity();

                        //----------------------------------------------
                        //判定当前活动状态，报名截止前10分钟和活动开始前10分钟要弹窗表示
                        Date now = new Date();
                        if(now.getTime() < projectDetailsResponse.getActivityStartTime() && now.getTime() > projectDetailsResponse.getActivityStartTime()-600000){
                            Toast.makeText(getBaseContext(), "离活动开始还有不到10分钟", Toast.LENGTH_SHORT).show();
                        }

                        if(now.getTime() < projectDetailsResponse.getSignUpEndTime() && now.getTime() > projectDetailsResponse.getActivityEndTime()-600000){
                            Toast.makeText(getBaseContext(), "离报名结束还有不到10分钟", Toast.LENGTH_SHORT).show();
                        }



                        //显示按钮：没报名显示报名按钮；报名了不在进行中只显示取消报名按钮，报名了在进行中显示取消报名和签到按钮
                        System.out.println("status:------------------------------------------------" + projectDetailsResponse.getStatus());
                        if(projectDetailsResponse.getEnrolled()){
                            Button applyButton = findViewById(R.id.apply);
                            applyButton.setVisibility(View.GONE);
                            if(now.getTime() < projectDetailsResponse.getActivityStartTime() && now.getTime() > projectDetailsResponse.getActivityEndTime()){
                                Button checkButton = findViewById(R.id.check_in);
                                checkButton.setVisibility(View.GONE);
                            }
                        }
                        else{
                            Button cancelApplyButton = findViewById(R.id.cancel_apply);
                            cancelApplyButton.setVisibility(View.GONE);
                            Button checkButton = findViewById(R.id.check_in);
                            checkButton.setVisibility(View.GONE);
                        }
                        //-------------------------------------------------------

                        TextView nameT = findViewById(R.id.pj_name);
                        TextView venueT = findViewById(R.id.pj_venue);
                        TextView introT = findViewById(R.id.pj_introduction);
                        TextView capacityT = findViewById(R.id.pj_capacity);
                        TextView processT = findViewById(R.id.pj_process_time);
                        TextView signT = findViewById(R.id.pj_sign_in_time);

                        nameT.setText(projectDetailsResponse.getActivityName());
                        venueT.setText(projectDetailsResponse.getActivityVenue());
                        introT.setText(projectDetailsResponse.getIntroduction());
                        capacityT.setText(projectDetailsResponse.getCapacity());



                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                        Date ps = new Date(projectDetailsResponse.getActivityStartTime());
                        Date pe = new Date(projectDetailsResponse.getActivityEndTime());
                        String process_start = ft.format(ps);
                        String process_end = ft.format(pe);

                        System.out.println("process time: "+process_start + "----" + process_end);
                        processT.setText("活动：" + process_start + "--" + process_end);

                        Date ss = new Date(projectDetailsResponse.getSignUpStartTime());
                        Date se = new Date(projectDetailsResponse.getSignUpEndTime());
                        String sign_start = ft.format(ss);
                        String sign_end = ft.format(se);

                        System.out.println("sign time: "+sign_start + "----" + sign_end);
                        signT.setText("报名：" + sign_start + "--" + sign_end);



                    }else {
                        Log.v("request failed", res.body().string());
                    }
                }

                @Override
                public void failed(IOException e) {
                    Log.v("request failed", e.toString());
                }
            });
        } catch (Exception e) {
            Log.v("tag", e.toString());
        }
    }

    final static Object object=new Object();
    public void doComment(String id, String desc, int rating, Context c){

        try {
            HttpClient client = HttpClient.getInstance(c);
            HashMap<String, String> param = new HashMap<>();
            param.put("activityId", id);
            param.put("text", desc);
            param.put("score", rating+"");
            client.post("writeReview", param, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    synchronized (object) {
                        if(res.code() == 200){
                            ///没效果
                            //System.out.println("----------------------------------------------------------------------------------评论成功");
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(res.code() == 201){
                            ///没效果
                            System.out.println("----------------------------------------------------------------------------------签到之后才能评论哦");

                            Looper.prepare();

                            Toast.makeText(getBaseContext(), "签到之后才能评论哦", Toast.LENGTH_SHORT).show();

                            Looper.loop();


                        }

                        object.notify();
                    }
                }

                @Override
                public void failed(IOException e) {
                    synchronized (object) {
                        //System.out.println("----------------------------------------------------------------------------------评论失败");
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), "评论失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        object.notify();
                        Log.v("request failed", e.toString());
                    }
                }
            });
            synchronized (object){
                object.wait();
            }


        } catch (Exception e) {
            Log.v("tag", e.toString());
        }
    }



    public void applyProject(String url, Context c){

        try {
            HttpClient client = HttpClient.getInstance(c);
            client.get(url, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    if(res.code() == 200) {
                        System.out.println(res.toString());
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), "报名：操作成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }

                @Override
                public void failed(IOException e) {
                    Log.v("request failed", e.toString());
                    Looper.prepare();
                    Toast.makeText(getBaseContext(), "报名：操作失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            Log.v("tag", e.toString());
        }
    }


    public void retreatProject(String url, Context c){

        try {
            HttpClient client = HttpClient.getInstance(c);
            client.get(url, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    if(res.code() == 200) {
                        System.out.println(res.toString());
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), "取消报名:操作成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }

                @Override
                public void failed(IOException e) {
                    Log.v("request failed", e.toString());
                    Looper.prepare();
                    Toast.makeText(getBaseContext(), "取消报名：操作失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            Log.v("tag", e.toString());
        }
    }

    public void checkInProject(String url, Context c){

        try {
            HttpClient client = HttpClient.getInstance(c);
            client.get(url, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    if(res.code() == 200) {
                        System.out.println(res.toString());
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), "签到：操作成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }

                @Override
                public void failed(IOException e) {
                    Log.v("request failed", e.toString());
                    Looper.prepare();
                    Toast.makeText(getBaseContext(), "签到：操作失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            Log.v("tag", e.toString());
        }
    }


}
