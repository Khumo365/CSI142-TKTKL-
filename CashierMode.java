import java.util.List;

public class CashierMode {
    public static void start(List<Product> products, List<Order> orders) {
        System.out.println("\n[Cashier Mode]");
        System.out.println("Orders placed: " + orders.size());
        for (Order o : orders) {
            System.out.println("Order ID: " + o.getId() + ", Date: " + o.getOrderDate() + ", Items: " + o.getItems().size());
        }
    }
}