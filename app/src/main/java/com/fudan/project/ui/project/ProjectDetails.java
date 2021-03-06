package com.fudan.project.ui.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fudan.project.HttpClient;
import com.fudan.project.R;
import com.fudan.project.UserActivity;

import com.fudan.project.data.model.ProjectDetailsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class ProjectDetails extends AppCompatActivity {


    LinearLayout comment_area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        comment_area = findViewById(R.id.comment_area);

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
                        System.out.println(projectDetailsResponse.getActivityName());
                        System.out.println(projectDetailsResponse.getActivityVenue());
                        System.out.println(projectDetailsResponse.getIntroduction());
                        System.out.println(projectDetailsResponse.getCapacity());


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


                        Date now = new Date();
                        //-------------------------------------------------------
                        //显示按钮：没报名显示报名按钮；报名了不在进行中只显示取消报名按钮，报名了在进行中显示取消报名和签到按钮
                        System.out.println("status:------------------------------------------------" + projectDetailsResponse.getStatus());
                        System.out.println(":---------------------" + now.getTime()+"-----------------------"+projectDetailsResponse.getActivityEndTime());
                        if(now.getTime() > projectDetailsResponse.getActivityEndTime()){
                            System.out.println("已结束:------------------------------------------------" );
                            Button applyButton = findViewById(R.id.apply);
                            applyButton.setVisibility(View.GONE);
                            Button checkButton = findViewById(R.id.check_in);
                            checkButton.setVisibility(View.GONE);
                            Button cancelApplyButton = findViewById(R.id.cancel_apply);
                            cancelApplyButton.setVisibility(View.GONE);


                            showComments(projectDetailsResponse.getComments());
                        }
                        else{
                            if(projectDetailsResponse.getEnrolled()){
                                Button applyButton = findViewById(R.id.apply);
                                applyButton.setVisibility(View.GONE);
                                if(now.getTime() < projectDetailsResponse.getActivityStartTime() || now.getTime() > projectDetailsResponse.getActivityEndTime()){
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
                            //to do: 隐藏评论（包括评论功能和评论展示）
                            //LinearLayout comment_area = findViewById(R.id.comment_area);
                            Looper.prepare();
                            comment_area.setVisibility(View.GONE);
                            Looper.loop();
                        }


                        //----------------------------------------------
                        // 判定当前活动状态，报名截止前10分钟和活动开始前10分钟要弹窗表示
                        //活动进行中和活动已结束也要弹窗展示
                        if(now.getTime() < projectDetailsResponse.getSignUpEndTime() && now.getTime() > projectDetailsResponse.getActivityEndTime()-600000){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), "离报名结束还有不到10分钟", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        if(now.getTime() < projectDetailsResponse.getActivityStartTime() && now.getTime() > projectDetailsResponse.getActivityStartTime()-600000){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), "离活动开始还有不到10分钟", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        if(now.getTime() > projectDetailsResponse.getActivityStartTime() && now.getTime() < projectDetailsResponse.getActivityEndTime()){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), "活动进行中", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(now.getTime() > projectDetailsResponse.getActivityEndTime()){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), "活动已结束", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }


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

    private void showComments(List<Map<String, String>> titleData) {

        LinearLayout comment_areas = findViewById(R.id.show_comments);
        for (int i = 0; i < titleData.size(); i++) {

            final Map<String, String> pojo = titleData.get(i);

            LinearLayout llWashingRoomItem = new LinearLayout(this);

            llWashingRoomItem.setLayoutParams(new RelativeLayout.LayoutParams(

                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            llWashingRoomItem = (LinearLayout) this.getLayoutInflater().inflate(R.layout.comment_reuse, null);

            TextView username = llWashingRoomItem.findViewById(R.id.user_name);

            TextView score = llWashingRoomItem.findViewById(R.id.user_score);

            TextView comment = llWashingRoomItem.findViewById(R.id.user_comment);

            //MyImageView iv = llWashingRoomItem.findViewById(R.id.imageView);
            //ImageView iv = llWashingRoomItem.findViewById(R.id.imageView);
            System.out.println("----------------------------------分界线------------------------------------");

            username.setText(pojo.get("username"));
            score.setText("评分：" +pojo.get("score"));
            comment.setText(pojo.get("content"));
            //iv.setImageURL("http://175.24.120.91/images/"+pojo.get("picture"));


            System.out.println("username: "+ pojo.get("username"));
            System.out.println("score: "+ pojo.get("score"));
            System.out.println("comment: "+ pojo.get("content"));

//            //从网上取图片,更改图片src，但好像没效果？？？
//            Bitmap bitmap = getHttpBitmap("http://175.24.120.91/images/" + pojo.get("picture"));
//            iv .setImageBitmap(bitmap);

            //动态设置layout_weight表格宽度
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            username.setLayoutParams(lp);

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            score.setLayoutParams(lp);

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            comment.setLayoutParams(lp);


            comment_areas.addView(llWashingRoomItem);

        }

    }
}
