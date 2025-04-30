// The machine that checks if payments work
public class PaymentProcessor {
    public boolean process(Payment payment) {
        // Pretend we’re checking with a bank or PayPal
        // Most of the time (9 out of 10), it works!
        double chance = Math.random(); // Pick a random number
        if (chance < 0.9) {
            System.out.println("Yay! Payment worked for " + payment.getPaymentMethod() + "!");
            return true;
        } else {
            System.out.println("Oh no! Payment didn't work for " + payment.getPaymentMethod() + ".");
            return false;
        }
    }
}