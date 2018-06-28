package com.myfitness.chatserver.service;

import com.myfitness.chatserver.domain.Id;
import com.myfitness.chatserver.domain.Message;
import com.myfitness.chatserver.domain.MessageDetails;
import com.myfitness.chatserver.domain.ShortMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacadeServiceImpl implements FacadeService {

    @Autowired
    TextService textService;

    @Override
    public Id addText(Message message) {
        Message obtainedMessage = textService.addText(message);
        Id resultMessage = new Id(obtainedMessage.getId());
        return resultMessage;
    }

    @Override
    public List<ShortMessage> getUnexpiredTexts(String userName, long currentTimeinMillis) {
        List<Message> messages = textService.getUnexpiredTexts(userName, currentTimeinMillis);
        return messages.stream().map(m -> new ShortMessage(m.getText(), m.getId())).collect(Collectors.toList());

    }

    @Override
    public MessageDetails getTextById(long id) {
        Message message = textService.getTextById(id);
        MessageDetails messageDetails = null;
        if (message != null) {
            messageDetails = new MessageDetails(message.getUsername(), message.getText(), message.getExpirationDate());

        }
        return messageDetails;

    }
}
