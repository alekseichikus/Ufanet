package com.example.ufanet.edit.model;

public interface ILoadConfigModel {
    interface OnFinishedListener {
        void onFinished();

        void onFailure(Throwable t);
    }

    void sendConfig(OnFinishedListener onFinishedListener, String config);
    void restartDevice(OnFinishedListener onFinishedListener);
}
