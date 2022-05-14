package com.example.spring_sse_websocket.controller;

import com.example.spring_sse_websocket.service.SSEService;
import com.example.spring_sse_websocket.util.UserChannels;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class SSEController {

//    @GetMapping("/{code}")
//    public Flux<ServerSentEvent<Stock>> stocks(@PathVariable("code") String code) {
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(t -> Stock.builder()
//                        .code(code)
//                        .value(randomValue())
//                        .build())
//                .map(stock -> ServerSentEvent.builder(stock).build());
//    }

    private int randomValue() {
        return ThreadLocalRandom.current().nextInt(1000) + 10000;
    }

    private UserChannels channels = new UserChannels();
    private AtomicInteger id = new AtomicInteger();

    @GetMapping("/{userId}/messages")
    public Flux<ServerSentEvent<String>> connect(@PathVariable("userId") Long userId) {
        int no = id.getAndAdd(1);
        Flux<String> userStream = channels.connect(userId).toFlux();
        Flux<String> tickStream = Flux.interval(Duration.ofSeconds(3))
                .map(tick -> "HEARTBEAT " + no);
        return Flux.merge(userStream, tickStream)
                .map(str -> ServerSentEvent.builder(str).build());
    }

    @PostMapping(path = "/{userId}/messages",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void send(@PathVariable("userId") Long userId,
                     @RequestBody String message) {
        channels.post(userId, message);
    }
}
