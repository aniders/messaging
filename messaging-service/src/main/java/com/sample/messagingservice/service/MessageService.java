package com.sample.messagingservice.service;

import java.util.List;

import com.sample.messagingservice.dto.Message;
import com.sample.messagingservice.dto.MessageRequest;

/**
 * 
 * @author arawo
 * 
 */

public interface MessageService {

    /**
     * Creates Message using message request object
     * 
     * @param request
     * @return
     */
    public Message createMessage(MessageRequest request);

    /**
     * Deletes Message with given id from Message store
     * 
     * @param message
     */
    public void deleteMessage(Long id);

    /**
     * Returns list of all the Messages from messages store
     * 
     * @return
     */
    public List<Message> getAll();

    /**
     * Returns Message having specified Id
     * 
     * @param id
     * @return
     */
    public Message getById(Long id);

    /**
     * Update Message text with new Message request
     * 
     * @param id
     * @param request
     */
    public void updateMessage(Long id, MessageRequest request);

    /**
     * Checks if Message with given Id can be updated by Client
     * 
     * @param id
     * @param string
     * @return
     */
    public boolean canUpdateDeleteMessage(Long id, String string);

}
