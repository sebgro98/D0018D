package sebron4;
/**
 * In the BankLogic class we do most of the "logic" here we fetch customers, can change their name, get accounts
 * calculate the end balance when closing the account etc. Everything regarding updating states or information is done here.
 * @author Sebastian Rone, sebron-4
 */

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BankLogic {

    private List<Customer> customers = new ArrayList<>(); // List of all customers in the bank

    /**
     * Retrieves a list of all customers formatted as strings.
     * @return A list of formatted strings representing all customers
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
     * @param name The first name of the customer
     * @param surname The last name of the customer
     * @param pNo The social security number of the customer
     * @return true if the customer was successfully created, false if a customer with the same social security number already exists
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
     * @param pNo The social security number of the customer
     * @return A list of formatted strings representing the customer and their accounts, or null if the customer is not found
     */
    public List<String> getCustomer(String pNo) {
        List<String> strings = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                strings.add(Objects.toString(customer));
                for (Account account : customer.getAccounts()) {
                    strings.add(Objects.toString(account));
                }
                return strings;
            }
        }
        return null; // Customer not found
    }

    /**
     * Changes the name of a customer.
     * @param name The new first name of the customer (empty if not changing)
     * @param surname The new last name of the customer (empty if not changing)
     * @param pNo The social security number of the customer
     * @return true if the name was successfully changed, false if the customer was not found or both names are empty
     */
    public boolean changeCustomerName(String name, String surname, String pNo) {
        if (!name.isEmpty() || !surname.isEmpty()) {
            for (Customer customer : customers) {
                if (customer.getSocialSecurityNumber().equals(pNo)) {
                    if (name.isEmpty()) {
                        customer.setSirName(surname);
                    } else {
                        customer.setName(name);
                    }
                    return true;
                }
            }
        }
        return false; // Customer not found or both names are empty
    }

    /**
     * Creates a new savings account for a customer.
     * @param pNo The social security number of the customer
     * @return The account number of the newly created account, or -1 if the customer was not found
     */
    public int createSavingsAccount(String pNo) {
        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                Account account = new Account();
                customer.addAccount(account);
                return account.getAccountNumber();
            }
        }
        return -1; // Customer not found
    }

    /**
     * Retrieves the details of a specific account belonging to a customer.
     * @param pNo The social security number of the customer
     * @param accountId The ID of the account
     * @return The formatted account details, or null if the customer or account was not found
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
     * @param pNo The social security number of the customer
     * @param accountId The ID of the account
     * @param amount The amount to deposit (must be greater than 0)
     * @return true if the deposit was successful, false if the amount is invalid or the customer or account was not found
     */
    public boolean deposit(String pNo, int accountId, int amount) {
        if (amount <= 0) {
            return false; // Invalid deposit amount
        }

        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        account.setBalance(account.getBalance() + amount);
                        return true;
                    }
                }
            }
        }
        return false; // Customer or account not found
    }

    /**
     * Withdraws a specified amount from a customer's account.
     * @param pNo The social security number of the customer
     * @param accountId The ID of the account
     * @param amount The amount to withdraw (must be greater than 0)
     * @return true if the withdrawal was successful, false if the amount is invalid, insufficient balance, or the customer or account was not found
     */
    public boolean withdraw(String pNo, int accountId, int amount) {
        if (amount <= 0) {
            return false; // Invalid withdrawal amount
        }

        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber() == accountId) {
                        if (account.getBalance() >= amount) {
                            account.setBalance(account.getBalance() - amount);
                            return true;
                        }
                        return false; // Insufficient balance
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
                        double interestAmount = account.getBalance() * account.getInterestRate() / 100.0;
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
        return null; // Customer or account not found
    }

    /**
     * Deletes a customer and their accounts from the bank.
     * @param pNo The social security number of the customer
     * @return A list of strings where the first element is the customer info and the remaining elements are the details of the deleted accounts, or null if the customer was not found
     */
    public List<String> deleteCustomer(String pNo) {
        List<String> result = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getSocialSecurityNumber().equals(pNo)) {
                // Add customer info to the result list
                result.add(String.format("%s %s %s",
                        customer.getSocialSecurityNumber(),
                        customer.getName(),
                        customer.getSirName()));

                // Remove accounts and add their details to the result list
                List<Account> accounts = new ArrayList<>(customer.getAccounts());
                for (Account account : accounts) {
                    double interestAmount = account.getBalance() * account.getInterestRate() / 100.0;
                    customer.getAccounts().remove(account);

                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
                    String balanceStr = currencyFormat.format(account.getBalance());
                    String interestStr = currencyFormat.format(interestAmount);

                    result.add(String.format("%d %s %s %s",
                            account.getAccountNumber(),
                            balanceStr,
                            account.getAccountType(),
                            interestStr));
                }

                customers.remove(customer);

                return result;
            }
        }
        return null; // Customer not found
    }
}
