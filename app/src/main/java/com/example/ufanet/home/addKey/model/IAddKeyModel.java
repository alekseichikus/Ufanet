package com.example.ufanet.home.addKey.model;


import com.example.ufanet.settings.IKey;

import java.util.ArrayList;

public interface IAddKeyModel {
    interface OnFinishedListener {
        void onFinished(String keys);

        void onFailure(Throwable t);
    }

    void sendKeys(OnFinishedListener onFinishedListener, String keys);
}
