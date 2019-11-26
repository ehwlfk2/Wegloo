package com.example.target_club_in_donga.calendar.data;

import androidx.lifecycle.MutableLiveData;

public class TSLiveData<T> extends MutableLiveData<T> {

    public TSLiveData() {

    }

    // public TSLiveData(T value) { setValue(value); }
    public TSLiveData(T value) {
        setValue(value);
    }


    public T getValue(T value) {
        return value;
    }
}
