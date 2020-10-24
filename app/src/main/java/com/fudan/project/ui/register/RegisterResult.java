package com.fudan.project.ui.register;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {
    @Nullable
    private String success;

    RegisterResult(@Nullable String success) {
        this.success = success;
    }

    @Nullable
    String getSuccess() {
        return success;
    }
}