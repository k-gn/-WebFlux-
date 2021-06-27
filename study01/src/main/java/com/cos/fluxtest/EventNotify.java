package com.cos.fluxtest;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventNotify {

    private List<String> events = new ArrayList<>();

    private boolean change = false;

    public void add(String data) {
        events.add(data);
        change = true;
    }

    public List<String> getEvents() {
        return events;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }
}
