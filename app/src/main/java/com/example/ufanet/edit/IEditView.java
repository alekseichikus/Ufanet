package com.example.ufanet.edit;

import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;
import java.util.List;

public interface IEditView {

    String getLoginUser();

    String getPasswordUser();

    Boolean isWiegand();

    Boolean isDallas();
    Boolean isGerkon();
    Boolean isButton();
    MemoryOperation getMemoryOperation();
    Boolean isBluetooth();
    Boolean isLock();
    Boolean isLockInvert();
    Integer getLockTime();
    Boolean isBuzzerCase();
    Boolean isBuzzerGerkon();
    Boolean isBuzzerKey();
    Boolean isBuzzerLock();

    void  onResponse(String string);
    void  closeView();

    void onResponseFailure(Throwable throwable);

    IEditPresenter getPresenter();
}
