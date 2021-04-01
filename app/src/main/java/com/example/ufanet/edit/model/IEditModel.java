package com.example.ufanet.edit.model;


public interface IEditModel {
    interface OnFinishedListener {
        void onFinished(String wlan_ssid, String wlan_pass, Integer bluetooth, Integer wiegand, Integer dallas, Integer gerkon, Integer button,
                        Integer lock, Integer lock_invert, Integer lock_time, Integer buzzer_case, Integer buzzer_gerkon, Integer buzzer_key, Integer buzzer_lock, String time);

        void onFailure(Throwable t);
    }

    void sendConfig(OnFinishedListener onFinishedListener, String wlan_ssid, String wlan_pass
            , Integer bluetooth, Integer wiegand, Integer dallas
            , Integer gerkon, Integer button, Integer lock, Integer lock_invert
            , Integer lock_time, Integer buzzer_case, Integer buzzer_gerkon
            , Integer buzzer_key, Integer buzzer_lock, String time);
    void restartDevice(OnFinishedListener onFinishedListener, String wlan_ssid, String wlan_pass
            , Integer bluetooth, Integer wiegand, Integer dallas
            , Integer gerkon, Integer button, Integer lock, Integer lock_invert
            , Integer lock_time, Integer buzzer_case, Integer buzzer_gerkon
            , Integer buzzer_key, Integer buzzer_lock, String time);
}
