package alvin.taburai.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Food {
  private final String id;
  private  final String name;
  private final BigDecimal price;
  private int stock;

  public Food(String id, String name, BigDecimal price, int stock){
    if(id == null || id.isBlank()){
      throw new IllegalArgumentException("ID cannot be null or blank");
    }

    if(name == null || name.isBlank()){
      throw new IllegalArgumentException("Name cannot be null or blank");
    }

    if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
      throw new  IllegalArgumentException("Price must be greater than 0");
    }

    if(stock < 0){
      throw new  IllegalArgumentException("Stock cannot be negative");
    }

    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public String getId() { return id;}
  public String getName() { return name;  }
  public BigDecimal getPrice() { return price; }
  public int getStock() { return stock; }

  public boolean canFulfill(int quantity){
    return stock >= quantity;
  }

  public void reduceStock(int quantity){
    if (quantity < 1) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }

    if(!canFulfill(quantity)){
      throw new IllegalStateException("Insuffient stock");
    }

    stock -= quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Food other = (Food) o;
    return Objects.equals(id, other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
