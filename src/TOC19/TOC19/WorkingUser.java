package TOC19;


import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*
*    TOC19 is a simple program to run TOC payments within a small group.
*    Copyright (C) 2014  Jarrah Gosbell
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 *
 * @author jarrah
 */
class WorkingUser {

    /** The database which stores all products used by the system. */
    private final ProductDatabase productDatabase;
    /** The database which stores all people who can use the system */
    private final PersonDatabase personDatabase;
    /** the checkout used to store what a person is purchasing at a given time */
    private CheckOut checkOuts;
    /** the database which stores all the completed transactions */
    private TransactionDatabase transactionDatabase;
     /** The currently logged in user */
    private static Person user;

    /**
     * Create the working user instance with both databases and a checkout.
     */
    public WorkingUser() {
        productDatabase = new ProductDatabase();
        personDatabase = new PersonDatabase();
        checkOuts = new CheckOut();
        transactionDatabase = new TransactionDatabase();
        user = null;
    }

    /**
     * Take the given PMKeyS and find the user which correlates with it.
     * @param input The PMKeyS that you wish to search for as a string
     * @return 0 if the user found, 1 if the user dose not exist, 2 if the user cannot buy.
     */
    public final int getPMKeyS(String input) {
        if ((input != null && !input.equals("")) && (!input.matches("[0-9]+"))) {
            input = input.substring(1);
        }
        if (input == null || input.equals("") || !isLong(input) || !personDatabase.personExists(Long.parseLong(input))) { // checks for valid numbers in the PMKeyS
            user = null;
            return 1;
        } else {
            user = personDatabase.readDatabasePerson(Long.parseLong(input));
        }
        if (!user.canBuy()|| input.equals("7000000")) {
            user = null;
            return 2;
        }
        return 0;
    }

    /**
     * Get a list of the names of all users in the database
     * @return A string array of the usernames
     */
    public final String[] getUserNames() {
        return personDatabase.getUserNames();
    }

    /**
     * Get a user for a given Name
     * @param name name of user to get
     * @return Person matching name or null if none exists
     */
    public final Person getUser(String name){
        return personDatabase.readDatabasePerson(name);
    }

    /**
     * Get a list of the names of all products in the database
     * @return A string array of the product names
     */
    public final String[] getProductNames() {
        return productDatabase.getProductNames();
    }

