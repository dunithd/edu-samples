package com.edu.samples.serde;

import com.edu.samples.serde.Order;

public class OrderEvent {

    private String eventId;
    private String eventType;
    private Order payload;

    public OrderEvent() {
    }

    public OrderEvent(String eventId, String eventType, Order payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Order getPayload() {
        return payload;
    }

    public void setPayload(Order payload) {
        this.payload = payload;
    }
}
