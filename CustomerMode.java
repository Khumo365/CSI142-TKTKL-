import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class CustomerMode {
    public static void start(List<Product> products, List<Order> orders, Scanner scanner) {
        System.out.println("\n[Customer Mode]");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        Customer customer = new Customer(orders.size() + 1, firstName, lastName, email, phone);

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - P" + p.getPrice() + " [Stock: " + p.getStockQuantity() + "]");
        }

        System.out.print("Enter the product number to buy: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < products.size()) {
            Product selected = products.get(index);
            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();
            scanner.nextLine();

            if (qty <= selected.getStockQuantity()) {
                selected.setStockQuantity(selected.getStockQuantity() - qty);
                
                // Create the order
                Order order = new Order(orders.size() + 1, customer, new Date(), "Placed");
                order.addItem(new OrderItem(1, selected, qty, selected.getPrice()));
                
                // Payment process
                System.out.print("Enter payment method (e.g., Credit Card, PayPal): ");
                String paymentMethod = scanner.nextLine();
                Payment payment = new Payment(1, selected.getPrice() * qty, paymentMethod);
                
                if (payment.processPayment()) {
                    order.setPayment(payment);
                    orders.add(order);
                    System.out.println("Purchase successful! Payment processed.");
                } else {
                    System.out.println("Payment failed.");
                }
            } else {
                System.out.println("Not enough stock.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
}