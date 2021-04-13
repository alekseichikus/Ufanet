package com.example.ufanet.configList;

import android.content.Context;

public interface IConfigListView {

    void  onResponse(String string);

    Context getContext();

    void onResponseFailure(Throwable throwable);

    void closeView();
    void startEditConfig(Integer id_config);
    void startControlConfigDialogFragment(Integer id_config);
}
