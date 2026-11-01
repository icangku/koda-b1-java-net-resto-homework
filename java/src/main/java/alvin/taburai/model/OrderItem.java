package alvin.taburai.model;

import java.math.BigDecimal;

public class OrderItem {
  public final Food food;
  public final int quantity;
  private final BigDecimal priceAtPurchase;

  public OrderItem(Food food, int qty) {
    if (food == null) {
      throw new IllegalArgumentException("Food cannot be null");
    }
    if (qty < 1) {
      throw new IllegalArgumentException("Quantity must be at least 1");
    }

    this.food = food;
    this.quantity = qty;
    this.priceAtPurchase = food.getPrice();
  }

  public Food getFood() {
    return food;
  }

  public BigDecimal getPriceAtPurchase() {
    return priceAtPurchase;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal subtotal() {
    return priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
  }
}
