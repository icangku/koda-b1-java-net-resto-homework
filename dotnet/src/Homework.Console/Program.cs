using Homework.Console.Models;
using Homework.Console.Payments;
using Homework.Console.Services;


class Program
{
  static void Main(string[] args)
  {
    
    CheckoutService checkoutService = new CheckoutService();
    Cart cart = new Cart();

    List <Food> menu = new List<Food>()
    {
      new("F001", "Ayam Kecombrang", 15000, 10),
        new("F002", "Ayam Kecap", 12000, 5),
        new("F003", "Rendang", 50000, 20),
        new("F004", "Ayam Pop", 24000, 20),
        new("F005", "Jus Melon Nanas", 17000, 20)
    };

    Console.WriteLine("======== WELCOME TO TABURAI =======");
    bool running = true;

    while (running)
    {
      Console.WriteLine("\n--- MAIN MENU ----");
      Console.WriteLine("1. View Menu");
      Console.WriteLine("2. Search Menu");
      Console.WriteLine("3. View Cart (" + cart.count() + " items)");
      Console.WriteLine("4. Add Item to Cart");
      Console.WriteLine("5. Checkout");
      Console.WriteLine("6. Exit");
      Console.WriteLine("Choose an option: ");

      string choice = Console.ReadLine()?.Trim()!;

      switch (choice) {
        case "1":
          Console.WriteLine("\n---MENU---");
          Console.WriteLine("1. Cheap to Expensive");
          Console.WriteLine("2. Expensive ke Cheapest");
          string sortOpt = Console.ReadLine()?.ToString()!;

          List<Food> sortedMenu = sortOpt.Equals("2") ? menu.OrderByDescending(f => f.Price).ToList() : menu.OrderBy(f => f.Price).ToList();
          var i = 0;
          foreach(Food food in sortedMenu)
          {
            i += 1;
            Console.WriteLine($"{i}. {food.Name} Rp{food.Price:N0} {food.Stock}");
          }
          break;

        case "2":
          Console.WriteLine("\n Enter the word you looking for: ");
          string keyword = Console.ReadLine()?.Trim().ToLower()!;

          List<Food> searchResults = menu
            .Where(f => f.Name.ToLower().Contains(keyword.ToLower()))
            .ToList();

          if (searchResults.Count == 0) {
            Console.WriteLine("Menu not found");
          } else {
            Console.WriteLine("\n Search Results");
            foreach(Food f in searchResults)
            {
              Console.WriteLine($"[{f.Id}] {f.Name} | Rp{f.Price:N0} | Stok: {f.Stock}");
            }
          }
          break;

        case "3":
          ManageCart(cart);
          break;

        case "4":
          Console.WriteLine("\n----- CHOOSE MENU------");
          var j = 0;
          foreach(Food f in menu)
            {
              j+=1;
              Console.WriteLine($"[{j}] {f.Name} | Rp{f.Price:N0} | Stok: {f.Stock}");
            }

          Console.WriteLine("Choose your menu ");
          try {
            int menuIndex = int.Parse(Console.ReadLine()?.Trim()!) - 1;
            if (menuIndex < 0 || menuIndex >= menu.Count) {
              Console.WriteLine("Menu not valid");
              break;
            }

            Food selectedFood = menu[menuIndex];
            Console.WriteLine("Enter your quantity: ");

            int qty = int.Parse(Console.ReadLine()?.Trim()!);
            cart.Add(new CartItem(selectedFood, qty));
            Console.WriteLine("Successfully add to new cart");
          } catch (FormatException e) {
            Console.WriteLine("Input must be a number"+e.Message);
          } catch (Exception e) {
            Console.WriteLine("Gagal menambah item: " + e.Message);
          }
          break;

        case "5":
          if (cart.GetItems().Count == 0) {
            Console.WriteLine("Cart is empty");
            break;
          }

          decimal totalCartAmount = cart.GetItems().Sum(f=>f.SubTotal());

          Console.WriteLine("\n--- CHECKOUT & PAYMENT-------");
          Console.WriteLine($"Total Your Checkout: Rp {totalCartAmount:N0}");
          Console.WriteLine("Choose your payment method: ");
          Console.WriteLine("1. Cash");
          Console.WriteLine("2. QRIS");
          string payChoice = Console.ReadLine()?.Trim()!;

          IPaymentMethod paymentMethod;
          try {
            if (payChoice.Equals("1")) {
              Console.WriteLine("Enter your amount: ");
              decimal cash = decimal.Parse(Console.ReadLine()?.Trim()!)!;
              paymentMethod = new CashPayment(cash);
            } else if (payChoice.Equals("2")) {
              paymentMethod = new QrisPayment("QRIS-TABURAI-001");
            } else {
              Console.WriteLine("Your payment method not valid.");
              break;
            }

            Order order = checkoutService.Checkout(cart, paymentMethod);
            PrintReceipt(order);
            running = false;

          } catch (Exception e) {
            Console.WriteLine("Transaction is failed: " + e.Message);
          }
          break;

        case "6":
          Console.WriteLine("Thanks for buying in TABURAI!");
          running = false;
          break;

        default:
          Console.WriteLine("Your choice not valid. Choose between 1-6.");
          break;
      }
    }
  }

