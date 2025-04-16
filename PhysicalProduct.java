public class PhysicalProduct extends Product {
    private double weight; // in kilograms

    // Constructor

    public PhysicalProduct(Long id, String name, String description, double price, int stockQuantity, double weight) {
        super(id, name, description, price, stockQuantity);
        this.weight = weight;
    }

    // Override to add weight-based eligibility (e.g., heavy items may not qualify)
    @Override
    public boolean isDiscountEligible() {
        return super.isDiscountEligible() && weight <= 10.0; // Eligible if in stock and not too heavy
    }

    // Getters and Setters
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}