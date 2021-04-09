package com.example.ufanet.edit;

import android.content.Context;

import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;
import java.util.List;

public interface IEditView {

    String getLoginUser();

    String getPasswordUser();

    MemoryOperation getMemoryOperation();
    Integer getLockTime();

    void  onResponse(String string);
    void  closeView();
    void  startSelectConfigActivity(Integer position);

    void onResponseFailure(Throwable throwable);

    IEditPresenter getPresenter();
    Integer[] getConfigsSelect();

    Context getContext();
}
