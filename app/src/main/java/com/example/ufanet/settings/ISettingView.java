package com.example.ufanet.settings;

import com.example.ufanet.settings.presenter.ISettingPresenter;

public interface ISettingView {

    void  onResponse(String string);

    void onResponseFailure(Throwable throwable);

    ISettingPresenter getPresenter();

    void closeView();
}
