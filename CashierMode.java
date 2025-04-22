import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CashierMode {
    public static void start(List<Product> products, List<Order> orders) {
        System.out.println("\n[Cashier Mode]");
        
        // Read orders from file
        List<String[]> orderData = readOrdersFromFile();
        
        System.out.println("Orders placed: " + orderData.size());
        for (String[] orderInfo : orderData) {
            System.out.println("Order ID: " + orderInfo[0] + 
                             ", Date: " + orderInfo[1] + 
                             ", Items: " + orderInfo[2]);
        }
    }

    private static List<String[]> readOrdersFromFile() {
        List<String[]> orders = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            String orderId = null;
            String date = null;
            String itemCount = "0";
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: ")) {
                    orderId = line.substring(10).trim();
                } else if (line.startsWith("Date: ")) {
                    date = line.substring(6).trim();
                } else if (line.startsWith("Items:")) {
                    // Count items by reading lines until separator or empty line
                    itemCount = "0";
                    int count = 0;
                    while ((line = reader.readLine()) != null && 
                           !line.startsWith("---") && 
                           !line.isEmpty()) {
                        if (line.startsWith("- ")) {
                            count++;
                        }
                    }
                    itemCount = String.valueOf(count);
                    
                    // Add completed order to list
                    if (orderId != null && date != null) {
                        orders.add(new String[]{orderId, date, itemCount});
                    }
                    
                    // Reset for next order
                    orderId = null;
                    date = null;
                    itemCount = "0";
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading orders from file: " + e.getMessage());
        }
        
        return orders;
    }
}