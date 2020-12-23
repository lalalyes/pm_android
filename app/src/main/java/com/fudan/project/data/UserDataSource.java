package com.fudan.project.data;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fudan.project.HttpClient;
import com.fudan.project.MyImageView;
import com.fudan.project.R;
import com.fudan.project.data.model.LoggedInUser;
import com.fudan.project.data.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class UserDataSource {
    public void getAvatar(final View root, Context c){
        try {
            HttpClient client = HttpClient.getInstance(c);
            client.get("my_info", new HttpClient.MyCallback() {
                @Override
                public void success(Response res) throws IOException {
                    if(res.code() == 200) {
                        Gson gson = new Gson();
                        String s = res.body().string();
                        User user = gson.fromJson(s, User.class);
//                        Log.v("UserDataSource", user.getAvatar());
                        //todo
//                        MyImageView avatar = (MyImageView) root.findViewById(R.id.avatar);
//                        TextView tv = (TextView)root.findViewById(R.id.text_user);
//                        avatar.setImageURL("http://175.24.120.91/images/" + user.getAvatar());
//                        tv.setText(user.getUsername());
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
}
