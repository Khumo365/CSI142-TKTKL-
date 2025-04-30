import java.util.List;
import java.util.Scanner;

public class ManagerMode {
    public static void start(List<Product> products) {
        Scanner kg = new Scanner(System.in);
        System.out.println("\n[Manager Mode]");
        System.out.print("Enter manager username: ");
        String username = kg.nextLine();
        System.out.print("Enter manager password: ");
        String password = kg.nextLine();

        if (!username.equals("manager") || !password.equals("admin@123")) {
            System.out.println("Access Denied.");
            return;
        }

        System.out.println("Total number of products: " + products.size());
        int totalStock = 0;
        for (Product p : products) {
            totalStock += p.getStockQuantity();
        }
        System.out.println("Total stock across all products: " + totalStock);

        System.out.print("Do you want to add new stock? (yes/no): ");
        String response = kg.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter product name: ");
            String name = kg.nextLine();
            System.out.print("Enter stock quantity to add: ");
            int qty = kg.nextInt();
            kg.nextLine();

            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(name)) {
                    p.setStockQuantity(p.getStockQuantity() + qty);
                    System.out.println("Stock updated.");
                    return;
                }
            }
            System.out.println("Product not found");
        } else if (response.equalsIgnoreCase("no")){
            System.out.println("No stock was added.");
        } else {
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }
        kg.close();
    }
}