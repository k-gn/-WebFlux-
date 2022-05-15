package com.example.spring_sse_websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTestController {

    @GetMapping("/chat")
    public String chatGET(){

        return "chat";
    }
}
