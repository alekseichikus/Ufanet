package com.example.ufanet.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class IKey implements Parcelable {

    private String fio;
    private String key;
    private Integer type;

    protected IKey(Parcel in) {
        fio = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(fio);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IKey> CREATOR = new Creator<IKey>() {
        @Override
        public IKey createFromParcel(Parcel in) {
            return new IKey(in);
        }

        @Override
        public IKey[] newArray(int size) {
            return new IKey[size];
        }
    };

    public IKey(String fio, String key_name, Integer type){
        this.fio = fio;
        this.key = key_name;
        this.type = type;
    }

    public String getKeyBytes() {
        return key;
    }
    public String getFio() {
        return fio;
    }

    public Integer getType() {
        return type;
    }
}
