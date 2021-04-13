package com.example.ufanet.editConfig;

import com.example.ufanet.utils.MemoryOperation;

public interface IEditConfigView {

    Boolean isWiegand();

    Boolean isDallas();
    Boolean isGerkon();
    Boolean isButton();
    Boolean isBluetooth();
    Boolean isLock();
    Boolean isLockInvert();
    Integer getLockTime();
    String getConfigName();
    Boolean isBuzzerCase();
    Boolean isBuzzerGerkon();
    Boolean isBuzzerKey();
    Boolean isBuzzerLock();

    void setWiegand(Boolean state);
    void setDallas(Boolean state);
    void setGerkon(Boolean state);
    void setButton(Boolean state);
    void setBluetooth(Boolean state);
    void setLock(Boolean state);
    void setLockInvert(Boolean state);
    void setLockTime(Integer time);
    void setConfigName(String state);
    void setBuzzerCase(Boolean state);
    void setBuzzerGerkon(Boolean state);
    void setBuzzerKey(Boolean state);
    void setBuzzerLock(Boolean state);

    MemoryOperation getMemoryOperation();

    void  onResponse(String string);
    void  closeView();

    void onResponseFailure(Throwable throwable);

}
