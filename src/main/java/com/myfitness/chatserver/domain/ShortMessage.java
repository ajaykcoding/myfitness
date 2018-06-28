package com.myfitness.chatserver.domain;

public class ShortMessage {
    private String text;
    private long id;

    public ShortMessage(String text, long id) {

        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
