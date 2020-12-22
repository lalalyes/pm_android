package com.fudan.project.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fudan.project.R;
import com.fudan.project.UserActivity;
import com.fudan.project.ui.project.ProjectActivity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        //------------------------------------------------------------------------------------------
        //点击已完成按钮，发送参数信息给project_xml页面，已完成和全部活动的页面复用，所以要传参数区别
        Button doneButton = root.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                intent.putExtra(UserActivity.EXTRA_MESSAGE, "已报名");
                startActivity(intent);
            }
        });

        //点击所有活动按钮，发送参数信息给project_xml页面，已完成和全部活动的页面复用，所以要传参数区别
        Button allButton = root.findViewById(R.id.all_button);
        allButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                intent.putExtra(UserActivity.EXTRA_MESSAGE, "可报名");
                startActivity(intent);
            }
        });

        return root;
    }
}