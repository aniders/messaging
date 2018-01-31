package com.sample.messagingservice.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sample.messagingservice.dao.MessageDAO;
import com.sample.messagingservice.dto.Message;

@Component
public class MessageDAOImpl implements MessageDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDAOImpl.class);

    private HashMap<Long, Message> messageStore;

    public MessageDAOImpl() {
        messageStore = new HashMap<Long, Message>();
    }

    public Message saveMessage(Message message) {
        return messageStore.put(message.getId(), message);
    }

    public List<Message> getAllMessages() {
        return new ArrayList<Message>(messageStore.values());
    }

    public Message getMessage(Long id) {
        return messageStore.get(id);
    }

    public void removeMessage(Long id) {
        if (messageStore != null && !messageStore.isEmpty()) {
            messageStore.remove(id);
        }
    }

    public void updateMessage(Message message) {
        messageStore.put(message.getId(), message);

    }

}
