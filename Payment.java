public abstract class Payment {
    protected int id; 
    protected double amount;
    protected String paymentMethod; 
    protected String currency; 
    protected String status; 

    public Payment(int id, double amount, String paymentMethod, String currency) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.status = "Pending"; 
    }

    public abstract boolean processPayment(PaymentProcessor processor);

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }
}