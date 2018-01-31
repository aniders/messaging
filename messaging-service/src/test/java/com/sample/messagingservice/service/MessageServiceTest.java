package com.sample.messagingservice.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sample.messagingservice.dao.MessageDAO;
import com.sample.messagingservice.dto.Message;
import com.sample.messagingservice.dto.MessageRequest;
import com.sample.messagingservice.service.impl.MessageServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageDAO messageDAOMock;

    @BeforeClass
    public static void beforeClass() {

    }

    @Test
    public void getAllMessagesTest() {
        Message m1 = Message.builder().id(112364L).title("Oe").createTime(new Date()).createdBy("user1@test.com").build();
        Message m2 = Message.builder().id(112365L).title("Two").createTime(new Date()).createdBy("user1@test.com").build();
        Message m3 = Message.builder().id(112366L).title("Tree").createTime(new Date()).createdBy("user1@test.com").build();
        List<Message> mock = Arrays.asList(new Message[] { m1, m2, m3 });
        when(messageDAOMock.getAllMessages()).thenReturn(mock);
        List<Message> msgs = messageService.getAll();
        assertNotNull(msgs);
        assertEquals(3, msgs.size());
        assertEquals("Two", msgs.get(1).getTitle());
    }

    @Test
    public void canUpdateTest() {
        Message m2 = Message.builder().id(112365L).title("Two").createTime(new Date()).createdBy("user1@test.com").build();

        when(messageDAOMock.getMessage(112365L)).thenReturn(m2);
        boolean can = messageService.canUpdateDeleteMessage(112365L, "user1@test.com");
        assertTrue(can);
    }
    
    @Test
    public void canotUpdateTest() {
        Message m2 = Message.builder().id(112365L).title("Two").createTime(new Date()).createdBy("user1@test.com").build();

        when(messageDAOMock.getMessage(112365L)).thenReturn(m2);
        boolean can = messageService.canUpdateDeleteMessage(112365L, "user2@test.com");
        assertFalse(can);
    }

    @Test
    public void updateMessageTest() {
        Message m2 = Message.builder().id(112365L).title("Two").createTime(new Date()).createdBy("user1@test.com").build();

        when(messageDAOMock.getMessage(112365L)).thenReturn(m2);
        MessageRequest request = MessageRequest.builder().message("Updated message").clientId("user1@test.com").build();

        messageService.updateMessage(112365L, request);
        assertEquals("Updated message", m2.getTitle());
    }
    
    @Test
    public void createMessageTest() {
        MessageRequest request = MessageRequest.builder().message("New message").clientId("user1@test.com").build();
        Message m2 = Message.builder().id(112365L).title("New message").createTime(new Date()).createdBy("user1@test.com").build();

        when(messageDAOMock.saveMessage(any(Message.class))).thenReturn(m2);

        Message dMessage =  messageService.createMessage(request);
        assertEquals(m2.getTitle(), dMessage.getTitle());
        assertEquals(m2.getCreatedBy(), dMessage.getCreatedBy());
    }

}
