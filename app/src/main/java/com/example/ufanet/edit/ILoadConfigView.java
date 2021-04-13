package com.example.ufanet.edit;

import android.content.Context;

import com.example.ufanet.edit.presenter.ILoadConfigPresenter;
import com.example.ufanet.utils.MemoryOperation;

public interface ILoadConfigView {

    String getLoginUser();

    String getPasswordUser();

    MemoryOperation getMemoryOperation();

    void  onResponse(String string);
    void  closeView();
    void  startSelectConfigActivity(Integer position);

    void onResponseFailure(Throwable throwable);

    ILoadConfigPresenter getPresenter();
    Integer[] getConfigsSelect();

    Context getContext();
}
