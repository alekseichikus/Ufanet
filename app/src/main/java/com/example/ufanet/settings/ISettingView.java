package com.example.ufanet.settings;

import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.utils.MemoryOperation;

public interface ISettingView {

    void  onResponse(String string);

    void onResponseFailure(Throwable throwable);

    ISettingPresenter getPresenter();

    void closeView();
}
