package sebron4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * A JavaFX-based Bank Application GUI.
 * Provides functionality to add and manage customers and accounts.
 * @author Sebastian Rone, sebron-4
 */

public class BankApp extends Application {

    private BankLogic bankLogic; // Instance of the BankLogic class to handle business logic
    private static final String FILE_DIRECTORY = "sebron4_file"; // Directory to store serialized data
        @Override
        public void start(Stage primaryStage) {

            File directory = new File(FILE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }

            bankLogic = new BankLogic(); // Initialize the BankLogic instance

            // Set up the primary stage
            primaryStage.setTitle("Bank Application");

            BorderPane root = new BorderPane(); // Root layout for the application

            // Create menu bar and menus
            MenuBar menuBar = new MenuBar();
            Menu customerMenu = new Menu("Kund");
            Menu accountMenu = new Menu("Konto");

            // Define menu items
            MenuItem addCustomer = new MenuItem("Lägg till kund");
            MenuItem deleteCustomer = new MenuItem("Tabort Kund");
            MenuItem addAccount = new MenuItem("Lägg till konto");
            MenuItem closeAccount = new MenuItem("Stäng konto");
            MenuItem withdraw = new MenuItem("Uttag");
            MenuItem deposit = new MenuItem("Insättning");
            MenuItem getTransactions = new MenuItem("Transaktioner");
            MenuItem getAccount = new MenuItem("Hämta Konto");
            MenuItem changeCustomerName = new MenuItem("Ändra Kundnamn");
            MenuItem getCustomer = new MenuItem("Hämta Kund");
            MenuItem getAllCustomers = new MenuItem("Hämta Alla Kunder");

            // Add menu items to their respective menus
            customerMenu.getItems().addAll(addCustomer, deleteCustomer, changeCustomerName, getCustomer, getAllCustomers);
            accountMenu.getItems().addAll(addAccount, closeAccount, withdraw, deposit, getTransactions, getAccount);

            // Add menus to the menu bar
            menuBar.getMenus().addAll(customerMenu, accountMenu);
            root.setTop(menuBar);

            // Create the output area to display information
            TextArea outputArea = new TextArea();
            outputArea.setEditable(false);
            root.setCenter(outputArea);

            /**
             * Handles adding a new customer.
             * Prompts the user for name, surname, and social security number.
             * Updates the output area with success or failure message.
             * @param e ActionEvent triggered when "Lägg till kund" is selected.
             */
            addCustomer.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Namn", "Efternamn", "Personnummer"});
                dialog.showAndWait().ifPresent(result -> {
                    boolean success = bankLogic.createCustomer(result[0], result[1], result[2]);
                    outputArea.appendText(success ? "Kunden lades till korrekt. Personnummer: " + result[2] + "\n" : "Gick inte att lägga till kund. Kunden kanske redan existerar.\n");
                });
            });

            /**
             * Handles deleting a customer.
             * Prompts the user for social security number and displays the deleted customer information.
             * @param e ActionEvent triggered when "Tabort Kund" is selected.
             */
            deleteCustomer.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Tabort Kund");
                dialog.setHeaderText(null);
                dialog.setContentText("Skriv in personnummer:");

                dialog.showAndWait().ifPresent(ssn -> {
                    java.util.List<String> deletedInfo = bankLogic.deleteCustomer(ssn);
                    if (deletedInfo != null) {
                        outputArea.appendText("kunden togs bort:\n");
                        deletedInfo.forEach(info -> outputArea.appendText(info + "\n"));
                    } else {
                        outputArea.appendText("Det gick inte att ta bort kunden. Kunden hittades inte.\n");
                    }
                });
            });

            /**
             * Handles changing a customer's name.
             * Prompts the user for social security number, new name, and new surname.
             * Updates the output area with success or failure message.
             * @param e ActionEvent triggered when "Ändra Kundnamn" is selected.
             */
            changeCustomerName.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Nytt Namn", "Nytt Efternamn"});
                dialog.showAndWait().ifPresent(result -> {
                    boolean success = bankLogic.changeCustomerName(result[0], result[1], result[2]);
                    outputArea.appendText(success ? "Kundens namn ändrades till: " + result[1] + " " + result[2] + "\n" : "Kunde inte ändra kundens namn.\n");
                });
            });

            /**
             * Handles fetching a specific customer's information.
             * Prompts the user for social security number and displays customer details.
             * @param e ActionEvent triggered when "Hämta Kund" is selected.
             */
            getCustomer.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Hämta Kund");
                dialog.setHeaderText(null);
                dialog.setContentText("Skriv in personnummer:");

                dialog.showAndWait().ifPresent(ssn -> {
                    String customerInfo = String.valueOf(bankLogic.getCustomer(ssn));
                    outputArea.appendText(customerInfo != null ? customerInfo + "\n" : "Kunden hittades inte.\n");
                });
            });

            /**
             * Handles fetching all customers in the bank.
             * Displays a list of all customers in the output area.
             * @param e ActionEvent triggered when "Hämta Alla Kunder" is selected.
             */
            getAllCustomers.setOnAction(e -> {
                java.util.List<String> allCustomers = bankLogic.getAllCustomers();
                allCustomers.forEach(customer -> outputArea.appendText(customer + "\n"));
            });

            /**
             * Handles adding a new account for a customer.
             * Prompts the user for social security number and account type (Savings or Credit).
             * Displays success or failure message in the output area.
             * @param e ActionEvent triggered when "Lägg till konto" is selected.
             */
            addAccount.setOnAction(e -> {
                TextInputDialog ssnDialog = new TextInputDialog();
                ssnDialog.setTitle("Lägg till konto");
                ssnDialog.setHeaderText(null);
                ssnDialog.setContentText("Skriv in kundens personnummer:");

                ssnDialog.showAndWait().ifPresent(ssn -> {
                    ChoiceDialog<String> accountTypeDialog = new ChoiceDialog<>("Sparkonto", "Sparkonto", "Kreditkonto");
                    accountTypeDialog.setTitle("Konto typ");
                    accountTypeDialog.setHeaderText(null);
                    accountTypeDialog.setContentText("Välj Konto typ:");

                    accountTypeDialog.showAndWait().ifPresent(accountType -> {
                        int accountId = accountType.equals("Sparkonto")
                                ? bankLogic.createSavingsAccount(ssn)
                                : bankLogic.createCreditAccount(ssn);

                        outputArea.appendText(accountId != -1
                                ? "Kontot skapades. Konto ID: " + accountId + "\n"
                                : "Det gick inte att skapa konto. Kunden hittades ej.\n");
                    });
                });
            });

            /**
             * Handles closing a customer's account.
             * Prompts the user for social security number and account ID.
             * Displays the result of the closure, including any interest.
             * @param e ActionEvent triggered when "Stäng konto" is selected.
             */
            closeAccount.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        String resultStr = bankLogic.closeAccount(result[0], accountId);
                        outputArea.appendText(resultStr != null
                                ? "Kontot avslutades:\n" + resultStr + "\n"
                                : "Det gick inte att avsluta konto. Kunden hittades inte.\n");
                    } catch (NumberFormatException ex) {
                        outputArea.appendText("Ogiltigt konto ID format.\n");
                    }
                });
            });

            /**
             * Handles retrieving account details for a specific account.
             * Prompts the user for social security number and account ID.
             * Displays account information in the output area.
             * @param e ActionEvent triggered when "Hämta Konto" is selected.
             */
            getAccount.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        String accountInfo = bankLogic.getAccount(result[0], accountId);
                        outputArea.appendText(accountInfo != null ? accountInfo + "\n" : "Konto hittades inte.\n");
                    } catch (NumberFormatException ex) {
                        outputArea.appendText("Ogiltig inmatning för Konto ID.\n");
                    }
                });
            });

            /**
             * Handles withdrawals from a customer's account.
             * Prompts the user for social security number, account ID, and amount.
             * Displays the result of the transaction in the output area.
             * @param e ActionEvent triggered when "Uttag" is selected.
             */
            withdraw.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID", "Belopp"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        double amount = Double.parseDouble(result[2]);
                        boolean success = bankLogic.withdraw(result[0], accountId, amount);
                        outputArea.appendText(success ? "Uttag lyckades.\n" : "Uttag misslyckades.\n");
                    } catch (NumberFormatException ex) {
                        outputArea.appendText("Ogiltig inmatning.\n");
                    }
                });
            });

            /**
             * Handles deposits into a customer's account.
             * Prompts the user for social security number, account ID, and amount.
             * Displays the result of the transaction in the output area.
             * @param e ActionEvent triggered when "Insättning" is selected.
             */
            deposit.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID", "Belopp"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        int amount = Integer.parseInt(result[2]);
                        boolean success = bankLogic.deposit(result[0], accountId, amount);
                        outputArea.appendText(success ? "Insättning lyckades.\n" : "Insättning misslyckades.\n");
                    } catch (NumberFormatException ex) {
                        outputArea.appendText("Ogiltig inmatning.\n");
                    }
                });
            });

            /**
             * Handles retrieving transactions for a specific account.
             * Prompts the user for social security number and account ID.
             * Displays the list of transactions in the output area.
             * @param e ActionEvent triggered when "Transaktioner" is selected.
             */
            getTransactions.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        java.util.List<String> transactions = bankLogic.getTransactions(result[0], accountId);
                        transactions.forEach(transaction -> outputArea.appendText(transaction + "\n"));
                    } catch (NumberFormatException ex) {
                        outputArea.appendText("Ogiltig inmatning.\n");
                    }
                });
            });

            // File menu for saving and loading customers
            Menu fileMenu = new Menu("Fil");
            MenuItem saveToFile = new MenuItem("Spara kunder till fil");
            MenuItem loadFromFile = new MenuItem("Läs in kunder från fil");

            fileMenu.getItems().addAll(saveToFile, loadFromFile);

            // Print menu for generating account statements
            Menu printMenu = new Menu("Utskrift");
            MenuItem generateStatement = new MenuItem("Skapa kontoutdrag");

            printMenu.getItems().add(generateStatement);

            // Add menus to the menu bar
            menuBar.getMenus().addAll(fileMenu, printMenu);

            /**
             * Adds functionality to save customers to a file.
             * When selected, the program saves all customer data to a .dat file in the specified directory.
             */
            saveToFile.setOnAction(e -> {
                try {
                    bankLogic.saveCustomersToFile(FILE_DIRECTORY + "/bank.dat");
                    outputArea.appendText("Kunder sparade till fil.\n");
                } catch (IOException ex) {
                    outputArea.appendText("Fel vid sparande: " + ex.getMessage() + "\n");
                }
            });


            /**
             * Adds functionality to save customers to a file.
             * When selected, the program saves all customer data to a .dat file in the specified directory.
             */
            loadFromFile.setOnAction(e -> {
                try {
                    bankLogic.loadCustomersFromFile(FILE_DIRECTORY + "/bank.dat");
                    outputArea.appendText("Kunder lästa in från fil.\n");
                } catch (IOException | ClassNotFoundException ex) {
                    outputArea.appendText("Fel vid inläsning: " + ex.getMessage() + "\n");
                }
            });


            /**
             * Adds functionality to generate an account statement.
             * Prompts the user for a personal number and account ID, retrieves account details, and saves them to a .txt file.
             */
            generateStatement.setOnAction(e -> {
                Dialog<String[]> dialog = createInputDialog(new String[]{"Personnummer", "Konto ID"});
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int accountId = Integer.parseInt(result[1]);
                        String accountDetails = bankLogic.getAccount(result[0], accountId);
                        if (accountDetails == null) {
                            outputArea.appendText("Konto eller kund hittades inte.\n");
                            return;
                        }
                        String filePath = FILE_DIRECTORY + "/statement.txt";
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                            writer.write("Kontoutdrag - " + LocalDate.now() + "\n");
                            writer.write(accountDetails + "\n");
                            outputArea.appendText("Kontoutdrag sparat i: " + filePath + "\n");
                        }
                    } catch (IOException | NumberFormatException ex) {
                        outputArea.appendText("Fel vid generering av kontoutdrag: " + ex.getMessage() + "\n");
                    }
                });
            });


            Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a dialog for user input with multiple fields.
     * @param prompts An array of field prompts.
     * @return A dialog that collects user input.
     */

    private Dialog<String[]> createInputDialog(String[] prompts) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Input Krävs");

        VBox content = new VBox(10);
        TextField[] fields = new TextField[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            TextField field = new TextField();
            field.setPromptText(prompts[i]);
            fields[i] = field;
            content.getChildren().add(new VBox(new Label(prompts[i]), field));
        }

        dialog.getDialogPane().setContent(content);
        ButtonType submitButtonType = new ButtonType("Skicka in", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String[] results = new String[prompts.length];
                for (int i = 0; i < prompts.length; i++) {
                    results[i] = fields[i].getText();
                }
                return results;
            }
            return null;
        });

        return dialog;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
