package com.myfitness.chatserver.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDetails {

    private String username;
    private String text;
    @JsonProperty("expiration_date")

    private String expirationDate;

    public MessageDetails(String username, String text, String expirationDate) {
        this.username = username;
        this.text = text;
        this.expirationDate = expirationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }


}
