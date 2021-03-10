package com.edu.eb;

import schema.edu_orders.orderconfirmed.AWSEvent;
import schema.edu_orders.orderconfirmed.Customer;
import schema.edu_orders.orderconfirmed.Item;
import schema.edu_orders.orderconfirmed.OrderConfirmed;
import schema.edu_orders.orderconfirmed.marshaller.Marshaller;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventProducer {

    public static void main(String[] args) {
        Region region = Region.US_EAST_1;
        EventBridgeClient eventBrClient = EventBridgeClient.builder()
                .region(region)
                .build();

        try {
            String detailString = null;
            OrderConfirmed event = populateDummyEvent();

            try {
                detailString = Marshaller.marshal(event);
            } catch (IOException e) {
                //Failed to serialise the event as a JSON formatted string. Let's quit.
                e.printStackTrace();
                System.exit(1);
            }

            PutEventsRequestEntry reqEntry = PutEventsRequestEntry.builder()
                    .source("edu.svc.orders")
                    .detailType("Order Confirmed")
                    .detail(detailString)
                    .eventBusName("custom-event-bus")
                    .build();

            // Add the PutEventsRequestEntry to a list
            List<PutEventsRequestEntry> list = new ArrayList<PutEventsRequestEntry>();
            list.add(reqEntry);

            PutEventsRequest eventsRequest = PutEventsRequest.builder()
                    .entries(reqEntry)
                    .build();

            PutEventsResponse result = eventBrClient.putEvents(eventsRequest);

            for (PutEventsResultEntry resultEntry : result.entries()) {
                if (resultEntry.eventId() != null) {
                    System.out.println("Event Id: " + resultEntry.eventId());
                } else {
                    System.out.println("Injection failed with Error Code: " + resultEntry.errorCode());
                }
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        eventBrClient.close();
    }

    /**
     * Populates a dummy event of type @OrderConfirmed
     *
     * @return
     */
    private static OrderConfirmed populateDummyEvent() {
        OrderConfirmed confirmation = new OrderConfirmed();
        confirmation.setId(BigDecimal.valueOf(123456789));
        confirmation.setStatus("CONFIRMED");
        confirmation.setCurrency("USD");

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("aa@bb.cc");
        confirmation.setCustomer(customer);

        Item item = new Item();
        item.setSku(BigDecimal.valueOf(1));
        item.setName("Foo");
        item.setPrice(12.99);
        item.setQuantity(BigDecimal.valueOf(1));

        confirmation.addItemsItem(item);

        return confirmation;
    }
}