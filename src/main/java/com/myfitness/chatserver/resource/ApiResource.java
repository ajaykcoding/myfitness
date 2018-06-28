package com.myfitness.chatserver.resource;


import com.myfitness.chatserver.domain.Id;
import com.myfitness.chatserver.domain.Message;
import com.myfitness.chatserver.domain.MessageDetails;
import com.myfitness.chatserver.domain.ShortMessage;
import com.myfitness.chatserver.service.FacadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ApiResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResource.class);
    @Autowired
    FacadeService facadeService;

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<Id> sendText(@RequestBody @Valid Message message) {
        LOGGER.info("Obtained a send text");
        Id obtainedMessage = facadeService.addText(message);
        return new ResponseEntity<Id>(obtainedMessage, HttpStatus.CREATED);
    }

    @GetMapping("/chat/{id}")
    @ResponseBody
    public ResponseEntity<Object> getTextById(@PathVariable long id) {
        MessageDetails messageDetails = facadeService.getTextById(id);
        return getResponse(messageDetails);
    }

    @GetMapping("/chats/{username}")
    @ResponseBody
    public ResponseEntity<Object> getTextByUserName(@PathVariable String username) {
        List<ShortMessage> messages = facadeService.getUnexpiredTexts(username, System.currentTimeMillis());
        return getResponse(messages);
    }

    private ResponseEntity<Object> getResponse(Object obj) {

        if (obj == null) {
            ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(obj, HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

}
