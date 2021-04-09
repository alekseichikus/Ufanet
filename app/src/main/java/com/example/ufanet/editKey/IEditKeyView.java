package com.example.ufanet.editKey;

import com.example.ufanet.utils.MemoryOperation;

public interface IEditKeyView {

    void setFio(String text);
    void setKey(String text);

    MemoryOperation getMemoryOperation();

    void  onResponse(String string);
    void  closeView();

    void onResponseFailure(Throwable throwable);

}
