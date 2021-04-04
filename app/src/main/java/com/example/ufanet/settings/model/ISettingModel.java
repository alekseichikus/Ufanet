package com.example.ufanet.settings.model;


public interface ISettingModel {
    interface OnFinishedListener {
        void onFinished();

        void onFailure(Throwable t);
    }

    void restartDevice(OnFinishedListener onFinishedListener);
}
