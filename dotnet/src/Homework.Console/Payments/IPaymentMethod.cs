namespace Homework.Console.Payments;

public interface IPaymentMethod
{
  void Pay(decimal amount);
}