package com.edu.samples.serde;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderEventDeserializer extends ObjectMapperDeserializer<OrderEvent> {

    public OrderEventDeserializer() {
        super(OrderEvent.class);
    }
}
