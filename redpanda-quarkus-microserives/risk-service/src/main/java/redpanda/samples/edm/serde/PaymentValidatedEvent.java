package redpanda.samples.edm.serde;

public class PaymentValidatedEvent {
  private int transactionID;
  private String paymentStatus;

  public PaymentValidatedEvent(int transactionId, String declined) {
  }

  public PaymentValidatedEvent() {
  }

  public int getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }
}
