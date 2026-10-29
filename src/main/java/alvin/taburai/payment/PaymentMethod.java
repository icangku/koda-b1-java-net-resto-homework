package alvin.taburai.payment;

import java.math.BigDecimal;

public interface PaymentMethod {
  void pay(BigDecimal amount);
}
