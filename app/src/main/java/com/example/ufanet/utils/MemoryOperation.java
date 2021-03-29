package com.example.ufanet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_PASSWORD_USER;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_TOKEN_USER;

public class MemoryOperation {

    SharedPreferences mSettings;

    public MemoryOperation(Context context) {
        this.mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getPasswordUser() {
        return mSettings.getString(APP_PREFERENCES_PASSWORD_USER, "");
    }

    public void setPasswordUser(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PASSWORD_USER, path);
        editor.commit();
    }

    public String getTokenUser() {
        return mSettings.getString(APP_PREFERENCES_TOKEN_USER, "");
    }

    public void setTokenUser(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_TOKEN_USER, path);
        editor.commit();
    }
}