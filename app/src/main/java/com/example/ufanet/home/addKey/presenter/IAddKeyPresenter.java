package com.example.ufanet.home.addKey.presenter;

public interface IAddKeyPresenter {

    void onDestroy();

    void requestEditConfig();
    //String wlan_ssid, String wlan_pass, Integer bluetooth, Integer wiegand, Integer dallas, Integer gerkon, Integer button,
    //                           Integer lock, Integer lock_invert, Integer lock_time, Integer buzzer_case, Integer buzzer_gerkon, Integer buzzer_key, Integer buzzer_lock, String time
}
