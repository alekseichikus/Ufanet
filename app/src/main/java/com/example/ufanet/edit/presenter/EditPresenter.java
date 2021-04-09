package com.example.ufanet.edit.presenter;

import android.content.Context;
import android.util.Log;

import com.example.ufanet.edit.IEditView;
import com.example.ufanet.edit.model.EditModel;
import com.example.ufanet.edit.model.IEditModel;
import com.example.ufanet.utils.MemoryOperation;

public class EditPresenter implements IEditPresenter, EditModel.OnFinishedListener {

    private IEditModel model;
    private IEditView view;

    public EditPresenter(IEditView view){
        this.view = view;
        model = new EditModel();
    }

    @Override
    public void onFinished() {
        if (view != null) {
            /*view.onResponse("Конфиг перезаписан, перезагружаюсь");
            view.getMemoryOperation().setLoginUser(wlan_ssid);
            view.getMemoryOperation().setPasswordUser(wlan_pass);
            view.getMemoryOperation().setBluetoothSW(intToBoolean(bluetooth));
            view.getMemoryOperation().setWiegandSW(intToBoolean(wiegand));
            view.getMemoryOperation().setDallasSW(intToBoolean(dallas));
            view.getMemoryOperation().setGerkonSW(intToBoolean(gerkon));
            view.getMemoryOperation().setButtonSW(intToBoolean(button));
            view.getMemoryOperation().setLockSW(intToBoolean(lock));
            view.getMemoryOperation().setLockInvertSW(intToBoolean(lock_invert));
            view.getMemoryOperation().setLockTimeConfig(lock_time);
            view.getMemoryOperation().setBuzzerCaseSW(intToBoolean(buzzer_case));
            view.getMemoryOperation().setBuzzerGerkonSW(intToBoolean(buzzer_gerkon));
            view.getMemoryOperation().setBuzzerKeySW(intToBoolean(buzzer_key));
            view.getMemoryOperation().setBuzzerLockSW(intToBoolean(buzzer_lock));
            view.getMemoryOperation().setTimeConfig(time);
            */
            view.closeView();
        }
    }

    int boolToInt(Boolean b) {
        return b.compareTo(false);
    }

    private boolean intToBoolean(int input) {
        if((input==0)||(input==1)) {
            return input!=0;
        }
        return true;
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

    Boolean isLockTimeValid(Integer numb){
        if(numb.toString().isEmpty()){
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
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void requestEditConfig() {
        if (view != null) {
            if(isLoginUserValid(view.getLoginUser())){
                if(isPasswordUserValid(view.getPasswordUser())){
                    if(isLockTimeValid(view.getLockTime())){
                        model.sendConfig(this, view.getLoginUser(), view.getPasswordUser(), "CST-5",  15236547893l, view.getConfigsSelect());
                    }
                    else{
                        view.onResponse("LockTime не должен быть пустым");
                    }
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
