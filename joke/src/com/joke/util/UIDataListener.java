package com.joke.util;

public interface UIDataListener<T>{
    public void onDataChanged(T data);
    public void onErrorHappened(String errorCode, String errorMessage);
}