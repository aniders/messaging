package com.sample.messagingservice.controller;

import java.security.Principal;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.messagingservice.dto.Message;
import com.sample.messagingservice.dto.MessageRequest;
import com.sample.messagingservice.service.MessageService;
import com.sample.messagingservice.util.RequestValidator;

@RestController
@RequestMapping("/messages")
@Api(description = "Messaging service for creating, modifying, deleting and retrieving messages")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;
    
    private RequestValidator requestValidator;
    
    @Autowired
    public void setDefaultRequestValidator(RequestValidator validator) {
        this.requestValidator = validator;
    }

    @GetMapping
    @ApiOperation(value = "View list of messages", produces = "application/json")
    public ResponseEntity<List<Message>> getAll() {
        LOGGER.debug("Fetching list of messages");
        List<Message> messages = messageService.getAll();
        if (messages.isEmpty()) {
            LOGGER.debug("No messages found");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View specific message by id", produces = "application/json")
    public ResponseEntity<Message> getOne(@PathVariable Long id) {
        LOGGER.debug("Fetching message with id {}", id);
        Message message = messageService.getById(id);
        if (message == null) {
            LOGGER.debug("Message with id {} not found", id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Create a message", produces = "application/json")
    public ResponseEntity<Message> create(@RequestBody MessageRequest request, BindingResult result, Principal principle) {
        LOGGER.debug("Create message request by {} with values {}", principle, request);
        request.setClientId(principle.getName());
        requestValidator.validate(request, result);
        if (result.hasErrors()) {
            LOGGER.debug("Bad create request", result.getAllErrors());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Message createdMessage = messageService.createMessage(request);
        return new ResponseEntity<Message>(createdMessage, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Update a message", produces = "application/json")
    public ResponseEntity<Message> update(@PathVariable String id, @RequestBody MessageRequest request, BindingResult result, Principal principle) {
        LOGGER.debug("Update request by Client {} for message {}", principle.getName(), request.toString());

        Long messageId = requestValidator.validateMessageId(id, result);
        request.setClientId(principle.getName());
        requestValidator.validate(request, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(messageService.canUpdateDeleteMessage(messageId, request.getClientId())){
            messageService.updateMessage(messageId, request);
            Message message = messageService.getById(messageId);
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
       
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete a message")
    public ResponseEntity delete(@PathVariable String id, Principal principle) {
        LOGGER.debug("Delete message with id {} ", id);
        Long messageId;
        try {
            messageId=  Long.valueOf(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(messageService.canUpdateDeleteMessage(messageId, principle.getName())){
            messageService.deleteMessage(messageId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
