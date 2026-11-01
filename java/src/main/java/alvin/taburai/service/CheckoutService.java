package alvin.taburai.service;

import java.util.ArrayList;
import java.util.List;

import alvin.taburai.model.Cart;
import alvin.taburai.model.CartItem;
import alvin.taburai.model.Food;
import alvin.taburai.model.Order;
import alvin.taburai.model.OrderItem;
import alvin.taburai.payment.PaymentMethod;

public class CheckoutService {
  public Order checkout(Cart cart, PaymentMethod paymentMethod) {
    if (cart == null || cart.getItems().size() == 0) {
      throw new IllegalArgumentException("Cart cannot be empty");
    }

    if (paymentMethod == null) {
      throw new IllegalArgumentException("Payment method cannot be null");
    }

    for (CartItem cartItem : cart.getItems()) {
      if (!cartItem.getFood().canFulfill(cartItem.getQuantity())) {
        throw new IllegalArgumentException("Sorry insufficient stock");
      }
    }

    List<OrderItem> orderItems = new ArrayList<>();
    for (CartItem cartItem : cart.getItems()) {
      orderItems.add(new OrderItem(cartItem.getFood(), cartItem.getQuantity()));
    }

    Order order = new Order(orderItems, paymentMethod);
    order.completePayment();

    for (CartItem cartItem : cart.getItems()) {
      cartItem.getFood().reduceStock(cartItem.getQuantity());
    }

    return order;
  }
}
