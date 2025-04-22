public class PhysicalProduct extends Product {
    private double weight;

    public PhysicalProduct(long id, String name, String description, double price, int stockQuantity, double weight) {
        super(id, name, description, price, stockQuantity);
        this.weight = weight;
    }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public String getProductType() {
        return "Physical";
    }
}