  private static void ManageCart(Cart cart)
  {
    List<CartItem> items = cart.GetItems().ToList();
    if(items.Count == 0)
    {
      Console.WriteLine("\n Your cart is empty");
      return;
    }

    Console.WriteLine("\n ------CART -------");
    var i = 0;
    foreach(CartItem cartItem in items)
    {
      i += 1;
      Console.WriteLine($"{i}. {cartItem.Food.Name} | Harga: Rp{cartItem.Food.Price:N0} | Qty: {cartItem.Quantity} | Subtotal: Rp {cartItem.SubTotal():N0}");
    }

    decimal total = items.Sum(item => item.SubTotal());

    Console.WriteLine("-------------------------------------");
    Console.WriteLine($"Grand Total: Rp  {total:N0}");

    Console.WriteLine("\nManage Cart:");
    Console.WriteLine("1. Change Quatity Item");
    Console.WriteLine("2. Delete from cart");
    Console.WriteLine("3. Back to main menu");
    Console.WriteLine("Choose option: ");
    string opt = Console.ReadLine()?.Trim()!;

    try {
      if (opt.Equals("1")) {
        Console.WriteLine("Choose which item you wanna change: ");
        int index = int.Parse(Console.ReadLine()?.Trim()!) - 1;
        if (index >= 0 && index < items.Count) {
          CartItem target = items[index];
          Console.WriteLine("Enter new quantity: ");
          int newQty = int.Parse(Console.ReadLine()?.Trim()!);
          if (newQty < 1) {
            Console.WriteLine("Minimal 1.");
            return;
          }
          target.Quantity = newQty;
          Console.WriteLine("Quantity has been updated!");
        } else {
          Console.WriteLine("Item number not valid.");
        }
      } else if (opt.Equals("2")) {
        Console.WriteLine("Choose number item you want to delete: ");
        int index = int.Parse(Console.ReadLine()?.Trim()!) - 1;
        if (index >= 0 && index < items.Count) {
          cart.RemoveItem(index);
          Console.WriteLine("Item has been deleted from cart.");
        } else {
          Console.WriteLine("Item number not valid.");
        }
      }
    } catch (Exception e) {
      string errorMsg = (e.Message != null)
          ? e.Message
          : "Input tidak valid (" + e.GetType().Name + ")";

      Console.WriteLine("Terjadi kesalahan: " + errorMsg);
    }
  }

  private static void PrintReceipt(Order order) {
    Console.WriteLine("\n========================================");
    Console.WriteLine("             RECEIPT ORDER             ");
    Console.WriteLine("             TABURAI RESTO              ");
    Console.WriteLine("========================================");
    Console.WriteLine("Order ID  : " + order.Id);
    Console.WriteLine("Date   : " + order.CreatedAt);
    Console.WriteLine("Payment: " + order.PaymentMethod.GetType().Name);
    Console.WriteLine("----------------------------------------");
    Console.WriteLine("Order List:");

     foreach(OrderItem orderItem in order.Items)
    {
      Console.WriteLine($"\n{orderItem.Food.Name} {orderItem.Quantity} x Rp{orderItem.PriceAtPurchase:N0} = Rp{orderItem.SubTotal():N0}\n");
    }

    Console.WriteLine("----------------------------------------");
    Console.WriteLine($"GRAND TOTAL       : Rp {order.TotalAmount:N0}");
    Console.WriteLine("STATUS            : PAID");
    Console.WriteLine("========================================");
    Console.WriteLine("   Thanks for your visit!    ");
    Console.WriteLine("========================================");
  }
}