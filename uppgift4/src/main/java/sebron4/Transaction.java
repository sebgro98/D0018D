package sebron4;

import java.io.Serial;
import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a financial transaction associated with an account.
 * @author Sebastian Rone, sebron-4
 */
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String type; // Type of the transaction (e.g., "Withdraw", "Deposit")
    private double amount; // Amount involved in the transaction
    private double balanceAfter; // Account balance after the transaction
    private LocalDateTime dateTime; // Date and time of the transaction

    /**
     * Constructs a Transaction object with the specified type, amount, and balance after the transaction.
     * Automatically sets the current date and time of the transaction.
     *
     * @param type The type of the transaction (e.g., "Withdraw", "Deposit").
     * @param amount The amount involved in the transaction.
     * @param balanceAfter The account balance after this transaction.
     */
    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type; // Initialize the transaction type
        this.amount = amount; // Initialize the transaction amount
        this.balanceAfter = balanceAfter; // Initialize the balance after the transaction
        this.dateTime = LocalDateTime.now(); // Automatically sets the current date and time
    }

    /**
     * Gets the amount involved in the transaction.
     *
     * @return The amount of the transaction.
     */
    public double getAmount() {
        return amount; // Return the transaction amount
    }

    /**
     * Gets the balance after the transaction.
     *
     * @return The account balance after this transaction.
     */
    public double getBalanceAfter() {
        return balanceAfter; // Return the balance after the transaction
    }

    /**
     * Gets the date and time when the transaction occurred.
     *
     * @return The date and time of the transaction.
     */
    public LocalDateTime getDateTime() {
        return dateTime; // Return the date and time of the transaction
    }

    /**
     * Returns a string representation of the transaction in a formatted manner.
     * The format includes the date and time, the transaction amount, and the balance after the transaction.
     *
     * @return A formatted string representation of the transaction.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Define the date format
        String dateStr = dateTime.format(formatter); // Format the date and time
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")); // Currency format for Swedish locale
        String amountStr = currencyFormat.format(amount); // Format the transaction amount
        String balanceStr = currencyFormat.format(balanceAfter); // Format the balance after the transaction
        return String.format("%s %s Saldo: %s", dateStr, amountStr, balanceStr); // Return the formatted string
    }
}
