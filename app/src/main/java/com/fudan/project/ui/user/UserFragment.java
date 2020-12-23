package com.fudan.project.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fudan.project.R;
import com.fudan.project.ui.home.DetailsActivity;
import com.fudan.project.ui.login.LoginActivity;

import static com.fudan.project.ui.home.HomeFragment.ACIDFORDETAILS;
import static com.fudan.project.ui.home.HomeFragment.PICNAME;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    private TextView userName;
    Button myInfoBtn, logoutBtn, changePwBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);


        userName = root.findViewById(R.id.userName);
        myInfoBtn = root.findViewById(R.id.myInformation);
        logoutBtn = root.findViewById(R.id.logout);
        changePwBtn = root.findViewById(R.id.changePassword);

        myInfoBtn.setOnClickListener(jumpToMyInfo());
        logoutBtn.setOnClickListener(logout());
        changePwBtn.setOnClickListener(jumpToChangePassword());
        userViewModel.getAvatar(root, getActivity());






        return root;
    }

    private View.OnClickListener jumpToChangePassword() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), PasswordFormActivity.class);
                v.getContext().startActivity(intent);
            }
        };
    }

    private View.OnClickListener logout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        };
    }

    private View.OnClickListener jumpToMyInfo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MyInformationActivity.class);
                v.getContext().startActivity(intent);
            }
        };
    }

}