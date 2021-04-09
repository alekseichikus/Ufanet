package com.example.ufanet.templates;

public class Config {




    private Integer[] config;
    private long time;
    private String wlan_pass;
    private String wlan_ssid;
    private String timezone;

    public Config(String wlan_ssid, String wlan_pass
            , String timezone, long time, Integer[] config) {
        this.wlan_ssid = wlan_ssid;
        this.wlan_pass = wlan_pass;
        this.timezone = timezone;
        this.config = config;
        this.time = time;
    }
}
