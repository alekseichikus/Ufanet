package com.example.ufanet.templates;

public class TrimConfig {

    private String nameConfig;

    Integer wiegand;
    Integer dallas;
    Integer bluetooth;
    Integer gerkon;
    Integer button;
    Integer lock;
    Integer lock_invert;
    Integer buzzer_case;
    Integer buzzer_gerkon;
    Integer buzzer_lock;
    Integer buzzer_key;
    Integer lock_time;
    char configWord;

    public TrimConfig(String nameConfig, char configWord) {

        this.nameConfig = nameConfig;
        this.configWord = configWord;

        String binary_string = Integer.toBinaryString(configWord);

        binary_string = addingLeadingZeros(binary_string);

        wiegand = Character.getNumericValue(binary_string.charAt(0));
        dallas = Character.getNumericValue(binary_string.charAt(1));
        bluetooth = Character.getNumericValue(binary_string.charAt(2));
        gerkon = Character.getNumericValue(binary_string.charAt(3));
        button = Character.getNumericValue(binary_string.charAt(4));
        lock = Character.getNumericValue(binary_string.charAt(5));
        lock_invert = Character.getNumericValue(binary_string.charAt(6));
        buzzer_case = Character.getNumericValue(binary_string.charAt(7));
        buzzer_gerkon = Character.getNumericValue(binary_string.charAt(8));
        buzzer_lock = Character.getNumericValue(binary_string.charAt(9));
        buzzer_key = Character.getNumericValue(binary_string.charAt(10));


        lock_time = Integer.parseInt(String.valueOf(Character.getNumericValue(binary_string.charAt(11))) + String.valueOf(Character.getNumericValue(binary_string.charAt(12))) +
                String.valueOf(Character.getNumericValue(binary_string.charAt(13))) + String.valueOf(Character.getNumericValue(binary_string.charAt(14))) +
                String.valueOf(Character.getNumericValue(binary_string.charAt(15))), 2);
    }

    String addingLeadingZeros(String text){
        while(text.length() != 16){
            text = "0" + text;
        }
        return text;
    }

    public String getNameConfig() {
        return nameConfig;
    }

    public Integer getWiegand() {
        return wiegand;
    }

    public Integer getDallas() {
        return dallas;
    }

    public Integer getBluetooth() {
        return bluetooth;
    }

    public Integer getGerkon() {
        return gerkon;
    }

    public Integer getButton() {
        return button;
    }

    public Integer getLock() {
        return lock;
    }

    public Integer getLock_invert() {
        return lock_invert;
    }

    public Integer getBuzzer_case() {
        return buzzer_case;
    }

    public Integer getBuzzer_gerkon() {
        return buzzer_gerkon;
    }

    public Integer getBuzzer_lock() {
        return buzzer_lock;
    }

    public Integer getBuzzer_key() {
        return buzzer_key;
    }

    public Integer getLock_time() {
        return lock_time;
    }

    public char getConfigWord() {
        return configWord;
    }
}
