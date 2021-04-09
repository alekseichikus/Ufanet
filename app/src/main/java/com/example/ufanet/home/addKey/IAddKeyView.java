package com.example.ufanet.home.addKey;

import com.example.ufanet.utils.MemoryOperation;

public interface IAddKeyView {

    MemoryOperation getMemoryOperation();

    void  onResponse(String string);
    void  closeView();

    String getFio();

    String getKey();

    void onResponseFailure(Throwable throwable);
}
