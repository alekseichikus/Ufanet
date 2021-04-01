package com.example.ufanet.templates;

public class Config {

    private String wlan_ssid;
    private String wlan_pass;
    private Integer bluetooth;
    private Integer wiegand;
    private Integer dallas;
    private Integer gerkon;
    private Integer button;
    private Integer lock;
    private Integer lock_invert;
    private Integer lock_time;
    private Integer buzzer_case;
    private Integer buzzer_gerkon;
    private Integer buzzer_key;
    private Integer buzzer_lock;
    private String time;
    private String status;
    private Integer code;

    public Config(String wlan_ssid, String wlan_pass
            , Integer bluetooth, Integer wiegand, Integer dallas
            , Integer gerkon, Integer button, Integer lock, Integer lock_invert
            , Integer lock_time, Integer buzzer_case, Integer buzzer_gerkon
            , Integer buzzer_key, Integer buzzer_lock, String time) {
        this.wlan_ssid = wlan_ssid;
        this.wlan_pass = wlan_pass;
        this.bluetooth = bluetooth;
        this.wiegand = wiegand;
        this.dallas = dallas;
        this.gerkon = gerkon;
        this.status = "fail";
        this.code = 0;
        this.button = button;
        this.lock = lock;
        this.lock_invert = lock_invert;
        this.lock_time = lock_time;
        this.buzzer_case = buzzer_case;
        this.buzzer_gerkon = buzzer_gerkon;
        this.buzzer_key = buzzer_key;
        this.buzzer_lock = buzzer_lock;
        this.time = time;
    }
}
