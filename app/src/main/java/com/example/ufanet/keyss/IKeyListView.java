package com.example.ufanet.keyss;

import android.content.Context;
import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;

public interface IKeyListView {

    void  onResponse(String string);

    void showFragmentSettings(IKey key);

    Context getContext();
    void startEditKey(Integer position);
    void startControlKeyDialogFragment(Integer position);

    void onResponseFailure(Throwable throwable);

    ISettingPresenter getPresenter();

    void closeView();
}
