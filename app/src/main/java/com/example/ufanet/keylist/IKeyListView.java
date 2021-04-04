package com.example.ufanet.keylist;

import com.example.ufanet.settings.presenter.ISettingPresenter;

public interface IKeyListView {

    void  onResponse(String string);

    void onResponseFailure(Throwable throwable);

    ISettingPresenter getPresenter();

    void closeView();
}
