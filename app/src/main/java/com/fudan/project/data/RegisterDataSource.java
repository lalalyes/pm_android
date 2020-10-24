package com.fudan.project.data;

import android.content.Context;
import android.util.Log;

import com.fudan.project.HttpClient;
import com.fudan.project.data.model.LoggedInUser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class RegisterDataSource {
    String success = "登录失败";
    final static Object object=new Object();
    public Result<String> register(final String username, String password, String workNumber, Context c) {
        try {
            HttpClient client = HttpClient.getInstance(c);
            HashMap<String, String> param = new HashMap<>();
            param.put("username", username);
            param.put("password", password);
            param.put("workNumber", workNumber);
            client.post("register", param, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    synchronized (object) {
                        if(res.code() == 200){
                            success = "登录成功";
                        }else {
                            String s = res.body().string();
                            if(s.contains("username")) {
                                success = "用户名存在";
                            }
                            if(s.contains("work number")) {
                                success = "学工号存在";
                            }
                        }
                        object.notify();
                    }
                }

                @Override
                public void failed(IOException e) {
                    synchronized (object) {
                        object.notify();
                        Log.v("request failed", e.toString());
                    }
                }
            });
            synchronized (object){
                object.wait();
            }
            if(success.equals("登录成功")) return new Result.Success<>(success);
            else return new Result.Error(new IOException(success));
        } catch (Exception e) {
            Log.v("tag", e.toString());
            return new Result.Error(new IOException(success));
        }
    }
}
