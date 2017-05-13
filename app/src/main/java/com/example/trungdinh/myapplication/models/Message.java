package com.example.trungdinh.myapplication.models;

/**
 * Created by TrungDinh on 5/11/2017.
 */

public class Message {

    private String id;
    private String name;
    private String messageText;
    private String time;
    private String images;

    public Message() {

    }

    public Message(String id, String name, String messageText, String time, String images) {
        this.id = id;
        this.name = name;
        this.messageText = messageText;
        this.time = time;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
