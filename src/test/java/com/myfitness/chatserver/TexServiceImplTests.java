package com.myfitness.chatserver;

import com.myfitness.chatserver.entity.ExpiredMessage;
import com.myfitness.chatserver.entity.Message;
import com.myfitness.chatserver.repository.TextBackupRepository;
import com.myfitness.chatserver.repository.TextRepository;
import com.myfitness.chatserver.service.TextService;
import com.myfitness.chatserver.service.TextServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class TexServiceImplTests {

    private static final long FORWARD_TIME = System.currentTimeMillis() + 240 * 1000;
    private static final long EXPIRED_TIME = System.currentTimeMillis() - 240 * 1000;
    @Autowired
    private TextService textService;
    @MockBean
    private TextBackupRepository textBackupRepository;
    @MockBean
    private TextRepository textRepository;

    @Before
    public void setUp() {
        Message message = new Message();
        message.setUserName("ajay");
        message.setId(1);
        message.setText("text");
        message.setEpochTime(FORWARD_TIME);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        Mockito.when(textRepository.findByUserNameAndEpochTimeGreaterThan(message.getUserName(), FORWARD_TIME))
                .thenReturn(messageList);

        Mockito.when(textRepository.save(any())).thenReturn(message);
    }

    @Test
    public void testToCheckWhetherAnUnexpiredTextIsReturned() {
        List<com.myfitness.chatserver.domain.Message> result = textService.getUnexpiredTexts("ajay", FORWARD_TIME);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getText()).isNotBlank();
    }

    @Test
    public void testForCreationofMessages() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername("ajay");
        message.setId(1);
        assertThat(textService.addText(message)).isNotNull();
    }

    @Test
    public void testToCheckForMessageRetrievalThroughIdReturnsNoResults() {
        long id = 1;
        Mockito.when(textRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(textBackupRepository.findById(id)).thenReturn(Optional.empty());
        assertThat(textService.getTextById(1)).isNull();

    }

    @Test
    public void testToCheckForMessageRetrievalThroughIdReturnsAnExpiredResult() {
        long id = 1;
        ExpiredMessage expiredMessage = new ExpiredMessage();
        expiredMessage.setId(1);
        expiredMessage.setTimeout(60);
        expiredMessage.setUserName("ajay");
        Mockito.when(textRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(textBackupRepository.findById(id)).thenReturn(Optional.ofNullable(expiredMessage));
        assertThat(textService.getTextById(1)).isNotNull();

    }

    @Test
    public void testToCheckForMessageRetrievalThroughIdReturnsAnUnExpiredResult() {
        long id = 1;
        Message message = new Message();
        message.setId(1);
        message.setTimeout(60);
        message.setUserName("ajay");
        Mockito.when(textRepository.findById(id)).thenReturn(Optional.ofNullable(message));
        assertThat(textService.getTextById(1)).isNotNull();

    }

    @Test
    public void testToCheckForMessageRetrievalAllExpiredTextsReturnsAnExpiredResult() {

        long currentTime = System.currentTimeMillis();
        Message message = new Message();
        message.setUserName("ajay");
        message.setId(1);
        message.setText("text");
        message.setEpochTime(EXPIRED_TIME);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        Mockito.when(textRepository.findByEpochTimeLessThan(currentTime)).thenReturn(messageList);
        assertThat(textService.getExpiredTexts(currentTime)).hasSize(1);

    }


    @Test
    public void testMoveExpiredMessages() {


        long currentTime = System.currentTimeMillis();
        Message message = new Message();
        message.setUserName("ajay");
        message.setId(1);
        message.setText("text");
        message.setEpochTime(EXPIRED_TIME);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        Mockito.when(textRepository.findByEpochTimeLessThan(currentTime)).thenReturn(messageList);
        textService.moveExpiredMessages();
        Mockito.verify(textRepository).delete(message);
        Mockito.verify(textBackupRepository).save(any());

    }


    @TestConfiguration
    static class TextServiceImplTestContextConfiguration {

        @Bean
        public TextService textService() {
            return new TextServiceImpl();
        }
    }
}

