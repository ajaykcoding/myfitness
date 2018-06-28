package com.myfitness.chatserver.service;

import com.myfitness.chatserver.domain.Message;
import com.myfitness.chatserver.dto.MessageDTO;
import com.myfitness.chatserver.entity.ExpiredMessage;
import com.myfitness.chatserver.repository.TextBackupRepository;
import com.myfitness.chatserver.repository.TextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TextServiceImpl implements TextService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextServiceImpl.class);
    @Autowired
    TextRepository textRepository;
    @Autowired
    TextBackupRepository textBackupRepository;

    @Override
    public Message addText(final Message message) {
        long currentTimeinMillis = System.currentTimeMillis() + message.getDuration() * 1000;
        LOGGER.info("Adding text for {} and with duration {} ", message.getUsername(), message.getDuration());

        com.myfitness.chatserver.entity.Message entity = new MessageDTO().getEntityFromDomain(message);
        entity.setEpochTime(currentTimeinMillis);
        entity.setExpirationTime(Instant.ofEpochMilli(currentTimeinMillis).toString());
        com.myfitness.chatserver.entity.Message savedEnity = textRepository.save(entity);
        return new MessageDTO().getDomainFromEntity(savedEnity);
    }


    @Override
    public List<Message> getUnexpiredTexts(String userName, long currentTimeMillis) {
        LOGGER.info("Getting unexpired texts for {} and with currentTime {} ", userName, currentTimeMillis);

        List<com.myfitness.chatserver.entity.Message> results = textRepository.findByUserNameAndEpochTimeGreaterThan(userName, currentTimeMillis);
        return results.stream().map(e -> new MessageDTO().getDomainFromEntity(e)).collect(Collectors.toList());
    }

    @Override
    public Message getTextById(long id) {
        LOGGER.info("Getting all texts for  user with id :{}  ", id);

        Message message = null;
        Optional<com.myfitness.chatserver.entity.Message> entity = textRepository.findById(id);
        if (entity.isPresent()) {
            message = new MessageDTO().getDomainFromEntity(entity.get());
        } else {
            LOGGER.debug("No active messages , falling back to expired messages");

            Optional<com.myfitness.chatserver.entity.ExpiredMessage> entity2 = textBackupRepository.findById(id);
            if (entity2.isPresent()) {
                message = new MessageDTO().getDomainFromEntity(entity2.get());
            }
        }
        return message;
    }


    @Override
    public List<com.myfitness.chatserver.entity.Message> getExpiredTexts(long currentTimeinMillis) {
        LOGGER.info("Getting all expired messages from the messages tables with ts {} ", currentTimeinMillis);

        List<com.myfitness.chatserver.entity.Message> messages = textRepository.findByEpochTimeLessThan(currentTimeinMillis);
        return messages;
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void moveExpiredMessages() {
        long currentTimeinMillis = System.currentTimeMillis();
        LOGGER.info("Expiration Task run  {} ", currentTimeinMillis);

        List<com.myfitness.chatserver.entity.Message> messages = getExpiredTexts(currentTimeinMillis);
        if (!messages.isEmpty()) {
            LOGGER.info("Found {} messages to move  ", messages.size());

            for (com.myfitness.chatserver.entity.Message message : messages) {
                ExpiredMessage expiredMessages = new ExpiredMessage();
                expiredMessages.setId(message.getId());
                expiredMessages.setText(message.getText());
                expiredMessages.setUserName(message.getUserName());
                expiredMessages.setTimeout(message.getTimeout());
                expiredMessages.setExpirationTime(message.getExpirationTime());
                textBackupRepository.save(expiredMessages);
                textRepository.delete(message);
            }
        }
    }


}
