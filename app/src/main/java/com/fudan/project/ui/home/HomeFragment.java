package com.fudan.project.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fudan.project.HttpClient;
import com.fudan.project.R;
import com.fudan.project.data.model.Activity;
import com.fudan.project.data.model.ActivityList;
import com.fudan.project.data.model.ActivityVenue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

public class HomeFragment extends Fragment {
    String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    public static final String ACIDFORDETAILS = "id for details";

    public static final String PICNAME = "pictureName for details";
    private  RecyclerView recyclerView;
    private View root;
    private  EditText searchText;
    private List<Activity> datas = new ArrayList<>();

    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        searchText = root.findViewById(R.id.searchBoxText);
        Button searchBtn = root.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(searchActivity());

        getActivityList();

        return root;
    }
    private static final int COMPLETED = 0;
    private Handler LoadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED){
                loadList(root,datas);
            }
        }
    };

    private View.OnClickListener searchActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acName = String.valueOf(searchText.getText());
                searchActivity(acName);
            }
        };
    }

    public void searchActivity(String acName){
        HashMap<String,String> param = new HashMap<>();
        param.put("content",acName);
        param.put("type","0");
        String url = "searchActivity";
        HttpClient client = HttpClient.getInstance(getContext());
        client.get(url, param, new HttpClient.MyCallback() {
            @Override
            public void success(Response res) throws IOException {

                String response = res.body().string();
                if (res.code() == 200){
                    ActivityList activityList = gson.fromJson(response, ActivityList.class);

                    datas = Arrays.asList(activityList.getActivities());
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    LoadDataHandler.sendMessage(msg);
                    Log.w(TAG,response);
                }else {
                    Log.w(TAG,response);
                }
            }

            @Override
            public void failed(IOException e) {
                Log.w(TAG,e.fillInStackTrace());
            }
        });


    }

    public void loadList(View root, List<Activity> activities){
        this.recyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        final ItemViewAdapter adapter = new ItemViewAdapter(recyclerView,activities);
        recyclerView.setAdapter(adapter);

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

    }



    public void getActivityList(){
        String url = "activityList";

        HttpClient client = HttpClient.getInstance(this.getContext());
        client.get(url, new HttpClient.MyCallback() {
            @Override
            public void success(Response res) throws IOException {
                String response = res.body().string();
                if (res.code() == 200){
                    ActivityList activityList = gson.fromJson(response, ActivityList.class);

                    datas = Arrays.asList(activityList.getActivities());
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    LoadDataHandler.sendMessage(msg);
                    Log.w(TAG,response);
                }else {
                    Log.w(TAG,response);
                }

            }

            @Override
            public void failed(IOException e) {
                Log.w(TAG, e.fillInStackTrace());
            }
        });

    }





}