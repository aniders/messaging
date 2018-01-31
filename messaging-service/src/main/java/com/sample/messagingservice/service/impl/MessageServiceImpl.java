package com.sample.messagingservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.messagingservice.dao.MessageDAO;
import com.sample.messagingservice.dto.Message;
import com.sample.messagingservice.dto.MessageRequest;
import com.sample.messagingservice.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageDAO dao;

    @Autowired
    public MessageServiceImpl(MessageDAO dao) {
        this.dao = dao;
    }

    public List<Message> getAll() {
        return dao.getAllMessages();
    }

    public Message getById(Long id) {
        return dao.getMessage(id);
    }

    public Message createMessage(MessageRequest request) {
        LOGGER.debug("savig Message : ", request.getMessage());
        return dao.saveMessage(new Message(request.getMessage(), request.getClientId()));
    }

    public void updateMessage(Long id, MessageRequest request) {
        LOGGER.debug("updating Message with Id : ", id); 
        Message message = dao.getMessage(id);
        if (null != message) {
            message.setTitle(request.getMessage());
        } 
    }

    @Override
    public void deleteMessage(Long id) {
        dao.removeMessage(id);
    }

    @Override
    public boolean canUpdateDeleteMessage(Long id, String clientId) {
        Message message = dao.getMessage(id);
        if (null != message && message.getCreatedBy() == clientId) {
            return true;
        }
        return false;
    }

}
