package com.example.ufanet.templates;

public class ConfigSelect {

    char configWord;
    String nameConfig;
    String nameUser;

    public ConfigSelect(String nameUser, String nameConfig, char configWord) {

        this.nameConfig = nameConfig;
        this.configWord = configWord;
        this.nameUser = nameUser;
    }

    public String getNameConfig() {
        return nameConfig;
    }

    public char getConfigWord() {
        return configWord;
    }

    public void setNameConfig(String nameConfig){
        this.nameConfig = nameConfig;
    }
    public void setWordConfig(char configWord){
        this.configWord = configWord;
    }

    public String getNameUser() {
        return nameUser;
    }
}
