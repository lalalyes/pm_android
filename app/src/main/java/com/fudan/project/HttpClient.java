package com.fudan.project;


import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.fudan.project.data.LoginRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private OkHttpClient client;
    private static HttpClient mClient;
    private Context context;
    final private String baseUrl = "http://175.24.120.91:8001/";
    //final private String baseUrl = "http://127.0.0.1:8001/";
    private HttpClient(Context c) {
        context = c;
        client = new OkHttpClient.Builder()
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    public static HttpClient getInstance(Context c){
        if (mClient == null) {
            mClient = new HttpClient(c);
        }
        return  mClient;
    }



    // GET方法
    public void get(String url, HashMap<String,String> param, final MyCallback callback) {
        url = baseUrl + url;
        // 拼接请求参数
        if (!param.isEmpty()) {
            StringBuffer buffer = new StringBuffer(url);
            buffer.append('?');
            for (Map.Entry<String,String> entry: param.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append('=');
                buffer.append(entry.getValue());
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length()-1);
            url = buffer.toString();
        }
        Request.Builder builder;
        if(LoginRepository.getInstance() != null &&LoginRepository.getInstance().user != null)
            builder = new Request.Builder().url(url).addHeader("Authorization", LoginRepository.getInstance().user.getToken());
        else builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    public void get(String url, MyCallback callback) {
        get(url, new HashMap<String, String>(), callback);
    }

    // POST 方法
    public void post(String url, HashMap<String, String> param, final MyCallback callback) {
        url = baseUrl + url;
        Gson gson = new Gson();
        String s = gson.toJson(param);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, s);

        Request.Builder builder = new Request.Builder();
        Request request;
        if(LoginRepository.getInstance() != null &&LoginRepository.getInstance().user != null)
            request = builder.post(requestBody)
                .url(url).addHeader("Authorization", LoginRepository.getInstance().user.getToken())
                .build();
        else request = builder.post(requestBody)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }
    public interface MyCallback {
        void success(Response res) throws IOException;
        void failed(IOException e);
    }
}
