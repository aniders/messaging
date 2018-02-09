package com.sample.messagingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class Client {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    
}
