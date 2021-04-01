package com.example.ufanet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_LOGIN_USER;
import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_PASSWORD_USER;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BLUETOOTH_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUTTON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_CASE_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_GERKON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_KEY_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_LOCK_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_DALLAS_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_GERKON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_INVERT_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_TIME;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_TIME;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_WIEGAND_SW;
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

    public void setBluetoothSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BLUETOOTH_SW, path);
        editor.commit();
    }

    public Boolean getBluetoothSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BLUETOOTH_SW, true);
    }

    public void setWiegandSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_WIEGAND_SW, path);
        editor.commit();
    }

    public Boolean getWiegandSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_WIEGAND_SW, false);
    }

    public void setDallasSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_DALLAS_SW, path);
        editor.commit();
    }

    public Boolean getDallasSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_DALLAS_SW, false);
    }

    public void setGerkonSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_GERKON_SW, path);
        editor.commit();
    }

    public Boolean getGerkonSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_GERKON_SW, false);
    }

    public void setButtonSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BUTTON_SW, path);
        editor.commit();
    }

    public Boolean getButtonSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BUTTON_SW, true);
    }

    public void setLockSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_LOCK_SW, path);
        editor.commit();
    }

    public Boolean getLockSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_LOCK_SW, true);
    }

    public void setLockInvertSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_LOCK_INVERT_SW, path);
        editor.commit();
    }

    public Boolean getLockInvertSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_LOCK_INVERT_SW, false);
    }

    public void setBuzzerCaseSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BUZZER_CASE_SW, path);
        editor.commit();
    }

    public Boolean getBuzzerCaseSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BUZZER_CASE_SW, true);
    }

    public void setBuzzerGerkonSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BUZZER_GERKON_SW, path);
        editor.commit();
    }

    public Boolean getBuzzerGerkonSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BUZZER_GERKON_SW, true);
    }

    public void setBuzzerKeySW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BUZZER_KEY_SW, path);
        editor.commit();
    }

    public Boolean getBuzzerKeySW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BUZZER_KEY_SW, true);
    }

    public void setBuzzerLockSW(Boolean path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CONFIG_BUZZER_LOCK_SW, path);
        editor.commit();
    }

    public Boolean getBuzzerLockSW() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_CONFIG_BUZZER_LOCK_SW, true);
    }

    public void setTimeConfig(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_CONFIG_TIME, path);
        editor.commit();
    }

    public String getTimeConfig() {
        return sharedPreferences.getString(APP_PREFERENCES_CONFIG_TIME, "15236547893");
    }

    public void setLockTimeConfig(Integer path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_CONFIG_LOCK_TIME, path);
        editor.commit();
    }

    public Integer getLockTimeConfig() {
        return sharedPreferences.getInt(APP_PREFERENCES_CONFIG_LOCK_TIME, 3000);
    }
}