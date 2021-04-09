package com.example.ufanet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.ufanet.settings.IKey;
import com.example.ufanet.templates.TrimConfig;

import java.io.BufferedReader;
import java.util.ArrayList;

import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_LOGIN_USER;
import static com.example.ufanet.profile.profileFragments.APP_PREFERENCES_PASSWORD_USER;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_ARRAY_SIZE;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BLUETOOTH_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUTTON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_CASE_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_GERKON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_KEY_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_BUZZER_LOCK_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_DALLAS_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_DATA;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_GERKON_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_INVERT_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_LOCK_TIME;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_TIME;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_CONFIG_WIEGAND_SW;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_KEY_ARRAY_SIZE;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_KEY_DATA;
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
        return sharedPreferences.getString(APP_PREFERENCES_PASSWORD_USER, "1234567890");
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
        return sharedPreferences.getString(APP_PREFERENCES_LOGIN_USER, "SCUD");
    }

    public Integer getConfigsArraySize() {
        return sharedPreferences.getInt(APP_PREFERENCES_CONFIG_ARRAY_SIZE, 0);
    }
    public Integer getKeyArraySize() {
        return sharedPreferences.getInt(APP_PREFERENCES_KEY_ARRAY_SIZE, 0);
    }

    public char getConfigDataWord(Integer i) {
        return (char) sharedPreferences.getInt(APP_PREFERENCES_CONFIG_DATA + "_" + i + "_word", 0);
    }

    public String getConfigDataName(Integer i) {
        return sharedPreferences.getString(APP_PREFERENCES_CONFIG_DATA + "_" + i + "_name", "Конфиг");
    }

    public String getKeyDataFIO(Integer i) {
        return sharedPreferences.getString(APP_PREFERENCES_KEY_DATA + "_" + i + "_fio", "");
    }

    public String getKeyDataKey(Integer i) {
        return sharedPreferences.getString(APP_PREFERENCES_KEY_DATA + "_" + i + "_key", "");
    }

    public Integer getKeyDataType(Integer i) {
        return sharedPreferences.getInt(APP_PREFERENCES_KEY_DATA + "_" + i + "_type", 0);
    }

    public void setConfigDataWord(Integer i, Integer path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_CONFIG_DATA + "_" + i + "_word", path);
        editor.commit();
    }

    public void setKeyDataFio(Integer i, String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_KEY_DATA + "_" + i + "_fio", path);
        editor.commit();
    }

    public void setKeyDataKey(Integer i, String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_KEY_DATA + "_" + i + "_key", path);
        editor.commit();
    }

    public void setKeyDataType(Integer i, Integer path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_KEY_DATA + "_" + i + "_type", path);
        editor.commit();
    }

    public void setConfigDataName(Integer i, String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_CONFIG_DATA + "_" + i + "_name", path);
        editor.commit();
    }

    public void setConfigsArraySize(Integer i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_CONFIG_ARRAY_SIZE, i);
        editor.commit();
    }

    public void setKeyArraySize(Integer i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_KEY_ARRAY_SIZE, i);
        editor.commit();
    }

    public ArrayList<IKey> getKeyList(){
        BufferedReader reader = null;
        ArrayList<IKey> items = new ArrayList<>();
        for (int i = 0; i < getKeyArraySize(); i++) {
            IKey trimConfig = new IKey(getKeyDataFIO(i), getKeyDataKey(i), getKeyDataType(i));
            items.add(trimConfig);
        }
        return items;
    }

    public ArrayList<TrimConfig> getConfigList(){
        BufferedReader reader = null;
        ArrayList<TrimConfig> items = new ArrayList<>();
        for (int i = 0; i < getConfigsArraySize(); i++) {
            TrimConfig trimConfig = new TrimConfig(getConfigDataName(i), getConfigDataWord(i));
            items.add(trimConfig);
        }
        return items;
    }

    public void deleteConfig(Integer id_config){
        Log.d("id_config delete", String.valueOf(id_config));
        ArrayList<TrimConfig> configs = new ArrayList<>();
        for (int i = 0; i < getConfigsArraySize(); i++) {
            if(i != id_config){
                configs.add(new TrimConfig(getConfigDataName(i), getConfigDataWord(i)));
            }
        }

        setConfigsArraySize(getConfigsArraySize()-1);

        for (int i = 0; i < getConfigsArraySize(); i++) {
            setConfigDataWord(i, (int) configs.get(i).getConfigWord());
            setConfigDataName(i, configs.get(i).getNameConfig());
        }
    }

    public void deleteKey(Integer id_key){
        Log.d("id_config delete", String.valueOf(id_key));
        ArrayList<IKey> configs = new ArrayList<>();
        for (int i = 0; i < getKeyArraySize(); i++) {
            if(i != id_key){
                configs.add(new IKey(getKeyDataFIO(i), getKeyDataKey(i), getKeyDataType(i)));
            }
        }

        setKeyArraySize(getKeyArraySize()-1);

        for (int i = 0; i < getKeyArraySize(); i++) {
            setKeyDataFio(i, configs.get(i).getFio());
            setKeyDataKey(i, configs.get(i).getKeyBytes());
            setKeyDataType(i, configs.get(i).getType());
        }
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