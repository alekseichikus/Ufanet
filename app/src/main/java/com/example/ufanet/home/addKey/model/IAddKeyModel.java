package com.example.ufanet.home.addKey.model;

public interface IAddKeyModel {
    interface OnFinishedListener {
        void onFinished();

        void onFailure(Throwable t);
    }

    void sendKeys(OnFinishedListener onFinishedListener, String keys);
}
