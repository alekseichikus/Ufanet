package com.example.ufanet.configList;

import android.content.Context;

import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.templates.TrimConfig;

public interface IConfigListView {

    void  onResponse(String string);

    void showFragmentSettings(IKey key);

    Context getContext();

    void onResponseFailure(Throwable throwable);

    ISettingPresenter getPresenter();

    void closeView();
    void startEditConfig(Integer id_config);
    void startControlConfigDialogFragment(Integer id_config);
}
