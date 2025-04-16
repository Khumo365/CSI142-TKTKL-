import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a customer
            Customer customer = new Customer(202456775, "Carter", "Morupisi", "cartermorupisi@gmail.com", "+267 78990345");
            System.out.println("Created customer: " + customer.getFirstName() + " " + customer.getLastName());

            // Create products
            PhysicalProduct laptop = new PhysicalProduct(1l, "Laptop", "Gaming Laptop", 30000.0, 10, 2.5);
            PhysicalProduct heavyBox = new PhysicalProduct(2L, "Heavy Box", "Industrial Equipment", 1000.0, 5, 15.0);
            List<Product> products = new ArrayList<>();
            products.add(laptop);
            products.add(heavyBox);
            System.out.println("Created product: " + laptop.getName() + ", Price: P" + laptop.getPrice() +
                    ", Weight: " + laptop.getWeight() + "kg");
            System.out.println("Created product: " + heavyBox.getName() + ", Price: P" + heavyBox.getPrice() +
                    ", Weight: " + heavyBox.getWeight() + "kg");

            // Create an order
            List<OrderItem> orderItems = new ArrayList<>();
            int requestedQuantity = 2;
            if (laptop.getStockQuantity() >= requestedQuantity) {
                OrderItem orderItem = new OrderItem(1L, laptop, requestedQuantity, laptop.getPrice());
                orderItems.add(orderItem);
            } else {
                throw new IllegalArgumentException("Insufficient stock for " + laptop.getName());
            }

            Order order = new Order(1L, customer, LocalDateTime.now(), "PENDING");
            order.setItems(orderItems);
            System.out.println("Created order ID: " + order.getId() + ", Status: " + order.getStatus() +
                    ", Items: " + order.getItems().size());

            // Apply discount to laptop (eligible)
            double discountPercentage = 10.0;
            double discountedPrice = laptop.applyDiscount(discountPercentage);
            System.out.println("Laptop discount (" + discountPercentage + "%): P" + discountedPrice +
                    (laptop.isDiscountEligible() ? " (eligible)" : " (ineligible)"));

            // Apply discount to heavy box (ineligible due to weight)
            discountedPrice = heavyBox.applyDiscount(discountPercentage);
            System.out.println("Heavy Box discount (" + discountPercentage + "%): P" + discountedPrice +
                    (heavyBox.isDiscountEligible() ? " (eligible)" : " (ineligible)"));

            // Test error case: order with insufficient stock
            try {
                List<OrderItem> invalidOrderItems = new ArrayList<>();
                int invalidQuantity = 20;
                if (laptop.getStockQuantity() <= invalidQuantity) {
                    invalidOrderItems.add(new OrderItem(2L, laptop, invalidQuantity, laptop.getPrice()));
                } else {
                    throw new IllegalArgumentException("Insufficient stock for " + laptop.getName());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}