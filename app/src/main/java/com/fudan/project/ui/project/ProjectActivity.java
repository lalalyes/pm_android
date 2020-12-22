package com.fudan.project.ui.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fudan.project.HttpClient;

import com.fudan.project.MyImageView;
import com.fudan.project.R;
import com.fudan.project.UserActivity;

import com.fudan.project.data.model.MyProjectResponse;
import com.fudan.project.data.model.ProjectResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class ProjectActivity extends AppCompatActivity {

    LinearLayout wr_areas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        wr_areas =  findViewById(R.id.wr_areas);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(UserActivity.EXTRA_MESSAGE);


        //得到done或者all参数之后，去访问数据库
        if(message.equals("已报名")){

            getMyProject("enrolledActivity", this);
            //getProject("my_project", this);

        }
        else{
            getMyProject("activityList", this);
        }
        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView2);
        //textView.setText(message);




    }

    public void getMyProject(String url, Context c){
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
                        MyProjectResponse myProjectResponse = gson.fromJson(s, MyProjectResponse.class);


                        List<Map<String, String>> data = myProjectResponse.getActivities();

                        showMyData(data);
                        //Log.v("UserDataSource", data.size());

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

    private void showMyData(List<Map<String, String>> titleData) {

        for (int i = 0; i < titleData.size(); i++) {

            final Map<String, String> pojo = titleData.get(i);

            LinearLayout llWashingRoomItem = new LinearLayout(this);

            llWashingRoomItem.setLayoutParams(new RelativeLayout.LayoutParams(

                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            llWashingRoomItem = (LinearLayout) this.getLayoutInflater().inflate(R.layout.table_reuse, null);

            TextView time = llWashingRoomItem.findViewById(R.id.pj_activity_name);

            TextView vhclNo = llWashingRoomItem.findViewById(R.id.pj_venue);

            TextView ticket = llWashingRoomItem.findViewById(R.id.pj_capacity);

            MyImageView iv = llWashingRoomItem.findViewById(R.id.imageView);

            System.out.println("----------------------------------分界线------------------------------------");

            time.setText(pojo.get("activityName"));

            //vhclNo.setText("地点：" + pojo.get("venue"));

            //Integer类型需要转换用.toString()不然报错
            vhclNo.setText(pojo.get("introduction"));
            ticket.setText("类型：" +pojo.get("type"));
            iv.setImageURL("http://175.24.120.91/images/"+pojo.get("picture"));


            System.out.println("time: "+time.getText());
            System.out.println("vhclNo: "+vhclNo.getText());
            System.out.println("ticket: "+ticket.getText());

//            //从网上取图片,更改图片src，但好像没效果？？？
//            Bitmap bitmap = getHttpBitmap("http://175.24.120.91/images/" + pojo.get("picture"));
//            iv .setImageBitmap(bitmap);

            //动态设置layout_weight权重设置表格宽度
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,  1.6f);

            time.setLayoutParams(lp);

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,  1f);

            vhclNo.setLayoutParams(lp);

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f);

            ticket.setLayoutParams(lp);

            llWashingRoomItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(v.getContext(), ProjectDetails.class);
                    intent.putExtra(UserActivity.Id, pojo.get("activityId"));
                    startActivity(intent);
                }
            });

            wr_areas.addView(llWashingRoomItem);

        }

    }

//    public static Bitmap getHttpBitmap(String url) {
//        URL myFileUrl = null;
//        Bitmap bitmap = null;
//        try {
////            Log.d(TAG, url);
//            myFileUrl = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//            conn.setConnectTimeout(0);
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//
//
//    public void getProject(String url, Context c){
//        try {
//            HttpClient client = HttpClient.getInstance(c);
//            client.get(url, new HttpClient.MyCallback() {
//                @Override
//                public void success(Response res) throws IOException {
//                    if(res.code() == 200) {
//                        System.out.println(res.toString());
//                        // 怎么获得data
//                        Gson gson = new Gson();
//                        //System.out.println(res.body());
//                        //System.out.println(res.body().getClass().toString());
//                        //System.out.println(res.body().getData());
//                        String s = res.body().string();
//                        ProjectResponseData projectResponseData = gson.fromJson(s, ProjectResponseData.class);
//                        System.out.println(projectResponseData.getData());
//                        s = projectResponseData.getData();
//                        Log.v("UserDataSource", s);
//                        Type type=new TypeToken<List<Map<String, String>>>(){}.getType();
//                        List<Map<String, String>> data = gson.fromJson(s, type);
//
//                        showData(data);
//                        //Log.v("UserDataSource", data.size());
//
//                    }else {
//                        Log.v("request failed", res.body().string());
//                    }
//                }
//
//                @Override
//                public void failed(IOException e) {
//                    Log.v("request failed", e.toString());
//                }
//            });
//        } catch (Exception e) {
//            Log.v("tag", e.toString());
//        }
//    }
//
//    private void showData(List<Map<String, String>> titleData) {
//
//        for (int i = 0; i < titleData.size(); i++) {
//
//            final Map<String, String> pojo = titleData.get(i);
//
//            LinearLayout llWashingRoomItem = new LinearLayout(this);
//
//            llWashingRoomItem.setLayoutParams(new RelativeLayout.LayoutParams(
//
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//            llWashingRoomItem = (LinearLayout) this.getLayoutInflater().inflate(R.layout.table_reuse, null);
//
//            TextView time = llWashingRoomItem.findViewById(R.id.pj_activity_name);
//
//            TextView vhclNo = llWashingRoomItem.findViewById(R.id.pj_venue);
//
//            TextView start_time = llWashingRoomItem.findViewById(R.id.pj_start_time);
//
//            TextView end_time = llWashingRoomItem.findViewById(R.id.pj_end_time);
//
//            TextView ticket = llWashingRoomItem.findViewById(R.id.pj_capacity);
//
//
//            System.out.println("----------------------------------分界线------------------------------------");
//
//            time.setText(pojo.get("activity_name"));
//
//            //vhclNo.setText("地点：" + pojo.get("venue"));
//
//
//
//
//
//
//            Long start = Long.parseLong(pojo.get("activity_start_time"));
//
//            Long end = Long.parseLong(pojo.get("activity_end_time"));
//
//            Date s = new Date(start);
//            Date e = new Date(end);
//            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String process_start = ft.format(s);
//            String process_end = ft.format(e);
//            System.out.println("process time: "+process_start);
//
//
//            start_time.setText("开始时间：" + process_start);
//            end_time.setText("结束时间：" + process_end);
//
//            //Integer类型需要转换用.toString()不然报错
//
//            ticket.setText("人数上限：" +pojo.get("capacity").toString());
//
//            System.out.println("time: "+time.getText());
//            System.out.println("vhclNo: "+vhclNo.getText());
//            System.out.println("start_time: "+start_time.getText());
//            System.out.println("end_time: "+end_time.getText());
//            System.out.println("ticket: "+ticket.getText());
//            //动态设置layout_weight权重设置表格宽度
//
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,  1.6f);
//
//            time.setLayoutParams(lp);
//
//            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,  1f);
//
//            vhclNo.setLayoutParams(lp);
//
//            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f);
//
//            start_time.setLayoutParams(lp);
//
//            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f);
//
//            end_time.setLayoutParams(lp);
//
//            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f);
//
//            ticket.setLayoutParams(lp);
//
//            wr_areas.addView(llWashingRoomItem);
//
//        }
//
//    }

}
