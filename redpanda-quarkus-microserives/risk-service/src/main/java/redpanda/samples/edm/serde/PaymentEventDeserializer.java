package redpanda.samples.edm.serde;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PaymentEventDeserializer extends ObjectMapperDeserializer<PaymentReceivedEvent> {

  public PaymentEventDeserializer() {
    super(PaymentReceivedEvent.class);
  }
}
