package sebron4;

/**
 * Represents a credit account which allows withdrawals up to a defined credit limit.
 * @author Sebastian Rone, sebron-4
 */
public class CreditAccount extends Account {
    // Constant defining the maximum allowable negative balance (credit limit)
    private static final double CREDIT_LIMIT = -5000;
    // Interest rate for positive balance (1.1%)
    private static final double POSITIVE_BALANCE_INTEREST = 0.011;
    // Interest rate for negative balance (5%)
    private static final double NEGATIVE_BALANCE_INTEREST = 0.05;

    /**
     * Constructor to create a CreditAccount with a specified account number.
     * Initializes the account type and sets the initial balance to zero.
     *
     * @param accountNumber The account number for the new credit account.
     */
    public CreditAccount(int accountNumber) {
        super(accountNumber); // Call the superclass constructor to initialize account number
        this.accountType = "Kreditkonto"; // Set account type to "Kreditkonto" (Credit Account)
        this.balance = 0; // Initialize balance to zero
    }

    /**
     * Withdraws an amount from the credit account.
     *
     * @param amount The amount to withdraw.
     * @return true if the withdrawal was successful, false otherwise.
     */
    @Override
    public boolean withdraw(double amount) {
        if (amount < 0) {
            return false; // Reject negative withdrawal amounts
        }
        if (balance - amount < CREDIT_LIMIT) {
            return false; // Cannot withdraw beyond the credit limit
        }
        balance -= amount; // Update balance after successful withdrawal
        transactions.add(new Transaction("Withdraw", -amount, balance)); // Record the transaction
        return true; // Withdrawal successful
    }

    /**
     * Gets the current interest rate based on the account balance.
     *
     * @return The interest rate as a percentage (1.1% for zero or positive balance, 5.0% for negative balance).
     */
    @Override
    public double getInterestRate() {
        // Return 1.1% for zero balance and positive balance; otherwise, return 5.0% for negative balance
        if (balance >= 0) {
            return POSITIVE_BALANCE_INTEREST * 100; // Return interest rate for positive balance
        } else {
            return NEGATIVE_BALANCE_INTEREST * 100; // Return interest rate for negative balance
        }
    }

    /**
     * Closes the credit account and calculates final balance including any applicable interest.
     *
     * @return The final balance after applying interest.
     */
    @Override
    public double closeAccount() {
        // Calculate interest based on the current balance
        double interest = (balance >= 0) ? balance * POSITIVE_BALANCE_INTEREST : balance * NEGATIVE_BALANCE_INTEREST;
        balance += interest; // Update balance with interest
        return balance; // Return the final balance
    }
}
