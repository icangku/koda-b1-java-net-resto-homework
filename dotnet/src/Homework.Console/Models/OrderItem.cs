namespace Homework.Console.Models;

public class OrderItem
{
  public Food Food {get; set;}
  public int Quantity {get; set;}
  public decimal PriceAtPurchase {get; set;}

  public OrderItem(Food food, int qty)
  {
    if(food == null)
    {
      throw new ArgumentException("Food cannot be null");
    }

    if(qty < 0)
    {
      throw new ArgumentException("Qty cannot be under 0");
    }

    Food = food;
    Quantity = qty;
    PriceAtPurchase = food.Price;
  }

  public decimal SubTotal()
  {
    return PriceAtPurchase * Quantity;
  }
}