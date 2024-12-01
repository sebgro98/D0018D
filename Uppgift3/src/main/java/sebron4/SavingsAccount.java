package sebron4;

/**
 * Represents a savings account that allows deposits, withdrawals, and interest accumulation.
 * This account type may have a fee for withdrawals after the first free withdrawal.
 * @author Sebastian Rone, sebron-4
 */
public class SavingsAccount extends Account {
    // Constants for withdrawal fee percentage and interest rate
    private static final double WITHDRAWAL_FEE_PERCENTAGE = 0.02; // 2% fee on withdrawals
    private static final double INTEREST_RATE = 0.024; // 2.4% interest rate for the account

    // Flag to determine if the first withdrawal is free of charge
    private boolean firstWithdrawalFree;

    /**
     * Constructs a SavingsAccount with the specified account number.
     *
     * @param accountNumber the account number for the savings account
     */
    public SavingsAccount(int accountNumber) {
        super(accountNumber); // Call to the parent class constructor
        this.accountType = "Sparkonto"; // Set the account type to "Savings Account"
        this.firstWithdrawalFree = true; // Allow the first withdrawal to be free
        this.balance = 0; // Initialize balance to 0
    }

    /**
     * Returns the interest rate of the savings account in percentage.
     *
     * @return the interest rate as a percentage (2.4%)
     */
    public double getInterestRate() {
        return INTEREST_RATE * 100; // Return interest rate in percentage for formatting
    }

    /**
     * Withdraws a specified amount from the savings account.
     * The withdrawal may incur a fee after the first free withdrawal.
     *
     * @param amount the amount to withdraw
     * @return true if the withdrawal is successful; false if the amount is invalid
     *         or insufficient funds are available
     */
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false; // Reject invalid withdrawal amounts (0 or negative)
        }

        double totalWithdrawalAmount = amount; // Total amount to be withdrawn

        // Check if there is a withdrawal fee to apply
        if (!firstWithdrawalFree) {
            totalWithdrawalAmount += amount * WITHDRAWAL_FEE_PERCENTAGE; // Apply fee if applicable
        }

        // Ensure there's enough balance to cover the withdrawal amount and any fees
        if (totalWithdrawalAmount <= balance) {
            balance -= totalWithdrawalAmount; // Deduct the total withdrawal amount from the balance

            // Log the transaction for the total amount deducted
            transactions.add(new Transaction("Withdrawal", -totalWithdrawalAmount, balance));

            // Update the first withdrawal flag if it's the first withdrawal
            if (firstWithdrawalFree) {
                firstWithdrawalFree = false; // Subsequent withdrawals incur fees
            }

            return true; // Withdrawal successful
        }

        return false; // Not enough funds to withdraw
    }

    /**
     * Closes the savings account, adding accrued interest to the balance.
     *
     * @return the final balance including interest earned
     */
    @Override
    public double closeAccount() {
        double interest = balance * INTEREST_RATE; // Calculate interest based on current balance
        // Print account closure details
        System.out.printf("Savings Account closed. Balance: %.2f SEK, Interest: %.2f SEK\n", balance, interest);
        balance += interest; // Add interest when closing the account
        return balance; // Return the final balance including interest
    }
}
