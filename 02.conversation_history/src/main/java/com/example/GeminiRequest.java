package com.example;

import java.util.List;

public class GeminiRequest {

    private String model;
    private List<Message> messages;

    public GeminiRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
