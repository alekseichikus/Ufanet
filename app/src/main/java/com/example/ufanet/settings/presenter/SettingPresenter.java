package com.example.ufanet.settings.presenter;

import com.example.ufanet.settings.ISettingView;
import com.example.ufanet.settings.model.ISettingModel;
import com.example.ufanet.settings.model.SettingModel;

public class SettingPresenter implements ISettingPresenter, SettingModel.OnFinishedListener {

    private ISettingModel model;
    private ISettingView view;

    public SettingPresenter(ISettingView view){
        this.view = view;
        model = new SettingModel();
    }

    @Override
    public void onFinished() {
        view.onResponse("Устройство начало перезагрузку");
        view.closeView();
    }

    @Override
    public void onFailure(Throwable t) {
        if (view != null) {
            view.onResponseFailure(t);
        }
    }

    @Override
    public void requestReloadDevice() {
        model.restartDevice(this);
    }
}
