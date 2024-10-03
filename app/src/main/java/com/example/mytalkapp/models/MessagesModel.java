package com.example.mytalkapp.models;

import android.util.Log;

public class MessagesModel {
    String uId, message;
    Long timestamp;
    private static final String TAG = "MessagesModel";

    public MessagesModel(String uId, String message, Long timestamp) {
        Log.d(TAG, "MessagesModel: Constructor with timestamp");
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String uId, String message) {
        Log.d(TAG, "MessagesModel: Constructor without timestamp");
        this.uId = uId;
        this.message = message;
    }

    public MessagesModel() {
        Log.d(TAG, "MessagesModel: Default Constructor");
    }

    public String getuId() {
        return uId;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
