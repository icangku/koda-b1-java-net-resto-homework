package alvin.taburai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
  private final List<CartItem> items;

  public Cart() {
    this.items = new ArrayList<>();
  }

  public List<CartItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void removeItem(int index) {
    if (index < 0 || index >= items.size()) {
      throw new IllegalArgumentException("Index not found");
    }
    items.remove(index);
  }

  public void add(CartItem cartItem) {
    if (cartItem == null) {
      throw new IllegalArgumentException("Cart Item cannot be null");
    }

    Food food = cartItem.getFood();
    int addedQuantity = cartItem.getQuantity();

    int existingQty = 0;
    CartItem existingItem = null;

    for (CartItem item : items) {
      if (item.getFood().equals(food)) {
        existingItem = item;
        existingQty = item.getQuantity();
        break;
      }
    }

    int totalDesireQuantity = existingQty + addedQuantity;

    if (!food.canFulfill(totalDesireQuantity)) {
      throw new IllegalArgumentException("Insufficient stock to add this quantity");
    }

    if (existingItem != null) {
      existingItem.increaseQuantity(addedQuantity);
    } else {
      items.add(cartItem);
    }
  }

  public int count() {
    return items.size();
  }
}
