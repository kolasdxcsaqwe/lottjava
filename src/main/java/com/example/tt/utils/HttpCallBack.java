package com.example.tt.utils;

public interface HttpCallBack {

    void onError(Exception ex);
    void onSuccess(String data);
}
