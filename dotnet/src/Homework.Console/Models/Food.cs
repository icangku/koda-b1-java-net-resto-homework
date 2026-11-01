namespace Homework.Console.Models;

public class Food
{
  public string Id {get; private set;}
  public string Name {get; private set;}
  public decimal Price {get; private set;}
  public int Stock {get; private set;}
  public Food(string id, string name, decimal price, int stock)
  {
    if (string.IsNullOrWhiteSpace(id))
    {
      throw new ArgumentException("Id cannot be empty");
    }

    if (string.IsNullOrWhiteSpace(name))
    {
      throw new ArgumentException("Name cannot be empty");
    }

    if(price < 0)
    {
      throw new ArgumentException("Price should be greater than 0");
    }

    if(stock < 0)
    {
      throw new ArgumentException("Price cannot be lower than 0");
    }

    Id = id;
    Name = name;
    Price = price;
    Stock = stock;
  }

  public bool canFulfill(int quantity)
  {
    return Stock >= quantity;
  }

  public void reduceStock(int quantity)
  {
    if(quantity < 0)
    {
      throw new ArgumentException("Quantity should be greater than 0");
    }

    if (!canFulfill(quantity))
    {
      throw new ThreadStateException("Insufficient stock");
    }

    Stock -= quantity;
  }

  public override bool Equals(object? obj)
  {
    return base.Equals(obj);
  }

  public override int GetHashCode()
  {
    return base.GetHashCode();
  }
}