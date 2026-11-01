using Homework.Console.Payments;

namespace Homework.Console.Models;


public class Order
{
  public string Id {get; private set;}
  public List<OrderItem> Items {get; private set;}
  public decimal TotalAmount {get; private set;}
  public IPaymentMethod PaymentMethod {get; private set;}
  public DateTime CreatedAt {get;} = DateTime.Now;

  public Order(List<OrderItem> items, IPaymentMethod paymentMethod)
  {
    if(items == null || items.Count == 0)
    {
      throw new ArgumentException("Order items cannot be null");
    }

    if(paymentMethod == null)
    {
      throw new ArgumentException("Payment method cannot be null");
    }

    Id = Guid.NewGuid().ToString();
    Items = items.ToList();
    TotalAmount = CalculateTotal(items);
    PaymentMethod = paymentMethod;
  }

  public void CompletePayment()
  {
    PaymentMethod.Pay(TotalAmount);
  }

  private decimal CalculateTotal(List<OrderItem> orderItems)
  {
    decimal total = orderItems.Sum(item => item.SubTotal());
    return total;
  }
}