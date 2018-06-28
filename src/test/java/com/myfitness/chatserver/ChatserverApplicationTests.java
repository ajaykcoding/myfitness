package com.myfitness.chatserver;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatserverApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testForA_NonExistingMessage() {
        this.webTestClient.get().uri("/chat/100").exchange().expectStatus().is4xxClientError();
    }

    @Test
    public void testForB_PostMessage() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setText("Hello");
        message.setUsername("ajay");
        this.webTestClient.post().uri("/chat").contentType(MediaType.APPLICATION_JSON_UTF8).
                body(BodyInserters.fromObject(message)).exchange().expectStatus().isCreated().expectBody().jsonPath("$.id").isEqualTo(1);
    }

    @Test
    public void testForC_PostMessageAndRetrieveMessage() {
        com.myfitness.chatserver.domain.Message message = new com.myfitness.chatserver.domain.Message();
        message.setText("Hello");
        message.setUsername("ajay");
        message.setDuration(10);
        this.webTestClient.post().uri("/chat").contentType(MediaType.APPLICATION_JSON_UTF8).
                body(BodyInserters.fromObject(message)).exchange().expectStatus().isCreated().expectBody().jsonPath("$.id").isEqualTo(2);
        this.webTestClient.get().uri("/chat/2").exchange().expectStatus().isOk().expectBody().jsonPath("$.username").isEqualTo("ajay");
        this.webTestClient.get().uri("/chats/ajay").exchange().expectStatus().isOk().expectBody().jsonPath("$[0].text").isEqualTo("Hello");

    }


}
