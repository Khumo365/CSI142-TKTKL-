import java.util.regex.Pattern;

// Worker 2: PayPal (like paying with an email)
public class PayPal extends Payment {
    private String email; // The email to send money

    // Rule to check the email
    private static final String EMAIL_RULE = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // Set up PayPal with the email
    public PayPal(int id, double amount, String email, String currency) {
        super(id, amount, "PayPal", currency); 
        this.email = email;
    }

    
    @Override
    public boolean processPayment(PaymentProcessor processor) {
    
        if (!Pattern.matches(EMAIL_RULE, email)) {
            System.out.println("Enter a valid email for PayPal.");
            setStatus("Failed");
            return false;
        }

        
        boolean worked = processor.process(this);
        setStatus(worked ? "Completed" : "Failed");
        return worked;
    }
}