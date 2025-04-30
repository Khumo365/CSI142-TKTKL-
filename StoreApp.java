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

        Scanner kg = new Scanner(System.in);
        System.out.print("Please choose a mode (manager, cashier, customer): ");
        String mode = kg.nextLine();

        switch (mode.toLowerCase()) {
            case "manager":
                ManagerMode.start(products, kg);
                break;
            case "cashier":
                CashierMode.start(products, orders);
                break;
            case "customer":
                CustomerMode.start(products, orders, kg);
                break;
            default:
                System.out.println("Unknown mode. Use: manager, cashier, or customer");
        }

       // Sort products using Selection Sort
       Sorter.selectionSort(products);
       System.out.println("Products sorted by price using Selection Sort:");
       for (Product p : products) {
           System.out.println(p.getName() + " - P" + p.getPrice());
       }

       // Search for a product by name using Linear Search
    System.out.println("Enter product name:");
        String productName = kg.nextLine();
       int index = Sorter.linearSearch(products,productName);
       if (index != -1) {
           System.out.println("Product found: " + products.get(index).getName());
       } else {
           System.out.println("Product not found.");
       }

       // Sort products again for Binary Search
       Sorter.insertionSort(products);
       System.out.println("Enter searchPrice");
       double searchPrice = kg.nextDouble();
       int priceIndex = Sorter.binarySearch(products, searchPrice);
       if (priceIndex != -1) {
           System.out.println("Product found at price " + searchPrice + ": " + products.get(priceIndex).getName());
       } else {
           System.out.println("Product not found at price " + searchPrice);
       }
   }
}
