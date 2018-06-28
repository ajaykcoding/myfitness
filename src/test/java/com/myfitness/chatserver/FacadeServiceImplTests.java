package com.myfitness.chatserver;

import com.myfitness.chatserver.domain.Id;
import com.myfitness.chatserver.domain.Message;
import com.myfitness.chatserver.domain.MessageDetails;
import com.myfitness.chatserver.domain.ShortMessage;
import com.myfitness.chatserver.service.FacadeService;
import com.myfitness.chatserver.service.FacadeServiceImpl;
import com.myfitness.chatserver.service.TextService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class FacadeServiceImplTests {

    private static final long FORWARD_TIME = System.currentTimeMillis() + 240 * 1000;
    private static final long EXPIRED_TIME = System.currentTimeMillis() - 240 * 1000;
    @MockBean
    TextService textService;

    @Autowired
    private FacadeService facadeService;

    @Test
    public void testForCreationofMessages() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername("ajay");
        message.setId(1);
        Mockito.when(textService.addText(message)).thenReturn(message);
        Id id = facadeService.addText(message);
        assertThat(id).isNotNull();
        assertThat(id.getId()).isEqualTo(1);
    }

    @Test
    public void testToCheckWhetherAnUnexpiredTextIsReturned() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername("ajay");
        message.setId(1);
        message.setText("Text");
        message.setExpirationDate(Instant.ofEpochMilli(FORWARD_TIME).toString());
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Mockito.when(textService.getUnexpiredTexts("ajay", FORWARD_TIME)).thenReturn(messages);

        List<ShortMessage> messageList = facadeService.getUnexpiredTexts("ajay", FORWARD_TIME);
        assertThat(messageList).isNotNull();
        assertThat(messageList.size()).isEqualTo(1);
        assertThat(messageList.get(0).getId()).isEqualTo(1);
        assertThat(messageList.get(0).getText()).isEqualTo("Text");


    }


    @Test
    public void testToCheckWhetherAnExpiredTextIsReturned() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setUsername("ajay");
        message.setId(1);
        message.setText("Text");
        message.setExpirationDate(Instant.ofEpochMilli(EXPIRED_TIME).toString());


        Mockito.when(textService.getTextById(1)).thenReturn(message);

        MessageDetails messageDetails = facadeService.getTextById(1);
        assertThat(messageDetails).isNotNull();
        assertThat(messageDetails.getExpirationDate()).isEqualTo(Instant.ofEpochMilli(EXPIRED_TIME).toString());
        assertThat(messageDetails.getText()).isEqualTo("Text");
        assertThat(messageDetails.getUsername()).isEqualTo("ajay");


    }


    @TestConfiguration
    static class TextServiceImplTestContextConfiguration {

        @Bean
        public FacadeService facadeService() {
            return new FacadeServiceImpl();
        }
    }

}
