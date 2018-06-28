package com.myfitness.chatserver.service;

import com.myfitness.chatserver.domain.Message;

import java.util.List;

public interface TextService {

    Message addText(Message message);

    List<Message> getUnexpiredTexts(String userName, long currentTimeinMillis);

    Message getTextById(long id);

    List<com.myfitness.chatserver.entity.Message> getExpiredTexts(long currentTimeinMillis);

    void moveExpiredMessages();

}
