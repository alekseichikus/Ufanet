package com.example.ufanet.edit.model;


public interface IEditModel {
    interface OnFinishedListener {
        void onFinished();

        void onFailure(Throwable t);
    }

    void sendConfig(OnFinishedListener onFinishedListener, String wlan_ssid, String wlan_pass
            , String timezone, long time, Integer[] config);
    void restartDevice(OnFinishedListener onFinishedListener);
}
