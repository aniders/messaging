package com.sample.messagingservice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.messagingservice.controller.MessageController;
import com.sample.messagingservice.dto.Message;
import com.sample.messagingservice.dto.MessageRequest;
import com.sample.messagingservice.service.MessageService;
import com.sample.messagingservice.util.RequestValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MessageService messageServiceMock;

    @InjectMocks
    private MessageController messageController;

    private RequestValidator requestValidator;

    @Mock
    private Principal principal;

    BindingResult resultMock;

    @Mock
    private SecurityContext securityContext;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        requestValidator = new RequestValidator();
        messageController.setDefaultRequestValidator(requestValidator);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void getAllMessagesTest() throws Exception {
        Message m1 = Message.builder().id(112364L).title("Test Message 1").createTime(new Date()).createdBy("user1@test.com").build();
        Message m2 = Message.builder().id(112365L).title("Test Message 2").createTime(new Date()).createdBy("user1@test.com").build();

        List<Message> mock = Arrays.asList(new Message[] { m1, m2 });

        when(messageServiceMock.getAll()).thenReturn(mock);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages").accept(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].title", is("Test Message 1")));

    }

    @Test
    public void getMessagesNoHitsTest() throws Exception {
        List<Message> mock = new ArrayList<Message>();
        when(messageServiceMock.getAll()).thenReturn(mock);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages").accept(APPLICATION_JSON)).andExpect(status().isNoContent());

    }

    @Test
    public void getMessageByIdTest() throws Exception {
        Message m1 = Message.builder().id(11111L).title("Test Message 1").createTime(new Date()).createdBy("user1@test.com").build();

        when(messageServiceMock.getById(11111L)).thenReturn(m1);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/11111").accept(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("title", is("Test Message 1")));

    }

    @Test
    public void getMessageByIdNoHitsTest() throws Exception {
        when(messageServiceMock.getById(11111L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/11111").accept(APPLICATION_JSON)).andExpect(status().isNoContent());

    }

    @Test
    public void createMessageTest() throws Exception {
        MessageRequest request = MessageRequest.builder().message("Create this message").clientId("user1@test.com").build();
        String json = objectMapper.writeValueAsString(request);

        Message message = Message.builder().id(1L).title("Create this message").createTime(new Date()).createdBy("user1@test.com").build();
        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.createMessage(request)).thenReturn(message);

        mockMvc.perform(post("/messages").principal(principal).content(json).contentType(APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void createMessageBadRequestTest() throws Exception {
        MessageRequest request = MessageRequest.builder().message("").clientId("user1@test.com").build();
        String json = objectMapper.writeValueAsString(request);

        when(principal.getName()).thenReturn("user1@test.com");

        mockMvc.perform(post("/messages").principal(principal).content(json).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateMessageTest() throws Exception {
        MessageRequest request = MessageRequest.builder().message("Message is updated").clientId("user1@test.com").build();
        String json = objectMapper.writeValueAsString(request);
        Message m1 = Message.builder().id(123L).title(request.getMessage()).createTime(new Date()).createdBy(request.getClientId()).build();

        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(true);
        when(messageServiceMock.getById(123L)).thenReturn(m1);

        mockMvc.perform(MockMvcRequestBuilders.put("/messages/123").principal(principal).content(json).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("title", is("Message is updated")));

    }

    @Test
    public void updateMessageBadRequestTest() throws Exception {
        MessageRequest request = MessageRequest.builder().message("").clientId("user1@test.com").build();
        String json = objectMapper.writeValueAsString(request);

        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/messages/123").principal(principal).content(json).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateMessageForiddenTest() throws Exception {

        MessageRequest request = MessageRequest.builder().message("Message is updated").clientId("user2@test.com").build();
        String json = objectMapper.writeValueAsString(request);
        Message m1 = Message.builder().id(123L).title(request.getMessage()).createTime(new Date()).createdBy(request.getClientId()).build();

        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(false);
        when(messageServiceMock.getById(123L)).thenReturn(m1);

        mockMvc.perform(MockMvcRequestBuilders.put("/messages/123").principal(principal).content(json).contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    public void deleteMessageTest() throws Exception {
        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/123").principal(principal)).andExpect(status().isOk());

    }

    @Test
    public void deleteMessageForbiddenTest() throws Exception {
        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/123").principal(principal)).andExpect(status().isForbidden());

    }

    @Test
    public void deleteMessageBadRequestTest() throws Exception {
        when(principal.getName()).thenReturn("user1@test.com");
        when(messageServiceMock.canUpdateDeleteMessage(123L, "user1@test.com")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/xyz").principal(principal)).andExpect(status().isBadRequest());

    }
}
