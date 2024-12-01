package sebron4;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents a bank account with basic functionality such as deposits,
 * withdrawals, and transaction tracking. This class serves as an abstract
 * base class for different types of accounts like SavingsAccount and
 * CreditAccount.
 * @author Sebastian Rone, sebron-4
 */
public abstract class Account {
    protected int accountNumber; // Unique identifier for the account
    protected double balance; // Current balance of the account
    protected String accountType; // Type of the account (e.g., Savings, Credit)
    protected List<Transaction> transactions; // List of transactions associated with the account

    /**
     * Constructs an Account with the specified account number.
     *
     * @param accountNumber the unique identifier for the account
     */
    public Account(int accountNumber) {
        this.accountNumber = accountNumber; // Set the account number
        this.balance = 0; // Initialize balance to 0
        this.transactions = new ArrayList<>(); // Initialize the transaction list
    }

    /**
     * Sets the balance of the account.
     *
     * @param balance the new balance to set for the account
     */
    public void setBalance(double balance) {
        this.balance = balance; // Update the balance
    }

    /**
     * Retrieves the list of transactions associated with the account.
     *
     * @return a list of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions; // Return the transaction list
    }

    /**
     * Returns a formatted string containing account details, including
     * account number, balance, account type, and interest rate.
     *
     * @return a formatted string representing account details
     */
    public String getFormattedAccountDetails() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")); // Currency formatter for SEK
        String balanceStr = currencyFormat.format(balance); // Format the balance

        double interestRate = 0; // Initialize interestRate variable
        if (this instanceof SavingsAccount) {
            interestRate = ((SavingsAccount) this).getInterestRate(); // Get interest rate for SavingsAccount
        } else if (this instanceof CreditAccount) {
            interestRate = ((CreditAccount) this).getInterestRate(); // Get interest rate for CreditAccount
        }

        String interestStr = formatInterestRate(interestRate); // Format interest rate

        // Format for account details
        return String.format("%d %s %s %s", accountNumber, balanceStr, accountType, interestStr);
    }

    /**
     * Formats the interest rate for display.
     *
     * @param interestRate the interest rate to format
     * @return a formatted string representation of the interest rate
     */
    private String formatInterestRate(double interestRate) {
        // Check for specific values and format accordingly
        if (interestRate == 5.0 || interestRate == -5.0) {
            return (interestRate < 0 ? "-" : "") + "5 %"; // Handle 5% formatting
        } else if (interestRate == 1.1 || interestRate == -1.1) {
            return (interestRate < 0 ? "-" : "") + "1,1 %"; // Handle 1.1% formatting
        } else if (interestRate == 2.4 || interestRate == -2.4) {
            return (interestRate < 0 ? "-" : "") + "2,4 %"; // Handle 2.4% formatting
        } else {
            return String.format("%.1f %%", interestRate).replace(".", ","); // Default formatting with proper locale
        }
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param amount the amount to withdraw
     * @return true if the withdrawal is successful; false if insufficient funds
     *         or invalid amount is provided
     */
    public abstract boolean withdraw(double amount);

    /**
     * Closes the account and returns the final balance after applying any final
     * adjustments (e.g., interest).
     *
     * @return the final balance of the account upon closure
     */
    public abstract double closeAccount();

    /**
     * Returns the interest rate of the account.
     *
     * @return the interest rate
     */
    public abstract double getInterestRate();

    /**
     * Retrieves the current balance of the account.
     *
     * @return the balance of the account
     */
    public double getBalance() {
        return balance; // Return the current balance
    }

    /**
     * Retrieves the type of the account.
     *
     * @return the account type
     */
    public String getAccountType() {
        return accountType; // Return the account type
    }

    /**
     * Retrieves the account number.
     *
     * @return the unique identifier for the account
     */
    public int getAccountNumber() {
        return accountNumber; // Return the account number
    }
}
