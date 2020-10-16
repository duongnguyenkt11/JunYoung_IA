package com.example.myapplication.object;

public class MessageEvent {
    String key;
    Object object;

    public MessageEvent(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
