import java.util.*;
import java.util.Scanner;
import java.util.Date;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class CustomerMode {
    // Email validation regex
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    // Matches: +26771234567, 71234567, 71-234-567, 71 234 567, +267 71 234 567
    private static final String PHONE_REGEX = "^(\\+267\\s?)?7[1-9][0-9]{6}$|^7[1-9][0-9]{2}[\\s-][0-9]{3}[\\s-][0-9]{3}$";

    public static void start(List<Product> products, List<Order> orders, Scanner kg) {
        System.out.println("\n[Customer Mode]");
        String firstName;
        String lastName;
        String email;
        String phone;
        int index;
        int qty;
        double total;
        String confirm;
        String getValidPaymentMethod;
        boolean isValidPayment;
        String cardNumber;
        String cardHolder;
        String expiryDate;
        String cvv;
        String paypalEmail;
        String accountNumber;
        String bankName;

        // Validate customer information
        //Scanner kg = new Scanner(System.in);

        System.out.printf("Enter Your First Name: ");
        firstName = kg.nextLine();
        if (firstName.isEmpty()) {
            System.out.printf("First name cannot be empty.");
            return;
        }

        System.out.printf("Enter Your Last Name: ");
        lastName = kg.nextLine();
        if (lastName.isEmpty()) {
            System.out.println("Last name cannot be empty.");
            return;
        }

        System.out.printf("Enter Your Valid Email: ");
        email = kg.nextLine();
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.printf("Enter Your Valid Phone Numder: ");
        phone = kg.nextLine();
        if (phone.isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return;
        }
        if (!Pattern.matches(PHONE_REGEX, phone)) {
            System.out.println("Invalid Botswana phone number format. Use formats like +267 71 234 567, 71234567, or 71-234-567.");
            return;
        }

        
        phone = phone.replaceAll("[\\s-]", "");
        if (phone.startsWith("+267")) {
            phone = phone.substring(4); // Remove +267 for storage
        }

        
        if (products.isEmpty()) {
            System.out.println("No products available for purchase.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - P" + p.getPrice() + " [Stock: " + p.getStockQuantity() + "]");
        }

        System.out.print("Enter the product number to buy (or 0 to cancel): ");
        try {
            index = Integer.parseInt(kg.nextLine()) - 1;
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

        
        System.out.print("Enter quantity (max " + selected.getStockQuantity() + "): ");
        try {
            qty = Integer.parseInt(kg.nextLine());

            if (qty <= 0) {
                System.out.println("Quantity must be greater than 0.");
            }

            if (qty > selected.getStockQuantity()) {
                System.out.println("Requested quantity exceeds available stock.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a number.");
            return;
        }

        // Update stock
        selected.setStockQuantity(selected.getStockQuantity() - qty);

        // Create the order
        Customer customer = new Customer(orders.size() + 1, firstName, lastName, email, phone);

        Order order = new Order(orders.size() + 1, customer, new Date(), "Placed");
        order.addItem(new OrderItem(1, selected, qty, selected.getPrice()));

        
        total = 0.0;
        for (OrderItem item : order.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        System.out.println("\nOrder Summary:");
        System.out.println("Product: " + selected.getName());
        System.out.println("Quantity: " + qty);
        System.out.println("Total: P" + String.format("%.2f", total));
        System.out.print("Confirm purchase? (yes/no): ");
        confirm = kg.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Purchase cancelled.");
            selected.setStockQuantity(selected.getStockQuantity() + qty);
        }

        isValidPayment = false;
        String[] validMethods = {"Credit Card", "PayPal", "Bank Transfer"};
        do {
            System.out.printf("Enter payment method (Credit Card, PayPal, Bank Transfer): ");
            getValidPaymentMethod = kg.nextLine();

            if (getValidPaymentMethod.isEmpty()) {
                System.out.println("Payment method cannot be empty.");
                continue;
            }

            for (String validMethod : validMethods) {
                if (getValidPaymentMethod.equalsIgnoreCase(validMethod)) {
                    isValidPayment = true;
                    break;
                }
            }

            if (!isValidPayment) {
                System.out.println("Invalid payment method. Please choose Credit Card, PayPal, or Bank Transfer.");
            }
        }

        while (!isValidPayment);

        Payment payment;

        if (getValidPaymentMethod.equalsIgnoreCase("Credit Card")) {
            System.out.printf("Enter your card number: ");
            cardNumber = kg.nextLine();

            if (!cardNumber.matches("\\d{16}")) {
                System.out.println("Invalid card number. Must be 16 digits.");
                selected.setStockQuantity(selected.getStockQuantity() + qty);
            }

            System.out.printf("Enter card holder name: ");
            cardHolder = kg.nextLine();

            if (cardHolder.isEmpty()) {
                System.out.println("Card holder name cannot be empty.");
                selected.setStockQuantity(selected.getStockQuantity() + qty);
            }

            System.out.printf("Enter expiry date (MM/yy): ");
            expiryDate = kg.nextLine();

            if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                System.out.println("Invalid expiry date. Use MM/yy format.");
                selected.setStockQuantity(selected.getStockQuantity() + qty);
                return;
            }

            System.out.print("Enter CVV (3-4 digits): ");
            cvv = kg.nextLine();

            if (!cvv.matches("\\d{3,4}")) {
                System.out.println("Invalid CVV. Must be 3 or 4 digits.");
                selected.setStockQuantity(selected.getStockQuantity() + qty);
            }
            payment = new CreditCard(1, total, cardNumber, cardHolder, expiryDate, cvv, "BWP");

        } else if (getValidPaymentMethod.equalsIgnoreCase("PayPal")) {
            System.out.printf("Enter your PayPal email: ");
            paypalEmail = kg.nextLine();
            payment = new PayPal(1, total, paypalEmail, "BWP");

        } else{ // Bank Transfer
            System.out.print("Enter your account number (10-12 digits): ");
            accountNumber = kg.nextLine();

            System.out.print("Enter bank name: ");
            bankName = kg.nextLine();
            payment = new BankTransfer(1, total, accountNumber, bankName, "BWP");
        }

        PaymentProcessor processor = new PaymentProcessor();

        if (payment.processPayment(processor)) {
            System.out.println("Payment successful!");
            orders.add(order); 
            try {
                writeOrderToFile(order, total);
            } catch (IOException e) {
                System.out.println("Error writing order to file: " + e.getMessage());
            }
        } else {
            System.out.println("Payment failed!");
            selected.setStockQuantity(selected.getStockQuantity() + qty); 
        }

       // kg.close(); // Close the scanner
    }

    private static void writeOrderToFile(Order order, double total) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("orders.txt", true))) {
            writer.println("Order ID: " + order.getId());
            writer.println("Customer: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
            writer.println("Email: " + order.getCustomer().getEmail());
            writer.println("Phone: " + order.getCustomer().getPhone());
            writer.println("Date: " + order.getOrderDate());
            writer.println("Status: " + order.getStatus());
            writer.println("Total Amount: " + String.format("%.2f", total));
            writer.println("Items:");
            for (OrderItem item : order.getItems()) {
                writer.println("- " + item.getProduct().getName() + " | Quantity: " + item.getQuantity() + " | Price: " + item.getUnitPrice());
            }
            writer.println("----------------------------------------");
        }
    }
}