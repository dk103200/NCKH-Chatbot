package com.chatbot_ute;

public class MessageClass {
    String message = "";
    String sender = "";
    boolean isbot = false;

    public MessageClass(String message, String sender, boolean isbot) {
        this.message = message;
        this.sender = sender;
        this.isbot = isbot;
    }
}
