package alvin.taburai;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import alvin.taburai.model.Cart;
import alvin.taburai.model.CartItem;
import alvin.taburai.model.CashPayment;
import alvin.taburai.model.Food;
import alvin.taburai.model.Order;
import alvin.taburai.model.QrisPayment;
import alvin.taburai.payment.PaymentMethod;
import alvin.taburai.service.CheckoutService;

public class App {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    CheckoutService checkoutService = new CheckoutService();
    Cart cart = new Cart();

    List<Food> menu = List.of(
        new Food("F001", "Ayam Kecombrang", new BigDecimal("15000"), 10),
        new Food("F002", "Ayam Kecap", new BigDecimal("12000"), 5),
        new Food("F003", "Rendang", new BigDecimal("50000"), 20),
        new Food("F004", "Ayam Pop", new BigDecimal("24000"), 20),
        new Food("F005", "Jus Melon Nanas", new BigDecimal("17000"), 20));

    System.out.println("====== WELCOME TO TABURAI ========");
    boolean running = true;
    while (running) {
      System.out.println("\n--- MAIN MENU ----");
      System.out.println("1. View Menu");
      System.out.println("2. Search Menu");
      System.out.println("3. View Cart (" + cart.count() + " items)");
      System.out.println("4. Add Item to Cart");
      System.out.println("5. Checkout");
      System.out.println("6. Exit");
      System.out.println("Choose an option: ");

      String choice = scanner.nextLine().trim();

      switch (choice) {
        case "1":
          System.out.println("\n---MENU---");
          System.out.println("1. Cheap to Expensive");
          System.out.println("2. Expensive ke Cheapest");
          String sortOpt = scanner.nextLine().toString();

          List<Food> sortedMenu = menu.stream()
              .sorted(sortOpt.equals("2") ? Comparator.comparing(Food::getPrice).reversed()
                  : Comparator.comparing(Food::getPrice))
              .toList();

          IntStream.range(0, sortedMenu.size()).forEach(i -> {
            Food f = sortedMenu.get(i);
            System.out.printf("%d. [%s] %s - Rp %s (Stok: %d)\n", (i + 1), f.getId(), f.getName(), f.getPrice(),
                f.getStock());
          });
          break;

        case "2":
          System.out.println("\n Enter the word you looking for: ");
          String keyword = scanner.nextLine().trim().toLowerCase();

          List<Food> searchResults = menu.stream()
              .filter(f -> f.getName().toLowerCase().contains(keyword))
              .toList();

          if (searchResults.isEmpty()) {
            System.out.println("Menu not found");
          } else {
            System.out.println("\n Search Results");
            searchResults.forEach(f -> System.out.printf("[%s] %s | Harga: Rp  %s | Stok: %d\n", f.getId(), f.getName(),
                f.getPrice(), f.getStock()));
          }
          break;

        case "3":
          manageCart(cart, scanner);
          break;

        case "4":
          System.out.println("\n----- CHOOSE MENU------");
          IntStream.range(0, menu.size()).forEach(i -> {
            Food f = menu.get(i);
            System.out.printf("%d. %s - Rp %s (Stok: %d) \n", (i + 1), f.getName(), f.getPrice(), f.getStock());
          });

          System.out.println("Choose your menu ");
          try {
            int menuIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (menuIndex < 0 || menuIndex >= menu.size()) {
              System.out.println("Menu not valid");
              break;
            }

            Food selectedFood = menu.get(menuIndex);
            System.out.println("Enter your quantity: ");

            int qty = Integer.parseInt(scanner.nextLine().trim());
            cart.add(new CartItem(selectedFood, qty));
            System.out.println("Successfully add to new cart");
          } catch (NumberFormatException e) {
            System.out.println("Input must be a number");
          } catch (Exception e) {
            System.out.println("Gagal menambah item: " + e.getMessage());
          }
          break;

        case "5":
          if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty");
            break;
          }

          BigDecimal totalCartAmount = cart.getItems().stream()
              .map(CartItem::subtotal)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

          System.out.println("\n--- CHECKOUT & PAYMENT-------");
          System.out.println("Total Your Checkout: Rp " + totalCartAmount);
          System.out.println("Choose your payment method: ");
          System.out.println("1. Cash");
          System.out.println("2. QRIS");
          String payChoice = scanner.nextLine().trim();

          PaymentMethod paymentMethod;
          try {
            if (payChoice.equals("1")) {
              System.out.print("Enter your amount: ");
              BigDecimal cash = new BigDecimal(scanner.nextLine().trim());
              paymentMethod = new CashPayment(cash);
            } else if (payChoice.equals("2")) {
              paymentMethod = new QrisPayment("QRIS-TABURAI-001");
            } else {
              System.out.println("Your payment method not valid.");
              break;
            }

            Order order = checkoutService.checkout(cart, paymentMethod);
            printReceipt(order);
            running = false;

          } catch (Exception e) {
            System.out.println("Transaction is failed: " + e.getMessage());
          }
          break;

        case "6":
          System.out.println("Thanks for buying in TABURAI!");
          running = false;
          break;

        default:
          System.out.println("Your choice not valid. Choose between 1-6.");
      }
    }
  }

  private static void manageCart(Cart cart, Scanner scanner) {
    List<CartItem> items = cart.getItems();
    if (items.isEmpty()) {
      System.out.println("\nYour cart is empty.");
      return;
    }

    System.out.println("\n--- CART ---");
    IntStream.range(0, items.size()).forEach(i -> {
      CartItem item = items.get(i);
      System.out.printf("%d. %s | Harga: Rp %s | Qty: %d | Subtotal: Rp %s\n",
          (i + 1), item.getFood().getName(), item.getFood().getPrice(), item.getQuantity(), item.subtotal());
    });

    BigDecimal total = items.stream()
        .map(CartItem::subtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    System.out.println("----------------------------------------");
    System.out.println("Grand Total: Rp " + total);

    System.out.println("\nManage Cart:");
    System.out.println("1. Change Quatity Item");
    System.out.println("2. Delete from cart");
    System.out.println("3. Back to main menu");
    System.out.println("Choose option: ");
    String opt = scanner.nextLine().trim();

    try {
      if (opt.equals("1")) {
        System.out.println("Choose which item you wanna change: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index >= 0 && index < items.size()) {
          CartItem target = items.get(index);
          System.out.println("Enter new quantity: ");
          int newQty = Integer.parseInt(scanner.nextLine().trim());
          if (newQty < 1) {
            System.out.println("Minimal 1.");
            return;
          }
          target.quantity = newQty;
          System.out.println("Quantity has been updated!");
        } else {
          System.out.println("Item number not valid.");
        }
      } else if (opt.equals("2")) {
        System.out.println("Choose number item you want to delete: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index >= 0 && index < items.size()) {
          cart.removeItem(index);
          System.out.println("Item has been deleted from cart.");
        } else {
          System.out.println("Item number not valid.");
        }
      }
    } catch (Exception e) {
      String errorMsg = (e.getMessage() != null && !e.getMessage().isEmpty())
          ? e.getMessage()
          : "Input tidak valid (" + e.getClass().getSimpleName() + ")";

      System.out.println("Terjadi kesalahan: " + errorMsg);
    }
  }

  private static void printReceipt(Order order) {
    System.out.println("\n========================================");
    System.out.println("             RECEIPT ORDER             ");
    System.out.println("             TABURAI RESTO              ");
    System.out.println("========================================");
    System.out.println("Order ID  : " + order.getId());
    System.out.println("Date   : " + order.getCreatedAt());
    System.out.println("Payment: " + order.getPaymentMethod().getClass().getSimpleName());
    System.out.println("----------------------------------------");
    System.out.println("Order List:");

    order.getItems().forEach(item -> System.out.printf("- %s\n  %d x Rp %s = Rp %s\n",
        item.getFood().getName(),
        item.getQuantity(),
        item.getPriceAtPurchase(),
        item.subtotal()));

    System.out.println("----------------------------------------");
    System.out.println("GRAND TOTAL       : Rp " + order.getTotalAmount());
    System.out.println("STATUS            : PAID");
    System.out.println("========================================");
    System.out.println("   Thanks for your visit!    ");
    System.out.println("========================================");
  }
}
