package com.example.ufanet.home.model;


public interface IHomeModel {
    interface OnFinishedListener {
        void onFinished();

        void onFailure(Throwable t);
    }

    void broadcastBluetooth(OnFinishedListener onFinishedListener);
}
