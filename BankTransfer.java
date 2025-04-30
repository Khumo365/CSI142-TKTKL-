import java.util.regex.Pattern;

public class BankTransfer extends Payment {
    private String accountNumber; // The bank account number
    private String bankName; // The name of the bank

    private static final String ACCOUNT_NUMBER_RULE = "\\d{16}"; 

    // Set up the bank transfer with account info
    public BankTransfer(int id, double amount, String accountNumber, String bankName, String currency) {
        super(id, amount, "Bank Transfer", currency); // Call the boss
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    // Check if the bank info works and process the payment
    @Override
    public boolean processPayment(PaymentProcessor processor) {
        // Check if the account number is 10 to 12 digits
        if (!Pattern.matches(ACCOUNT_NUMBER_RULE, accountNumber)) {
            System.out.println("Oops! The account number should be 16 digits.");
            setStatus("Failed");
            return false;
        }

        // Check if the bank name is not empty
        if (bankName.isEmpty()) {
            System.out.println("Oops! We need the name of the bank.");
            setStatus("Failed");
            return false;
        }

        // Ask the magic machine to check the payment
        boolean worked = processor.process(this);
        setStatus(worked ? "Completed" : "Failed");
        return worked;
    }
}