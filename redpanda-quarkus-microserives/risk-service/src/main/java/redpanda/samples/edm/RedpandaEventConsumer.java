package redpanda.samples.edm;

import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redpanda.samples.edm.serde.PaymentReceivedEvent;
import redpanda.samples.edm.serde.PaymentValidatedEvent;


@ApplicationScoped
public class RedpandaEventConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(RedpandaEventConsumer.class);

  private List<String> flaggedCardNumbers = new ArrayList();

  @Incoming("flagged-txn")
  @Blocking
  public void onFlaggedTransaction(String cardNumber)
      throws InterruptedException {
    LOG.info(String.format("A flagged transaction received with card number %s", cardNumber));
    this.flaggedCardNumbers.add(cardNumber);
  }

  @Incoming("payments-in")
  @Outgoing("payments-out")
  @Blocking
  public PaymentValidatedEvent onPayment(PaymentReceivedEvent payment)
      throws InterruptedException {
    try {
      LOG.info(String.format("Payment received. Tx ID: %d, Card Number: %s, Amount: %d",
          payment.getTransactionId(),
          payment.getCardNumber(),
          payment.getAmount()
      ));

      String cardNumber = payment.getCardNumber();
      PaymentValidatedEvent validatedEvent = new PaymentValidatedEvent();
      validatedEvent.setTransactionID(payment.getTransactionId());

      if (flaggedCardNumbers.contains(cardNumber)) {
        LOG.info(String.format("!!!! PAYMENT DECLINED FOR CARD NUMBER", cardNumber));
        validatedEvent.setPaymentStatus("DECLINED");
      } else {
        LOG.info("Payment validated.");
        validatedEvent.setPaymentStatus("VALID");
      }
      return validatedEvent;
    } catch (Exception e) {
      LOG.error("Error while validating payment.");
      throw e;
    }
  }

}
