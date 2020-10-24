package com.fudan.project.data;

import android.content.Context;
import android.util.Log;

import com.fudan.project.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public Result<String> register(String username, String password, String workNumber, Context context) {
        // handle login
        Result<String> result = dataSource.register(username, password, workNumber, context);
//        if (result instanceof Result.Success) {
//            Log.e("RegisterRepository", ((Result.Success<Integer>) result).getData() == 1 ? "Success" : "Fail");
//        }
        return result;
    }
}
