import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class CustomerMode {
    // Email validation regex
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    // Botswana phone number regex
    // Matches: +26771234567, 71234567, 71-234-567, 71 234 567, +267 71 234 567
    private static final String PHONE_REGEX = "^(\\+267\\s?)?7[1-9][0-9][0-9][0-9][0-9][0-9][0-9]$";

    public static void start(List<Product> products, List<Order> orders, Scanner scanner) {
        System.out.println("\n[Customer Mode]");

        // Validate customer information
        String firstName = getValidName(scanner, "first name");
        if (firstName == null) return;

        String lastName = getValidName(scanner, "last name");
        if (lastName == null) return;

        String email = getValidEmail(scanner);
        if (email == null) return;

        String phone = getValidPhone(scanner);
        if (phone == null) return;

        Customer customer = new Customer(orders.size() + 1, firstName, lastName, email, phone);

        // Display available products
        if (products.isEmpty()) {
            System.out.println("No products available for purchase.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - P" + p.getPrice() + 
                              " [Stock: " + p.getStockQuantity() + "]");
        }

        // Validate product selection
        System.out.print("Enter the product number to buy (or 0 to cancel): ");
        int index;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == -1) {
                System.out.println("Purchase cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (index < 0 || index >= products.size()) {
            System.out.println("Invalid product selection.");
            return;
        }

        Product selected = products.get(index);

        // Validate quantity
        System.out.print("Enter quantity (max " + selected.getStockQuantity() + "): ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine());
            if (qty <= 0) {
                System.out.println("Quantity must be greater than 0.");
                return;
            }
            if (qty > selected.getStockQuantity()) {
                System.out.println("Requested quantity exceeds available stock.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a number.");
            return;
        }

        // Update stock
        selected.setStockQuantity(selected.getStockQuantity() - qty);

        // Create the order
        Order order = new Order(orders.size() + 1, customer, new Date(), "Placed");
        order.addItem(new OrderItem(1, selected, qty, selected.getPrice()));

        // Calculate and display order total
        double orderTotal = calculateOrderTotal(order);
        System.out.println("Order Total: P" + String.format("%.2f", orderTotal));

        Scanner kg = new Scanner(System.in);
        
        // Validate payment method
        String Payment = getValidPaymentMethod(kg.nextLine());
        
        System.out.print("Enter payment method (Credit Card, PayPal, Bank Transfer): ");
        String paymentMethod = kg.nextLine();

        System.out.print("Enter your card number: ");
        String cardNumber = kg.nextLine();

        System.out.print("Enter card holder name: ");
        String cardHolder = kg.nextLine();

        System.out.print("Enter expiry date (MM/yy): ");
        String expiryDate = kg.nextLine();

        System.out.print("Enter CVV (3-4 digits): ");
        String cvv = kg.nextLine();
        
        if (Payment == null) {
            // Revert stock if payment method is invalid
           selected.setStockQuantity(selected.getStockQuantity() + qty);
            return;
        } else {
            // Revert stock if payment fails
            selected.setStockQuantity(selected.getStockQuantity() + qty);
            System.out.println("Payment failed.");
        }
       
        // Call the Payment class independently
        
        Payment payment = new Payment(1, orderTotal, paymentMethod, cardNumber, cardHolder, expiryDate, cvv, "USD");
        PaymentProcessor processor = new PaymentProcessor();
        
        if (payment.processPayment(processor)) {
            System.out.println("Payment successful!");
        } else 
        System.out.println("Payment failed!");
        
        // Write order to file
            try {
                writeOrderToFile(order);
            } catch (IOException e) {
                System.out.println("Error writing order to file: " + e.getMessage());
            }
        } 


    // Method to calculate order total
    public static double calculateOrderTotal(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }
        return total;
    }

    // Method to write order details to file
    private static void writeOrderToFile(Order order) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("orders.txt", true))) {
            writer.println("Order ID: " + order.getId());
            writer.println("Customer: " + order.getCustomer().getFirstName() + " " + 
                         order.getCustomer().getLastName());
            writer.println("Email: " + order.getCustomer().getEmail());
            writer.println("Phone: " + order.getCustomer().getPhone());
            writer.println("Date: " + order.getOrderDate());
            writer.println("Status: " + order.getStatus());
            writer.println("Total Amount: " + String.format("%.2f", calculateOrderTotal(order)));
            
            writer.println("Items:");
            for (OrderItem item : order.getItems()) {
                writer.println("- " + item.getProduct().getName() + 
                            " | Quantity: " + item.getQuantity() + 
                            " | Price: " + item.getUnitPrice());
            }
            writer.println("----------------------------------------");
            writer.println();
        }
    }

    private static String getValidName(Scanner scanner, String fieldName) {
        System.out.print("Enter your " + fieldName + ": ");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            System.out.println(fieldName + " cannot be empty.");
            return null;
        }
        if (!input.matches("^[A-Za-z\\s-]+$")) {
            System.out.println(fieldName + " can only contain letters, spaces, or hyphens.");
            return null;
        }
        if (input.length() > 50) {
            System.out.println(fieldName + " is too long (max 50 characters).");
            return null;
        }
        return input;
    }

    private static String getValidEmail(Scanner scanner) {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return null;
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            System.out.println("Invalid email format.");
            return null;
        }
        if (email.length() > 25) {
            System.out.println("Email is too long (max 25 characters).");
            return null;
        }
        return email;
    }

    private static String getValidPhone(Scanner scanner) {
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();
        
        if (phone.isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return null;
        }
        if (!Pattern.matches(PHONE_REGEX, phone)) {
            System.out.println("Invalid Botswana phone number format. Use formats like +267 71 234 567, 71234567, or 71-234-567.");
            return null;
        }
        // Normalize phone number (remove spaces, hyphens, and optional country code for consistency)
        phone = phone.replaceAll("[\\s-]", "");
        if (phone.startsWith("+267")) {
            phone = phone.substring(4); // Remove +267 for storage
        }
        return phone;
    }

    private static String getValidPaymentMethod(String method) {
            System.out.print("Enter payment method (Credit Card, PayPal, Bank Transfer): ");
            Scanner kg = new Scanner(System.in);
            method = kg.nextLine();//.trim();
        
        if (method.isEmpty()) {
            System.out.println("Payment method cannot be empty.");
            return null;
        }
        // List of allowed payment methods
        String[] validMethods = {"Credit Card", "PayPal", "Bank Transfer"};
        for (String validMethod : validMethods) {
            if (method.equalsIgnoreCase(validMethod)) {
                return validMethod;
            }
        }
        System.out.println("Invalid payment method. Please choose Credit Card, PayPal, or Bank Transfer.");
        return null;
    }
}