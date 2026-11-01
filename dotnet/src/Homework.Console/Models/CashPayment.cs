using Homework.Console.Payments;

namespace Homework.Console.Models;

public class CashPayment : IPaymentMethod
{
  private readonly decimal CashProvided;
  public CashPayment(decimal cashProvided)
  {
    if(cashProvided < 0)
    {
      throw new ArgumentException("Cash cannot be lower than 0");
    }
    CashProvided = cashProvided;
  }

  public void Pay(decimal amount)
  {
    if(amount <= 0)
    {
      throw new ArgumentException($"Amount to pay must be greater than 0");
    }

    if(CashProvided < amount)
    {
      throw new ArgumentException("Insufficient cash");
    }

    decimal change = CashProvided - amount;
    System.Console.WriteLine($"Paid with Cash. Change: Rp{change:N0}");
  }

}
