package alvin.taburai.model;

import java.math.BigDecimal;

import alvin.taburai.payment.PaymentMethod;

public class QrisPayment implements PaymentMethod {
  private final String qrisId;

  public QrisPayment(String qrisId){
    this.qrisId = qrisId;
  }

  @Override 
  public void pay(BigDecimal amount){
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount to pay must be greater than 0");
    }

    System.out.println("Processing QRIS payment of " + amount + "...");
  }

  public String getQrisId(){
    return qrisId;
  }
}
