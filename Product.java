public abstract class Product implements Discountable {
    private long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    protected Product(long id, String name, String description, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public double applyDiscount(double discountPercentage) {
        if (isDiscountEligible() && discountPercentage >= 0 && discountPercentage <= 100) {
            return price * (1 - discountPercentage / 100);
        }
        return price;
    }

    @Override
    public boolean isDiscountEligible() {
        return stockQuantity > 0;
    }

    public abstract String getProductType();
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
