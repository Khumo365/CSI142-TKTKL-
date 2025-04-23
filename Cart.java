import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    // Add item to cart
    public void addItem(Product product, int quantity) {
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
    }

    // Remove item from cart
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    // Get total price
    public double getTotalPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // List cart contents
    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Items in your cart:");
            for (OrderItem item : items) {
                System.out.println("- " + item.getProduct().getName() +
                                   " x" + item.getQuantity() +
                                   " = R" + (item.getProduct().getPrice() * item.getQuantity()));
            }
            System.out.println("Total: R" + getTotalPrice());
        }
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
    }
}