package sebron4;

/**
 * The BankLogic class handles the core functionality of the banking system, including
 * customer management, account operations, and transaction processing.
 *
 * This class allows for operations such as creating and retrieving customers,
 * managing their accounts, and performing transactions like deposits and withdrawals.
 *
 * It also includes methods for formatting data and calculating interest based on account types.
 *
 * @author Sebastian Rone, sebron-4
 */

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BankLogic {
    private static int accountNumberCounter = 1000;
    private List<Customer> customers = new ArrayList<>(); // List of all customers in the bank

    /**
     * Retrieves a list of all customers formatted as strings.
     *
     * @return A list of formatted strings representing all customers.
     */
    public List<String> getAllCustomers() {
        List<String> strings = new ArrayList<>();
        for (Customer customer : customers) {
            strings.add(Objects.toString(customer));
        }
        return strings;
    }

    /**
     * Creates a new customer and adds them to the bank's customer list.
     *
     * @param name   The first name of the customer.
     * @param surname The last name of the customer.
     * @param pNo    The social security number of the customer.
     * @return true if the customer was successfully created,
     *         false if a customer with the same social security number already exists.
     */
    public boolean createCustomer(String name, String surname, String pNo) {
        for (Customer customer : customers) {
            if (Objects.equals(customer.getSocialSecurityNumber(), pNo)) {
                return false; // Customer with the same social security number already exists
            }
        }
        Customer customer = new Customer(name, surname, pNo);
        customers.add(customer);
        return true;
    }

    /**
     * Retrieves the details of a specific customer and their accounts.
     *
     * @param pNo The social security number of the customer.
     * @return A list of formatted strings representing the customer and their accounts,
     *         or null if the customer is not found.
     */
    public List<String> getCustomer(String pNo) {
        List<String> result = new ArrayList<>();

        // Find the customer with the given personal number
        Customer customer = customers.stream()
                .filter(c -> c.getSocialSecurityNumber().equals(pNo))
                .findFirst()
                .orElse(null);

        // If the customer doesn't exist, return null
        if (customer == null) {
            return null;
        }

        // Add customer info to the result list
        result.add(String.format("%s %s %s",
                customer.getSocialSecurityNumber(),
                customer.getName(),
                customer.getSirName()));

        // Handle account details
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));

        for (Account account : customer.getAccounts()) {
            String balanceStr = currencyFormat.format(account.getBalance());
            String interestRateStr = formatInterestRate(account.getInterestRate());

            // Add account details to the result list
            result.add(String.format("%d %s %s %s",
                    account.getAccountNumber(),
                    balanceStr,
                    account.getAccountType(),
                    interestRateStr));
        }

        return result; // Return the result list containing customer and account info
    }

    /**
     * Formats the interest rate for display.
     *
     * @param interestRate The interest rate to format.
     * @return A string representation of the formatted interest rate.
     */
    private String formatInterestRate(double interestRate) {
        // Check for specific values and format accordingly
        if (interestRate == 5.0 || interestRate == -5.0) {
            return (interestRate < 0 ? "-" : "") + "5 %"; // Convert 5.0 or -5.0 to 5 %
        } else if (interestRate == 1.1 || interestRate == -1.1) {
            return (interestRate < 0 ? "-" : "") + "1,1 %"; // Handle 1.1 % formatting
        } else if (interestRate == 2.4 || interestRate == -2.4) {
            return (interestRate < 0 ? "-" : "") + "2,4 %"; // Handle 2.4 % formatting
        } else {
            return String.format("%.1f %%", interestRate).replace(".", ","); // Default formatting with proper locale
        }
    }

    /**
     * Changes the name of a customer.
     *
     * @param name   The new first name of the customer (empty if not changing).
     * @param surname The new last name of the customer (empty if not changing).
     * @param pNo    The social security number of the customer.
     * @return true if the name was successfully changed,
     *         false if the customer was not found or both names are empty.
     */
    public boolean changeCustomerName(String pNo, String name, String surname) {
        if (name != null && surname != null && !name.isEmpty() && !surname.isEmpty()) {
            for (Customer customer : customers) {
                if (customer.getSocialSecurityNumber().equals(pNo)) {
                    customer.setName(name);
                    customer.setSirName(surname);
                    return true;
                }
            }
        }
        return false; // Customer not found or input is invalid
    }


    /**
     * Creates a new savings account for a customer.
     *
     * @param pNo The social security number of the customer.
     * @return The account number of the newly created account, or -1 if the customer was not found.
     */
    public int createSavingsAccount(String pNo) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                int newAccountNumber = ++accountNumberCounter;
                Account account = new SavingsAccount(newAccountNumber);
                customer.addAccount(account);
                return account.getAccountNumber();
            }
        }
        return -1; // Customer not found
    }

    /**
     * Creates a new credit account for a customer.
     *
     * @param pNo The social security number of the customer.
     * @return The account number of the newly created account, or -1 if the customer was not found.
     */
    public int createCreditAccount(String pNo) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                int newAccountNumber = ++accountNumberCounter;
                Account account = new CreditAccount(newAccountNumber);
                customer.addAccount(account);
                return account.getAccountNumber();
            }
        }
        return -1; // Customer not found
    }

    /**
     * Retrieves the details of a specific account belonging to a customer.
     *
     * @param pNo      The social security number of the customer.
     * @param accountId The ID of the account.
     * @return The formatted account details, or null if the customer or account was not found.
     */
    public String getAccount(String pNo, int accountId) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        return account.getFormattedAccountDetails();
                    }
                }
            }
        }
        return null; // Customer or account not found
    }

    /**
     * Deposits a specified amount into a customer's account.
     *
     * @param pNo      The social security number of the customer.
     * @param accountId The ID of the account.
     * @param amount    The amount to deposit (must be greater than 0).
     * @return true if the deposit was successful,
     *         false if the amount is invalid or the customer or account was not found.
     */
    public boolean deposit(String pNo, int accountId, int amount) {
        if (amount <= 0) {
            return false; // Invalid deposit amount
        }

        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        // Update balance
                        double newBalance = account.getBalance() + amount;
                        account.setBalance(newBalance);

                        // Record the transaction
                        account.getTransactions().add(new Transaction("Deposit", amount, newBalance));

                        return true; // Deposit successful
                    }
                }
            }
        }
        return false; // Customer or account not found
    }

    /**
     * Retrieves the transaction history for a specific account.
     *
     * @param pNo      The social security number of the customer.
     * @param accountId The ID of the account.
     * @return A list of formatted transaction details, or null if the customer or account was not found.
     */
    public List<String> getTransactions(String pNo, int accountId) {
        // Find the customer by their social security number
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                // Find the account by its account number
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        // Fetch the transactions for this account
                        List<Transaction> transactions = account.getTransactions();

                        // Return an empty list if there are no transactions
                        if (transactions.isEmpty()) {
                            return new ArrayList<>();
                        }

                        // Format the transaction details for return
                        List<String> transactionDetails = new ArrayList<>();
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));

                        for (Transaction transaction : transactions) {
                            String amountStr = currencyFormat.format(transaction.getAmount());
                            String balanceStr = currencyFormat.format(transaction.getBalanceAfter());

                            // Format: "2024-09-12 10:53:44 -500,00 kr Saldo: -500,00 kr"
                            transactionDetails.add(String.format("%s %s Saldo: %s",
                                    transaction.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                    amountStr,
                                    balanceStr));
                        }

                        return transactionDetails; // Return the formatted transaction details
                    }
                }
            }
        }

        return null; // Return null if the customer or account is not found
    }

    /**
     * Withdraws a specified amount from a customer's account.
     * @param pNo The social security number of the customer
     * @param accountId The ID of the account
     * @param amount The amount to withdraw (must be greater than 0)
     * @return true if the withdrawal was successful, false if the amount is invalid, insufficient balance, or the customer or account was not found
     */
    public boolean withdraw(String pNo, int accountId, double amount) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        return account.withdraw(amount);
                    }
                }
            }
        }
        return false; // Customer or account not found
    }


    /**
     * Closes an account and calculates the final interest amount.
     * @param pNo The social security number of the customer
     * @param accountId The ID of the account to close
     * @return A formatted string with account details and interest amount, or null if the customer or account was not found
     */
    public String closeAccount(String pNo, int accountId) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        double interestAmount;
                        if (account instanceof SavingsAccount) {
                            interestAmount = account.getBalance() * 0.024; // 2.4% interest for savings
                        } else if (account instanceof CreditAccount) {
                            if (account.getBalance() < 0) {
                                interestAmount = account.getBalance() * 0.05; // 5% interest for debt
                            } else {
                                interestAmount = account.getBalance() * 0.011; // 1.1% interest for positive balance
                            }
                        } else {
                            interestAmount = 0;
                        }

                        customer.getAccounts().remove(account);

                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
                        String balanceStr = currencyFormat.format(account.getBalance());
                        String interestStr = currencyFormat.format(interestAmount);

                        return String.format("%d %s %s %s",
                                accountId, balanceStr, account.getAccountType(), interestStr);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Deletes a customer and their accounts from the bank.
     *
     * @param pNo The social security number of the customer.
     * @return A list of strings where the first element is the customer info
     *         and the remaining elements are the details of the deleted accounts,
     *         or null if the customer was not found.
     */
    public List<String> deleteCustomer(String pNo) {
        List<String> result = new ArrayList<>(); // Initialize a list to hold the result

        // Find the customer with the given personal number (social security number)
        Customer customerToDelete = customers.stream()
                .filter(customer -> customer.getSocialSecurityNumber().equals(pNo)) // Filter customers by SSN
                .findFirst() // Get the first matching customer
                .orElse(null); // Return null if no customer is found

        // If the customer doesn't exist, return null
        if (customerToDelete == null) {
            return null; // Customer not found
        }

        // Add customer info to the result list
        result.add(String.format("%s %s %s",
                customerToDelete.getSocialSecurityNumber(), // Add SSN
                customerToDelete.getName(), // Add customer's first name
                customerToDelete.getSirName())); // Add customer's last name

        // Format currency for account balances and interest
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")); // Swedish locale for currency formatting

        // Iterate over a copy of the customer's accounts to avoid concurrent modification issues
        for (Account account : new ArrayList<>(customerToDelete.getAccounts())) {
            double interestAmount = 0.0; // Initialize interest amount

            // Check the account type and calculate interest accordingly
            if (account instanceof SavingsAccount) {
                SavingsAccount savingsAccount = (SavingsAccount) account; // Cast to SavingsAccount
                double interestRate = savingsAccount.getInterestRate(); // Get interest rate
                interestAmount = savingsAccount.getBalance() * (interestRate / 100.0); // Calculate interest
            } else if (account instanceof CreditAccount) {
                CreditAccount creditAccount = (CreditAccount) account; // Cast to CreditAccount
                double interestRate = creditAccount.getInterestRate(); // Get interest rate
                interestAmount = creditAccount.getBalance() * (interestRate / 100.0); // Calculate interest
            }

            // Format the account balance and interest amount
            String balanceStr = currencyFormat.format(account.getBalance()); // Format balance
            String interestStr = currencyFormat.format(interestAmount); // Format interest

            // Add account details to the result list
            result.add(String.format("%d %s %s %s",
                    account.getAccountNumber(), // Add account number
                    balanceStr, // Add formatted balance
                    account.getAccountType(), // Add account type
                    interestStr)); // Add formatted interest amount

            // Remove the account from the customer
            customerToDelete.removeAccount(account); // Update the customer's accounts
        }
        // Finally, remove the customer from the list of customers
        customers.remove(customerToDelete); // Remove customer from bank's customer list

        return result; // Return the result list containing customer and account info
    }




}
