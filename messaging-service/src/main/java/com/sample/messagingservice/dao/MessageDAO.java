package com.sample.messagingservice.dao;

import java.util.List;

import com.sample.messagingservice.dto.Message;

public interface MessageDAO {


    Message saveMessage(Message message);

    List<Message> getAllMessages();

    Message getMessage(Long id);

    void removeMessage(Long id);

    void updateMessage(Message message);

}
