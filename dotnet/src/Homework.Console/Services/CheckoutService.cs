using Homework.Console.Models;
using Homework.Console.Payments;

namespace Homework.Console.Services;

public class CheckoutService()
{
  public Order Checkout(Cart cart, IPaymentMethod paymentMethod)
  {
    if(cart == null || cart.GetItems().Count == 0)
    {
      throw new ArgumentException("Cart cannot be empty");
    }

    if(paymentMethod == null)
    {
      throw new ArgumentException("Payment method cannot be null");
    }

    foreach(CartItem cartItem in cart.GetItems()){
      if (!cartItem.Food.canFulfill(cartItem.Quantity))
      {
        throw new ArgumentException("Insufficient stock");
      }
    }

    List<OrderItem> orderItems = [];
    foreach(CartItem cartItem in cart.GetItems())
    {
      orderItems.Add(new OrderItem(cartItem.Food, cartItem.Quantity));
    }

    Order order = new(orderItems, paymentMethod);
    order.CompletePayment();

    foreach(CartItem cartItem in cart.GetItems())
    {
      cartItem.Food.reduceStock(cartItem.Quantity);
    }

    return order;
  }
}