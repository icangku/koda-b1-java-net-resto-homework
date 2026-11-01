using Homework.Console.Payments;

namespace Homework.Console.Models;

public class QrisPayment(string qrisId) : IPaymentMethod
{
  private readonly string QrisId = qrisId;

  public void Pay(decimal amount)
  {
    if(amount <= 0)
    {
      throw new ArgumentException($"Amount to pay must be greater than 0");
    }

    System.Console.WriteLine($"Processing QRIS payment of {amount:N0}...");
  }

}