    /**
     * Take a cleartext password and hash it ready for either checking or storage
     * @param passwordToHash The clear text password
     * @return The hash of the given password.
     */
    public final String getSecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.print(e);
        }
        return generatedPassword;
    }

    /**
     * Test whether a cleartext password is equal to the stored admin password
     * @param PW A cleartext password to test
     * @return The boolean test for whether the passwords are equal.
     */
    public final boolean passwordsEqual(String PW) {
        String testing = getSecurePassword(PW);
        return (testing.equals(personDatabase.getPersonName(-2)));
    }

    /**
     * Takes the given (prehashed) password and set it as the admin password
     * @param PW The prehashed password to be set
     */
    public final void setAdminPassword(String PW) {
        personDatabase.setAdminPassword(PW);
    }

    public final boolean isInteger(String s) {
        if (s == null) return false;
        try {
            Integer.parseInt(s); // try to parse the string, catching a failure
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public final boolean isDouble(String s) {
        if (s == null) return false;
        try {
            Double.parseDouble(s);  // try to parse the string, catching a failure
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    final boolean isLong(String s) {
        if (s == null) return false;
        try {
            Long.parseLong(s); // try to parse the string, catching a failure.
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Create a scroll pane of one of the databases
     * @param type A string of either "Product" or "Person" used to determine which database to print.
     * @return A scroll pane showing the database chosen in the parameter or the person database by default.
     * @throws IOException
     */
    public ScrollPane printDatabase(String type) throws IOException {
        TextArea textArea;
        switch (type) {
            case ("Product"):
                textArea = new TextArea(productDatabase.getDatabase());
                break;
            case ("Person"):
                textArea = new TextArea(personDatabase.getDatabase());
                break;
            default:
                textArea = new TextArea(personDatabase.getDatabase());
                break;
        }
        textArea.setEditable(false); // stop the user being able to edit this and thinking it will save.
        ScrollPane scrollPane = new ScrollPane(textArea);
        textArea.setWrapText(true);
        scrollPane.setHvalue(600);
        scrollPane.setVvalue(800);
        return scrollPane;

    }

    /**
     * Log the user out from this class
     */
    public final void logOut() {
        user = null;
        checkOuts = new CheckOut();
    }

    /**
     * Have the connected user buy the products in the checkout, adding the total cost to the users bill,
     * taking the number bought from the products in the database and clearing both the user and the checkout
     */
    public final void buyProducts() {
        user.addPrice(checkOuts.getPrice());
        Product[] purchased = checkOuts.productBought(); // clear the quantities and checkout
        productDatabase.writeOutDatabase(purchased);
        personDatabase.writeOutDatabasePerson(user);
        transactionDatabase.addTransactionToDatabase(new Transaction(user,checkOuts.getRawProducts(),checkOuts.getRawQuantities(), LocalDateTime.now()));
        checkOuts = new CheckOut(); // ensure checkout clear
        user = null;
        

    }

    /**
     * Get the names of all products in the checkout
     * @return A string array of the names of all products in the checkout
     */
    public final String[] getCheckOutNames() {
        return checkOuts.getCheckOutNames();
    }

    /**
     * Get the prices of all products in teh checkout
     * @return A string array of the prices of all products in the checkout.
     */
    public final String[] getCheckOutPrices() {
        return checkOuts.getCheckOutPrices();
    }

    /**
     * Takes the error value given by getPMKeyS and uses it to give the username or an error message
     * @param userError 0 if the user was correctly found, 1 if the user was not found and 2 if the user has been locked out.
     * @return The appropriate error message or the users name
     */
    public final String userName(int userError) {
        switch (userError) {
            case 0:
                if(user != null) return user.getName();
            case 1:
                return "User not found";
            case 2:
                return "You have been locked out.\n See the TOC treasurer";
        }
        return user == null ? "Error" : user.getName();
    }

    /**
     * Takes the barcode for a product and adds it to the checkout
     * @param input The barcode for the product as a string
     * @return True if the product was added, false if it failed
     */
    public final boolean addToCart(String input) {
        long tempBarCode = -1;
        if (input != null && !input.equals("") && isLong(input)) {
            tempBarCode = Long.parseLong(input); // disallows the user from entering nothing or clicking cancel.
        } else if ((input == null) || ("".equals(input))) {
            return false;
        }
        Product adding = productDatabase.getProductRef(tempBarCode);
        if (adding != null) {
            checkOuts.addProduct(adding); //otherwise, add the product as normal.
            return true;
        }
        else if(user.getBarCode() == tempBarCode) {
            adding = new Product("Buying yourself are you? You can't do that.", 0, tempBarCode);
            checkOuts.addProduct(adding);
            return true;
        }
        return false;
    }

    /**
     * Gets the price of the whole checkout
     * @return The price of the whole checkout as a double
     */
    public final double getPrice() {
        long price = checkOuts.getPrice();
        return ((double) price) / 100;
    }

    /**
     * Add a person to the database, given their name and PMKeyS
     * @param name The name of the person you wish to add
     * @param PMKeyS The PMKeyS of the person you wish to add
     */
    public final void addPersonToDatabase(String name, long PMKeyS) {
        personDatabase.setDatabasePerson(name, 0, 0, PMKeyS, true);
    }

    /**
     * Add a product to the database given their name, barcode and price
     * @param name The name of the product you wish to add
     * @param barCode The barcode for the product you wish to add.
     * @param price The price of the product you wish to add.
     */
    public final void addProductToDatabase(String name, long barCode, long price) {
        productDatabase.setDatabaseProduct(name, price, barCode);
    }

    /**
     * Alter a product in the database
     * @param name The new name of the product
     * @param oldName The old name of the product
     * @param price The new price of the product
     * @param barcode The new barcode of the product
     * @param oldBarcode The old barcode of the product
     */
    public final void changeDatabaseProduct(String name, String oldName, long price, long barcode, long oldBarcode) {
        productDatabase.changeDatabaseProduct(name, oldName, price, barcode, oldBarcode);
    }


    /**
     * Alter a product in the database
     * @param selectedIndex
     * @param name The new name of the person
     * @param pmkeys The new PMKeyS of the person
     * @param oldPmkeys The old PMKeyS of the person
     */
    public final void changeDatabasePerson(String selectedIndex, String name, long pmkeys, long oldPmkeys) {
        personDatabase.changeDatabasePerson(selectedIndex, name, pmkeys, oldPmkeys);
    }

    /**
     * Write out the CSV version of the database for the admin.
     * @param type "Person" for the person database or "Produt" for the product database
     * @throws IOException
     */
    public final void adminWriteOutDatabase(String type) throws IOException {
        switch (type) {
            case ("Person"):
                personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
                break;
            case ("Product"):
                productDatabase.adminWriteOutDatabase("adminProductDatabase.csv");
                break;
            default:
                personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
        }
    }

    /**
     * Delete the specified person
     * @param index The PMKeyS or name of the person as a string
     * @throws IOException
     * @throws InterruptedException
     */
    public final void removePerson(String index) throws IOException, InterruptedException {
        personDatabase.delPerson(index);
    }

    /**
     * Remove the specified product
     * @param index The barcode or name of the product to be removed
     * @throws IOException
     * @throws InterruptedException
     */
    public final void removeProduct(String index) throws IOException, InterruptedException {
        productDatabase.delProduct(index);
    }

    /**
     * Delete a product from the checkout given it's index in the checkout array
     * @param index The index of the item to delete in the checkout array
     */
    public final void deleteProduct(int index)
    {
        checkOuts.delProduct(index);
    }

    public final long getProductBarCode(int index) {
        return productDatabase.getBarCode(index);
    }

    /**
     * Get the barcode of a product given it's name
     * @param name The name of the product to get the barcode of
     * @return The barcode of the product with the name specified.
     */
    public final long getProductBarCode(String name) {
        Product getting = productDatabase.readDatabaseProduct(name);
        return getting.getBarCode();
    }

    /**
     * Get the name of a product given it's barcode
     * @param index The barcode of the product as a string
     * @return The name of the product with the given barcode
     */
    public final String getProductName(String index) {
        Product getting = productDatabase.readDatabaseProduct(index);
        return getting.getName();
    }

    /**
     * Get the price of a product given it's name or barcode
     * @param index the name or barcode of the desired product
     * @return The price of the specified product
     */
    public final double getProductPrice(String index) {
        return productDatabase.getProductPrice(index);
    }

    /**
     * Get the number of a product left in stock
     * @param name The name of the product you wish to check stock count.
     * @return The number of the specified product in stock
     */
    public final int getProductNumber(String name) {
        return productDatabase.getNumber(name);
    }

    /**
     * set the number of a product in stock
     * @param name The name of the product you wish to set the stock count for
     * @param numberOfProducts The new stock count.
     */
    public final void setNumberOfProducts(String name, int numberOfProducts) {
        productDatabase.setNumber(name, numberOfProducts);
    }

    public final boolean userCanBuy() {
        return user.canBuy(); //not sure whether this will do the requested job.
    }

    /**
     * Determine whether the given user can buy from the program
     * @param name The name of the product you wish to check for.
     * @return The boolean of whether the user can buy or not.
     */
    public final boolean userCanBuyAdmin(String name) {
        Person usr = personDatabase.readDatabasePerson(name);
        return usr.canBuy();
    }

    /**
     * Set Whether the user can buy from the program
     * @param userName The name of the user to change
     * @param canBuy Whether you wish the user to be able to buy or not.
     */
    public final void setUserCanBuy(String userName, boolean canBuy) {
        personDatabase.setPersonCanBuy(userName, canBuy);
    }

    /**
     * Determine whether there is a user logged in
     * @return The boolean value of whether the user is logged in.
     */
    public final boolean userLoggedIn() {
        return user != null;
    }

    /**
     * Reset the bills of all users to zero for this billing period.
     */
    public final void resetBills()
    {
        personDatabase.resetBills();
    }

    /**
     * Reset the Running totals of all users to zero.
     */
    public final void resetRunningCosts(){
        personDatabase.resetRunningTotals();
    }

    public static long getLogedInBarcode()
    {
        return user.getBarCode();
    }

    /** Retrieves all transactions for a specified user */
    public ArrayList<Transaction> readPersonsTransactions(String PMKeyS){
        return transactionDatabase.readTransactionDatabase(PMKeyS);
    }

    /** Retrieves all recorded transactions */
    public ArrayList<Transaction> readTransactions(){
        return transactionDatabase.readTransactions();
    }

    public final void resetTransactionDatabase(){
        transactionDatabase.resetTransactionDatabase();
    }

    /**
     * Get the current users current bill.
     * @return The current bill of the current user as a double.
     */
    public final double getUserBill() {
        return user.totalCostWeek();
    }
}
