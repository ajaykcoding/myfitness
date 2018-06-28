package com.myfitness.chatserver.service;

import com.myfitness.chatserver.domain.Id;
import com.myfitness.chatserver.domain.Message;
import com.myfitness.chatserver.domain.MessageDetails;
import com.myfitness.chatserver.domain.ShortMessage;

import java.util.List;

public interface FacadeService {

    Id addText(Message message);

    List<ShortMessage> getUnexpiredTexts(String userName, long currentTimeinMillis);

    MessageDetails getTextById(long id);

}
