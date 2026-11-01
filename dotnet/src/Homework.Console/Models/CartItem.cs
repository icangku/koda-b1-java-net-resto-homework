namespace Homework.Console.Models;

public class CartItem
{
  public Food Food {get; set;}
  public int Quantity {get; set;}

  public CartItem(Food food, int quantity)
  {
    if (quantity < 0)
    {
      throw new ArgumentException("Quantity should be greather than 0");
    }

    Food = food ?? throw new ArgumentException("Food cannot be null");
    Quantity = quantity;
  }

  public void IncreaseQuantity(int quantity)
  {
    if(quantity < 0)
    {
      throw new ArgumentException("Qty should be greater than 0");
    }

    Quantity += quantity;
  }

  public decimal SubTotal()
  {
    return Food.Price * Quantity;
  }
}