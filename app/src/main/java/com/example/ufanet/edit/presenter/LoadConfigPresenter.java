package com.example.ufanet.edit.presenter;

import android.util.Log;
import com.example.ufanet.edit.ILoadConfigView;
import com.example.ufanet.edit.model.LoadConfigModel;
import com.example.ufanet.edit.model.ILoadConfigModel;

public class LoadConfigPresenter implements ILoadConfigPresenter, LoadConfigModel.OnFinishedListener {

    private ILoadConfigModel model;
    private ILoadConfigView view;

    public static final Integer APP_PREFERENCES_MAX_COUNT_SLAVE_DEVICES = 7;

    public LoadConfigPresenter(ILoadConfigView view){
        this.view = view;
        model = new LoadConfigModel();
    }

    @Override
    public void onFinished() {
        if (view != null) {
            view.closeView();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (view != null) {
            view.onResponseFailure(t);
        }
    }

    Boolean isLoginUserValid(String text){
        if(text.isEmpty()){
            return false;
        }
        return true;
    }

    Boolean isPasswordUserValid(String text){
        if(text.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void requestEditConfig() {
        if (view != null) {
            if(isLoginUserValid(view.getLoginUser())){
                if(isPasswordUserValid(view.getPasswordUser())){
                    String request_string = "wlan_ssid=\"" + view.getLoginUser() + "\"\n" +
                            "wlan_pass=\"" + view.getPasswordUser() + "\"\n" +
                            "unixtime=\"1617786779\"\n" +
                            "timezone=\"CST-5\"\n" +
                            "master=\"" + view.getConfigsSelect()[0] + "\"\n";

                    for (int i = 1; i <= APP_PREFERENCES_MAX_COUNT_SLAVE_DEVICES; i++) {
                        if(i < view.getConfigsSelect().length){
                            request_string += "slave_"+(i)+"=\"" + view.getConfigsSelect()[i] + "\"\n";
                        }
                        else{
                            request_string += "slave_"+(i)+"=\"65507\"\n";
                        }
                    }
                    model.sendConfig(this, request_string);
                    Log.d("request", request_string);
                }
                else{
                    view.onResponse("Пароль не должен быть пустым");
                }
            }
            else{
                view.onResponse("Логин не должен быть пустым");
            }
        }
    }
}
