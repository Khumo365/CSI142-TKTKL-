import java.util.regex.Pattern;
import java.time.YearMonth;

public class CreditCard extends Payment {
    private String cardNumber; // The long number on the card
    private String cardHolder; // The name on the card
    private String expiryDate; // When the card stops working
    private String cvv; // A secret code on the back
    int month;
    int year;

    // Rules to check the card info
    private static final String CARD_NUMBER_RULE = "\\d{16}"; // Must be 16 numbers
    private static final String EXPIRY_DATE_RULE = "(0[1-12]|1[1-100])/\\d{4}"; // Like 12/25
    private static final String CVV_RULE = "\\d{3,4}"; // 3 or 4 numbers

    
    public CreditCard(int id, double amount, String cardNumber, String cardHolder, String expiryDate, String cvv, String currency) {
        super(id, amount, "Credit Card", currency); // Call the boss
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    // Check if the card works and process the payment
    @Override
    public boolean processPayment(PaymentProcessor processor) {
        // Check if the card number is 16 digits
        if (!Pattern.matches(CARD_NUMBER_RULE, cardNumber)) {
            System.out.println("Oops! The card number should be 16 digits.");
            setStatus("Failed");
            return false;
        }

        // Check if the name is not empty
        if (cardHolder.isEmpty()) {
            System.out.println("Oops! We need the name on the card.");
            setStatus("Failed");
            return false;
        }

        // Check if the expiry date is correct (like 12/25)
        if (!Pattern.matches(EXPIRY_DATE_RULE, expiryDate)) {
            System.out.println("Oops! The expiry date should be like MM/yy (e.g., 12/25).");
            setStatus("Failed");
            return false;
        }

        // Check if the CVV is 3 or 4 digits
        if (!Pattern.matches(CVV_RULE, cvv)) {
            System.out.println("Oops! The CVV should be 3 or 4 digits.");
            setStatus("Failed");
            return false;
        }

        // Check if the card is still good (not expired)
        String[] dateParts = expiryDate.split("/");
        month = Integer.parseInt(dateParts[0]);
        year = Integer.parseInt(dateParts[1]) + 2000; // Turn 25 into 2025
        YearMonth now = YearMonth.now();
        YearMonth expiry = YearMonth.of(year, month);
        if (expiry.isBefore(now)) {
            System.out.println("Oops! This card is too old and doesn’t work anymore.");
            setStatus("Failed");
            return false;
        }

        // Ask the machine to check the payment
        boolean worked = processor.process(this);
        setStatus(worked ? "Completed" : "Failed");
        return worked;
    }
}