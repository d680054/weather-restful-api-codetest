package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Message {

    private UUID msgId = UUID.randomUUID(); //the internal message code

    private String msg;

    //constructor with msg only
    public Message(String msg)
    {
        this.msg = msg;
    }
}
