package com.fudan.project.ui.user;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fudan.project.data.UserDataSource;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private UserDataSource dataSource;
    public UserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        dataSource = new UserDataSource();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getAvatar(View root, Context c) {
        dataSource.getAvatar(root,c);
    }
}