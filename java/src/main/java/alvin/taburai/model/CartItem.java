package alvin.taburai.model;

import java.math.BigDecimal;

public class CartItem {
  public final Food food;
  public int quantity;

  public CartItem(Food food, int quantity){
    if (food == null) {
      throw new IllegalArgumentException("Food cannot be null");
    }

    if(quantity < 1){
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }

    this.food = food;
    this.quantity = quantity;
  }

  public Food getFood() {return food;}
  public int getQuantity() {return quantity;}

  public void increaseQuantity(int amount){
    if(amount < 1){
      throw new IllegalArgumentException("Amount must be at least 1");
    }

    this.quantity += amount;
  }

  public BigDecimal subtotal(){
    return food.getPrice().multiply(BigDecimal.valueOf(quantity));
  }
}
