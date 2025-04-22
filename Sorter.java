import java.util.List;

public class Sorter {
   
    // Selection Sort for sorting products by price
    public static void selectionSort(List<Product> products) {
        int n = products.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (products.get(j).getPrice() < products.get(minIndex).getPrice()) {
                    minIndex = j;
                }
            }
            // Swap the found minimum element with the first element
            Product temp = products.get(minIndex);
            products.set(minIndex, products.get(i));
            products.set(i, temp);
        }
    }

    // Insertion Sort for sorting products by price
    public static void insertionSort(List<Product> products) {
        int n = products.size();
        for (int i = 1; i < n; i++) {
            Product key = products.get(i);
            int j = i - 1;

            // Move elements of products[0..i-1], that are greater than key,
            // to one position ahead of their current position
            while (j >= 0 && products.get(j).getPrice() > key.getPrice()) {
                products.set(j + 1, products.get(j));
                j = j - 1;
            }
            products.set(j + 1, key);
        }
    }

    // Linear Search for a product by name
    public static int linearSearch(List<Product> products, String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(name)) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }

    // Binary Search for a product by price (requires sorted list)
    public static int binarySearch(List<Product> products, double price) {
        int left = 0;
        int right = products.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (products.get(mid).getPrice() == price) {
                return mid; // Return the index if found
            }
            if (products.get(mid).getPrice() < price) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }
        return -1; // Return -1 if not found
    }
}

