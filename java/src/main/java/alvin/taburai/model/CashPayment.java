package alvin.taburai.model;

import java.math.BigDecimal;

import alvin.taburai.payment.PaymentMethod;

public class CashPayment implements PaymentMethod {
  private final BigDecimal cashProvided;

  public CashPayment(BigDecimal cashProvided) {
    if (cashProvided == null || cashProvided.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Cash provided cannot be negative");
    }
    this.cashProvided = cashProvided;
  }

  @Override
  public void pay(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Cannot be lower than 0");
    }

    if (cashProvided.compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient cash provided");
    }

    BigDecimal change = cashProvided.subtract(amount);
    System.out.println("Paid with Cash. Change: " + change);
  }

  public BigDecimal getCashProvided() {
    return cashProvided;
  }
}
