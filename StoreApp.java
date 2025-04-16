import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class StoreApp {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        System.out.println("Welcome to Bazaar Electronics Store! ");

        products.add(new PhysicalProduct(1, "Laptop", "Gaming laptop", 30000, 5, 2.5));
        products.add(new PhysicalProduct(2, "TV", "Smart TV", 10000, 3, 7));
        products.add(new PhysicalProduct(3, "Washing Machine", "Heavy appliance", 5000, 2, 15));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please choose a mode (manager, cashier, customer): ");
        String mode = scanner.nextLine();

        switch (mode.toLowerCase()) {
            case "manager":
                ManagerMode.start(products, scanner);
                break;
            case "cashier":
                CashierMode.start(products, orders);
                break;
            case "customer":
                CustomerMode.start(products, orders, scanner);
                break;
            default:
                System.out.println("Unknown mode. Use: manager, cashier, or customer");
        }
    }
}