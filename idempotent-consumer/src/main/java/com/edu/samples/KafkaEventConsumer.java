package com.edu.samples;

import com.edu.samples.serde.OrderEvent;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class KafkaEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaEventConsumer.class);

    @Inject
    OrderEventHandler orderEventHandler;

    @Incoming("orders")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<Void> onOrderEvent(KafkaRecord<String, OrderEvent> message) {
        return CompletableFuture.runAsync(() -> {
            try {
                LOG.info("Event with ID {} received", message.getPayload().getEventId());

                orderEventHandler.onOrderEvent(message.getPayload());
                message.ack();
            } catch (Exception e) {
                LOG.error("Error while preparing shipment");
                throw e;
            }
        });
    }
}
