package com.fudan.project.ui.user;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fudan.project.MyImageView;
import com.fudan.project.R;
import com.fudan.project.UserActivity;
import com.fudan.project.ui.home.HomeFragment;
import com.fudan.project.ui.login.LoginActivity;
import com.fudan.project.ui.project.ProjectActivity;
import com.fudan.project.ui.register.RegisterActivity;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;

    //public static final String EXTRA_MESSAGE = "com.example.pm.MESSAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        final AppCompatImageView tv = root.findViewById(R.id.user_edit);
        final MyImageView iv = root.findViewById(R.id.avatar);

        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.v("UserFragment", "to do");
            }
        });

        iv.setClickable(true);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.v("UserFragment", "to do");
            }
        });

        userViewModel.getAvatar(root, getActivity());
        final TextView textView = root.findViewById(R.id.text_user);
//        MyImageView avatar = (MyImageView) root.findViewById(R.id.avatar);
//        avatar.setImageURL("http://175.24.120.91/images/pay.png");
        userViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //------------------------------------------------------------------------------------------
        //点击已完成按钮，发送参数信息给project_xml页面，已完成和全部活动的页面复用，所以要传参数区别
        Button doneButton = root.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                intent.putExtra(UserActivity.EXTRA_MESSAGE, "done");
                startActivity(intent);
            }
        });

        //点击所有活动按钮，发送参数信息给project_xml页面，已完成和全部活动的页面复用，所以要传参数区别
        Button allButton = root.findViewById(R.id.all_button);
        allButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                intent.putExtra(UserActivity.EXTRA_MESSAGE, "all");
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------



        return root;
    }

}