package com.example.websocket.dto;

import com.example.websocket.constants.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {

    private String sender;
    private String receiver;
    private String message;
    private String date;
    private Status statis;
}
