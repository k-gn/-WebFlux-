package com.example.spring_sse_websocket.util;

import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Component
public class UserChannel {
    private EmitterProcessor<String> processor;
    private Flux<String> flux;
    private FluxSink<String> sink;
    private Runnable closeCallback;

    public UserChannel() {
        processor = EmitterProcessor.create();
        this.sink = processor.sink();
        this.flux = processor
                .doOnCancel(() -> {
                    if (processor.downstreamCount() == 1) close();
                })
                .doOnTerminate(() -> {
                });
    }

    public void send(String message) {
        sink.next(message);
    }

    public Flux<String> toFlux() {
        return flux;
    }

    private void close() {
        if (closeCallback != null) closeCallback.run();
        sink.complete();
    }

    public UserChannel onClose(Runnable closeCallback) {
        this.closeCallback = closeCallback;
        return this;
    }
}