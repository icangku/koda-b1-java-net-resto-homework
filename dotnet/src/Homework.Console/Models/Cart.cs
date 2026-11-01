namespace Homework.Console.Models;

public class Cart
{
  private List<CartItem> Items {get; set;}
  
  public Cart()
  {
    Items = [];
  }

  public IReadOnlyCollection<CartItem> GetItems()
  {
    return Items.AsReadOnly();
  }

  public void RemoveItem(int index)
  {
    if(index < 0 || index > Items.Count)
    {
      throw new ArgumentException("Index not found");
    }

    Items.RemoveAt(index);
  }

  public void Add(CartItem cartItem)
  {
    if(cartItem == null)
    {
      throw new ArgumentException("Cart Item cannot be null");
    }

    Food food = cartItem.Food;
    int addedQty = cartItem.Quantity;

    int existingQty = 0;
    CartItem? existingItem = null;

    foreach(CartItem cartItemLoop in Items)
    {
      if (cartItemLoop.Food.Equals(food))
      {
        existingItem = cartItemLoop;
        existingQty = cartItemLoop.Quantity;
        break;
      }
    }

    int TotalDesireQty = existingQty + addedQty;
    if (!food.canFulfill(TotalDesireQty))
    {
      throw new ArgumentException("Insufficient stock");
    }

    if(existingItem != null)
    {
      existingItem.IncreaseQuantity(addedQty);
    }
    else
    {
      Items.Add(cartItem);
    }
  }

    public int count()
  {
    return Items.Count;
  }
}