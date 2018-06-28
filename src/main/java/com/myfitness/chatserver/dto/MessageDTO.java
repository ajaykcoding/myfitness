package com.myfitness.chatserver.dto;

import com.myfitness.chatserver.entity.ExpiredMessage;
import com.myfitness.chatserver.entity.Message;

public class MessageDTO {

    public Message getEntityFromDomain(com.myfitness.chatserver.domain.Message domain) {
        Message message = new Message();
        message.setTimeout(domain.getDuration());
        message.setUserName(domain.getUsername());
        message.setText(domain.getText());
        return message;
    }

    public com.myfitness.chatserver.domain.Message getDomainFromEntity(Message entity) {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername(entity.getUserName());
        message.setDuration(entity.getTimeout());
        message.setExpirationDate(entity.getExpirationTime());
        message.setText(entity.getText());
        message.setId(entity.getId());

        return message;
    }

    public com.myfitness.chatserver.domain.Message getDomainFromEntity(ExpiredMessage entity) {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername(entity.getUserName());
        message.setDuration(entity.getTimeout());
        message.setText(entity.getText());
        message.setExpirationDate(entity.getExpirationTime());
        message.setId(entity.getId());

        return message;
    }

}
