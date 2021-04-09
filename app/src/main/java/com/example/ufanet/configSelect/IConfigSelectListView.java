package com.example.ufanet.configSelect;

import android.content.Context;

import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;

public interface IConfigSelectListView {

    void  onResponse(String string);

    Context getContext();

    void onResponseFailure(Throwable throwable);

    void closeView();
    void selectConfig(Integer id_config);
}
