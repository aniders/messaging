package com.sample.messagingservice.dto;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties
@ToString
@Builder
@AllArgsConstructor
public class Message {

    private static final AtomicInteger count = new AtomicInteger(0);

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("createTime")
    private Date createTime;

    @JsonProperty("createdBy")
    private String createdBy;

    public Message(String title, String clientID) {
        this.title = title;
        this.createdBy = clientID;
        this.createTime = new Date();
        this.id = Long.valueOf(count.incrementAndGet());
    }
}
