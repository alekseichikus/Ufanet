package com.example.ufanet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_LOGIN_USER;
import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_PASSWORD_USER;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_PASSCODE_USER;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_TOKEN_USER;

public class MemoryOperation {

    SharedPreferences sharedPreferences;

    public MemoryOperation(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getPasscodeUser() {
        return sharedPreferences.getString(APP_PREFERENCES_PASSCODE_USER, "");
    }

    public void setPasscodeUser(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_PASSCODE_USER, path);
        editor.commit();
    }

    public String getPasswordUser() {
        return sharedPreferences.getString(APP_PREFERENCES_PASSWORD_USER, "");
    }

    public void setPasswordUser(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_PASSWORD_USER, path);
        editor.commit();
    }

    public Boolean isPasswordUserValid(){
        if(getPasswordUser().isEmpty())
            return false;
        return true;
    }

    public String getLoginUser() {
        return sharedPreferences.getString(APP_PREFERENCES_LOGIN_USER, "");
    }

    public Boolean isLoginUserValid(){
        if(getLoginUser().isEmpty())
            return false;
        return true;
    }

    public void setLoginUser(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_LOGIN_USER, path);
        editor.commit();
    }

    public String getTokenUser() {
        return sharedPreferences.getString(APP_PREFERENCES_TOKEN_USER, "");
    }

    public void setTokenUser(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_TOKEN_USER, path);
        editor.commit();
    }
}