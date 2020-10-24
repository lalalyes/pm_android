package com.fudan.project.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;

import com.fudan.project.data.RegisterRepository;
import com.fudan.project.data.Result;
import com.fudan.project.R;

import java.io.IOException;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String password, String workNumber, Context context) {
        // can be launched in a separate asynchronous job
        Result<String> result = registerRepository.register(username, password, workNumber, context);
        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult(((Result.Success<String>) result).getData()));
        } else {
            registerResult.setValue(new RegisterResult(((Result.Error) result).getError().getMessage()));
        }
    }

    public void registerDataChanged(String username, String password, String confirmPassword, String workNumber) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null, null));
        } else if (!confirmPassword.equals(password)){
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_confirm_password, null));
        }else if (workNumber == null || workNumber.length() != 6){
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_workNumber));
        }else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return !username.trim().isEmpty();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}