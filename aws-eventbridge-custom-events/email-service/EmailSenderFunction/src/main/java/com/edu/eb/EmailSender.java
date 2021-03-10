package com.edu.eb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import schema.edu_orders.orderconfirmed.AWSEvent;
import schema.edu_orders.orderconfirmed.OrderConfirmed;
import schema.edu_orders.orderconfirmed.marshaller.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for EventBridge invocations of a Lambda function target
 */
public class EmailSender implements RequestStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    static final String NEW_DETAIL_TYPE = "HelloWorldFunction updated event of %s";

    private Object handleEvent(final AWSEvent<OrderConfirmed> inputEvent, final Context context) {
	    if (inputEvent != null) {
            OrderConfirmed confirmation = inputEvent.getDetail();

            logger.info(String.format("Order ID: %s, Customer: %s No.of Items: %d",
                    confirmation.getId(),
                    confirmation.getCustomer().toString(),
                    confirmation.getItems().size()));

            //Developers write your event-driven business logic code here, such as...
            inputEvent.setDetailType(String.format(NEW_DETAIL_TYPE, inputEvent.getDetailType()));

            return inputEvent;
        }

        throw new IllegalArgumentException("Unable to deserialize lambda input event to AWSEvent<OrderConfirmed>. Check that you have the right schema and event source.");
    }

    /**
     * Handles a Lambda Function request
     * @param input The Lambda Function input stream
     * @param output The Lambda function output stream
     * @param context The Lambda execution environment context object.
     * @throws IOException
     */
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        AWSEvent<OrderConfirmed> event = Marshaller.unmarshalEvent(input, OrderConfirmed.class);

        Object response = handleEvent(event, context);

        Marshaller.marshal(output, response);
    }
}
