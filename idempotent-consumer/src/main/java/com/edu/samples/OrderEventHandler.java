package com.edu.samples;

import com.edu.samples.messagelog.MessageLog;
import com.edu.samples.serde.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandler.class);

    @Inject
    MessageLog log;

    @Transactional
    public void onOrderEvent(OrderEvent event) {
        String eventId = event.getEventId();

        if (log.alreadyProcessed(eventId)) {
            LOGGER.info("Event with eventID {} was already retrieved, ignoring it", eventId);
            return;
        }

        LOGGER.info("Received 'Order' event -- orderId: {}, customerId: '{}', status: '{}'",
                event.getPayload().getId(),
                event.getPayload().getCustomerId(),
                event.getPayload().getStatus()
        );

        log.processed(eventId);
    }

}
