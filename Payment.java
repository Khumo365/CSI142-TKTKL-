public class Payment {
    private long id;
    private double amount;
    private String method; // e.g., "Credit Card", "PayPal", etc.
    private boolean successful;

    public Payment(long id, double amount, String method) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.successful = false;
    }

    public boolean processPayment() {
        // Simulate payment processing logic
        // Here you can add actual payment gateway integration
        this.successful = true; // Assume payment is always successful for simplicity
        return successful;
    }

    public long getId() { return id; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public boolean isSuccessful() { return successful; }
}
