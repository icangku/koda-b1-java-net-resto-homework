package alvin.taburai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import alvin.taburai.payment.PaymentMethod;

public class Order {
  private final String id;
  private final List<OrderItem> items;
  private final BigDecimal totalAmount;
  private final PaymentMethod paymentMethod;
  private final LocalDateTime createdAt;

  public Order(List<OrderItem> items, PaymentMethod paymentMethod) {
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("Order items cannot be null or empty");
    }
    if (paymentMethod == null) {
      throw new IllegalArgumentException("Payment method cannot be null");
    }

    this.id = UUID.randomUUID().toString();
    this.items = List.copyOf(items);
    this.totalAmount = calculateTotal(items);
    this.paymentMethod = paymentMethod;
    this.createdAt = LocalDateTime.now();
  }

  private BigDecimal calculateTotal(List<OrderItem> orderItems) {
    BigDecimal total = BigDecimal.ZERO;
    for (OrderItem orderItem : orderItems) {
      total = total.add(orderItem.subtotal());
    }
    return total;
  }

  public String getId() {
    return id;
  }

  public List<OrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void completePayment() {
    paymentMethod.pay(totalAmount);
  }
}
