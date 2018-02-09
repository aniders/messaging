package com.sample.messagingservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sample.messagingservice.dto.MessageRequest;

@Component
public class RequestValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestValidator.class);

    public void validate(Object target, Errors errors) {
        MessageRequest request = (MessageRequest) target;

        if (request.getClientId() == null) {
            errors.rejectValue("clientId", "error.clientId", "Client id is required");
        }
        if (request.getMessage().isEmpty()) {
            errors.rejectValue("message", "error.message", "message text is required");
        }
    }

    public Long validateMessageId(String id, Errors errors) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            errors.rejectValue("messageId", "error.id", "Invalid message ID");
        }
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return MessageRequest.class.equals(clazz);
    }

}