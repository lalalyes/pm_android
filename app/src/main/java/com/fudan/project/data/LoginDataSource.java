package com.fudan.project.data;

import android.content.Context;
import android.util.Log;

import com.fudan.project.HttpClient;
import com.fudan.project.data.model.LoggedInUser;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    LoggedInUser user;
    final static Object object=new Object();
    public Result<LoggedInUser> login(final String username, String password, Context c) {
        try {
            HttpClient client = HttpClient.getInstance(c);
            HashMap<String, String> param = new HashMap<>();
            param.put("username", username);
            param.put("password", password);
            client.post("login", param, new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    synchronized (object) {
                        if(res.code() == 200){
                            String s = res.body().string();
                            Gson gson = new Gson();
                            user = gson.fromJson(s, LoggedInUser.class);
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
            if(user != null) return new Result.Success<>(user);
            else return new Result.Error(new IOException("Error logging in"));
        } catch (Exception e) {
            Log.v("tag", e.toString());
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}