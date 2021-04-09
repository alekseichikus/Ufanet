package com.example.ufanet.addConfig;

import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.utils.MemoryOperation;

public interface IAddConfigView {


    Boolean isWiegand();

    Boolean isDallas();
    Boolean isGerkon();
    Boolean isButton();
    MemoryOperation getMemoryOperation();
    Boolean isBluetooth();
    Boolean isLock();
    Boolean isLockInvert();
    Integer getLockTime();
    String getConfigName();
    Boolean isBuzzerCase();
    Boolean isBuzzerGerkon();
    Boolean isBuzzerKey();
    Boolean isBuzzerLock();

    void  onResponse(String string);
    void  closeView();

    void onResponseFailure(Throwable throwable);

}
