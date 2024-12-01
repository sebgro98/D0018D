package sebron4;

/**
 * The class Customer will create the customer object will have getters and setters and also
 * add accounts to the customer .
 * @author Sebastian Rone, sebron-4
 */

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name; // First name of the customer
    private String sirName; // Last name (surname) of the customer
    private final String socialSecurityNumber; // Unique social security number of the customer
    private List<Account> accounts = new ArrayList<>(); // List of accounts associated with the customer

    /**
     * Constructs a new Customer with the given details.
     * @param name The first name of the customer
     * @param sirName The last name of the customer
     * @param socialSecurityNumber The unique social security number of the customer
     */
    public Customer(String name, String sirName, String socialSecurityNumber) {
        this.name = name;
        this.sirName = sirName;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * Gets the last name (surname) of the customer.
     * @return The last name of the customer
     */
    public String getSirName() {
        return sirName;
    }

    /**
     * Sets the last name (surname) of the customer.
     * @param sirName The new last name to set
     */
    public void setSirName(String sirName) {
        this.sirName = sirName;
    }

    /**
     * Gets the first name of the customer.
     * @return The first name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name of the customer.
     * @param name The new first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique social security number of the customer.
     * @return The social security number of the customer
     */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
     * Gets the list of accounts associated with the customer.
     * @return The list of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of accounts associated with the customer.
     * @param accounts The new list of accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Adds an account to the customer's list of accounts.
     * @param account The account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", socialSecurityNumber, name, sirName);
    }
}
