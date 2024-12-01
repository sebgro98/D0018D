package sebron4;

/**
 * The class Account will create an account object in which we have formating methods, getter and setters.
 * @author Sebastian Rone, sebron-4
 */

import java.text.NumberFormat;
import java.util.Locale;

public class Account {

    private int balance;
    private double interestRate = 2.4; // Default interest rate in percentage
    private int accountNumber;
    private static int id = 1000; // Static ID counter to ensure unique account numbers
    private final String accountType = "Sparkonto"; // Account type is fixed as "Sparkonto"

    // Default constructor
    public Account() {
        id++; // Increment static ID counter
        accountNumber = id; // Assign new account number
    }

    /**
     * Formats the account details including balance and interest rate.
     * @return A formatted string with account details
     */
    public String getFormattedAccountDetails() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        String balanceStr = currencyFormat.format(balance);

        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        percentFormat.setMaximumFractionDigits(1); // Set maximum decimal places for percentage
        String percentStr = percentFormat.format(interestRate / 100.0);

        return String.format("%d %s %s %s",
                accountNumber, balanceStr, accountType, percentStr);
    }

    /**
     * Gets the type of the account.
     * @return The account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Gets the current balance of the account.
     * @return The balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets a new balance for the account.
     * @param balance The new balance to set
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Gets the current interest rate of the account.
     * @return The interest rate in percentage
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Sets a new interest rate for the account.
     * @param interestRate The new interest rate to set
     */
    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Gets the unique account number.
     * @return The account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets a new account number.
     * @param accountNumber The new account number to set
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return getFormattedAccountDetails();
    }
}
