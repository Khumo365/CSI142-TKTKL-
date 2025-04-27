import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Payment {
    private final long id;
    private final double amount;
    private final String method; // e.g., "Credit Card", "PayPal"
    private boolean successful;
    // Credit card-specific fields
    private final String cardNumber; // Nullable for non-credit card methods
    private final String cardHolder; // Nullable
    private final String expiryDate; // Format: MM/yy, nullable
    private final String cvv; // Nullable
    private final String currency; // Added for compatibility

    public Payment(long id, double amount, String method, String cardNumber, String cardHolder, String expiryDate, String cvv, String currency) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.successful = false;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.currency = currency != null ? currency : "USD";
    }

    public boolean processPayment(PaymentProcessor processor) {
        // Delegate to PaymentProcessor for processing
        PaymentResult result = processor.processPayment(this);
        this.successful = result.getStatus().equals("SUCCESS");
        return this.successful;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getCurrency() {
        return currency;
    }
}

class PaymentProcessor {
    private String transactionId;
    private String status;

    public PaymentProcessor() {
        this.transactionId = null;
        this.status = "PENDING";
    }

    public boolean validatePayment(Payment payment) {
        try {
            // Amount validation (applies to all methods)
            if (payment.getAmount() <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }

            // Credit card-specific validation
            if ("Credit Card".equalsIgnoreCase(payment.getMethod())) {
                // Card number validation (16 digits, numeric)
                if (payment.getCardNumber() == null || !payment.getCardNumber().matches("\\d{16}")) {
                    throw new IllegalArgumentException("Invalid card number");
                }

                // Expiry date validation (MM/yy format, not expired)
                if (payment.getExpiryDate() == null) {
                    throw new IllegalArgumentException("Expiry date required");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
                LocalDate expiry = LocalDate.parse(payment.getExpiryDate(), formatter).withDayOfMonth(1);
                if (expiry.isBefore(LocalDate.now().withDayOfMonth(1))) {
                    throw new IllegalArgumentException("Card expired");
                }

                // CVV validation (3-4 digits)
                if (payment.getCvv() == null || !payment.getCvv().matches("\\d{3,4}")) {
                    throw new IllegalArgumentException("Invalid CVV");
                }
            }

            return true;

        } catch (DateTimeParseException e) {
            System.out.println("Validation error: Invalid expiry date format");
            this.status = "FAILED";
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            this.status = "FAILED";
            return false;
        }
    }

    public PaymentResult processPayment(Payment payment) {
        // Step 1: Validate payment details
        if (!validatePayment(payment)) {
            return new PaymentResult(this.status, null, "Payment validation failed", payment.getAmount(), payment.getCurrency(), payment.getId());
        }

        // Step 2: Simulate external payment gateway call
        try {
            this.transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
            this.status = callPaymentGateway(payment);

            // Step 3: Return transaction result
            String message = this.status.equals("SUCCESS") ? "Payment processed successfully" : "Payment declined";
            return new PaymentResult(
                this.status,
                this.transactionId,
                message,
                payment.getAmount(),
                payment.getCurrency(),
                payment.getId()
            );

        } catch (RuntimeException e) {
            this.status = "FAILED";
            return new PaymentResult(this.status, this.transactionId, "Payment processing failed: " + e.getMessage(), payment.getAmount(), payment.getCurrency(), payment.getId());
        }
    }

    private String callPaymentGateway(Payment payment) {
        // Simulate network delay or failure (10% failure rate)
        Random random = new Random();
        if (random.nextDouble() < 0.1) {
            throw new RuntimeException("Gateway timeout");
        }

        // Simulate gateway approval logic (90% success rate)
        return random.nextDouble() < 0.9 ? "SUCCESS" : "DECLINED";
    }
}

class PaymentResult {
    private final String status;
    private final String transactionId;
    private final String message;
    private final double amount;
    private final String currency;
    private final long paymentId;
    private final String timestamp;

    public PaymentResult(String status, String transactionId, String message, double amount, String currency, long paymentId) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
        this.amount = amount;
        this.currency = currency;
        this.paymentId = paymentId;
        this.timestamp = LocalDate.now().atTime(10, 0).toString();
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"status\": \"%s\", \"transaction_id\": \"%s\", \"message\": \"%s\", \"amount\": %.2f, \"currency\": \"%s\", \"payment_id\": %d, \"timestamp\": \"%s\"}",
            status, transactionId != null ? transactionId : "null", message, amount, currency != null ? currency : "null", paymentId, timestamp
        );
    }
}

class PaymentApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PaymentProcessor processor = new PaymentProcessor();

        // Collect payment details from user
        System.out.print("Enter payment ID: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Using default ID 1001.");
            id = 1001L;
        }

        System.out.print("Enter payment amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Using default amount 0.0.");
            amount = 0.0;
        }

        System.out.print("Enter payment method (e.g., Credit Card, PayPal): ");
        String method = scanner.nextLine().trim();
        if (method.isEmpty()) {
            method = "Credit Card";
            System.out.println("Method not provided. Defaulting to Credit Card.");
        }

        String cardNumber = null;
        String cardHolder = null;
        String expiryDate = null;
        String cvv = null;
        if ("Credit Card".equalsIgnoreCase(method)) {
            System.out.print("Enter card number (16 digits): ");
            cardNumber = scanner.nextLine().trim();

            System.out.print("Enter card holder name: ");
            cardHolder = scanner.nextLine().trim();

            System.out.print("Enter expiry date (MM/yy): ");
            expiryDate = scanner.nextLine().trim();

            System.out.print("Enter CVV (3-4 digits): ");
            cvv = scanner.nextLine().trim();
        }

        System.out.print("Enter currency (e.g., USD, EUR) [default: USD]: ");
        String currency = scanner.nextLine().trim();
        if (currency.isEmpty()) {
            currency = "USD";
        }

        // Create Payment object
        Payment payment = new Payment(id, amount, method, cardNumber, cardHolder, expiryDate, cvv, currency);

        // Process payment
        PaymentResult result = processor.processPayment(payment);
        System.out.println("Payment Result: " + result);
        System.out.println("Payment Successful: " + payment.isSuccessful());

        scanner.close();
    }
}
