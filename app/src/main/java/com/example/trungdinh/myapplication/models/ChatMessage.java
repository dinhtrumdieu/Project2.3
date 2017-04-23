package com.example.trungdinh.myapplication.models;

import java.util.Date;

/**
 * Created by reale on 18/11/2016.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private boolean isMine;
    private boolean type;


    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }


    public ChatMessage(String messageText, String messageUser, boolean isMine, boolean type) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
        this.isMine = isMine;
        this.type = type;
    }

    public boolean isType() {
        return type;
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
