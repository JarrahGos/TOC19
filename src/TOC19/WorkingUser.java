//package TOC19;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jarrah
 */
public class WorkingUser {
	
	private ProductDatabase productDatabase;
	private PersonDatabase personDatabase;
	private CheckOut checkOuts;
	private static int userNumber;
	
	public WorkingUser() {
		productDatabase = new ProductDatabase();
		personDatabase = new PersonDatabase();
		checkOuts = new CheckOut();

		userNumber = -1;
	}
	public final void addDatabases()
	{
		int error = productDatabase.readDatabase("productDatabase.txt"); // read in the product database
		if(error == -1) { // tell the user there was an error reading the above
			System.out.println("There was an error reading the productDatabase");
		}
		else { // tell the user the above went swimingly. 
			System.out.printf("I have imported %d products\n", error);
		}
		error = personDatabase.readDatabase("personDatabase.txt"); // as above for the person database
		if(error == -1) {
			System.out.println("There was an error reading the personDatabase");
		}
		else {
			System.out.printf("I have imported %d people\n", error);
		}
	}
	public final void getPMKeyS(String input) 
    {
        boolean correct = false;
		if(( input != null && !input.equals("")) && (input.charAt(0) == 'c' || input.charAt(0) == 'n' || input.charAt(0) == 'C' || input.charAt(0) == 'N')) {
			input = input.substring(1);
		}
		if(input == null || input.equals("") || !isLong(input) || (input.length() != 7 && input.length() != 5 && input.length() != 6) || !personDatabase.personExists(Long.parseLong(input))) { // checks for valid numbers in the PMKeyS
			userNumber =  -1;
		}
		else {
			correct = true;
			userNumber =  personDatabase.findPerson(Long.parseLong(input));
		}
    }
	public final String[] getUserNames() {
		return personDatabase.getUserNames();
	}
	public final String[] getProductNames() {
		return productDatabase.getProductNames();
	}
	public static String getSecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
	public final boolean isInteger(String s) 
	{
		if(s == null) return false;
		try { 
			Integer.parseInt(s); // try to parse the string, catching a failure
		} 
		catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}
	public final boolean isDouble(String s) 
	{
		if(s == null) return false;
		try { 
			Double.parseDouble(s);  // try to parse the string, catching a failure
		} 
		catch(NumberFormatException e) { 
		 return false; 
		}
		// only got here if we didn't return false
		return true;
	}
	public final boolean isLong(String s)
	{
		if(s == null) return false;
		try {
			Long.parseLong(s); // try to parse the string, catching a failure. 
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	public ScrollPane printDatabase(String type)
	{
		TextArea textArea;
		switch(type) {
			case("Product"):textArea = new TextArea(productDatabase.getDatabase(3));
							break;
			case("Person"):textArea = new TextArea(personDatabase.getDatabase(1));
							break;
			default:textArea = new TextArea(personDatabase.getDatabase(1));
							break;
		}
		textArea.setEditable(false); // stop the user being able to edit this and thinking it will save. 
		ScrollPane scrollPane = new ScrollPane(textArea);
		textArea.setWrapText(true);
		scrollPane.setHvalue(600);
		scrollPane.setVvalue(800);
		return scrollPane;
		
	}
        public final void logOut()
        {
            userNumber = -1;
            checkOuts = new CheckOut();
        }
	public final void buyProducts()
	{
		personDatabase.addCost(userNumber, checkOuts.getPrice());// add the bill to the persons account
		checkOuts.productBought(); // clear the quantities and checkout
		productDatabase.writeOutDatabase("productDatabase.txt"); // write out the databases. 
		personDatabase.writeOutDatabase("personDatabase.txt");
		checkOuts = new CheckOut(); // ensure checkout clear
        userNumber = -1;
	}
	public final String getCheckOut()
	{
		return checkOuts.getCheckOut(1);
	}
	public final String getCheckOutNames()
	{
		return checkOuts.getCheckOutNames();
	}
	public final String getCheckOutPrices()
	{
		return checkOuts.getCheckOutPrices();
	}
	public final String userName()
	{
		return userNumber == -1 ? "error" : PersonDatabase.getPersonName(userNumber);
	}
	public final boolean addToCart(String input) 
	{
		long tempBarCode = -1; 
		String tempInput;
		if(input != null && !input.equals("") && isLong(input)) {
			tempBarCode = Long.parseLong(input); // disallows the user from entering nothing or clicking cancel. 
		}
		else if((input == null ) || ("".equals(input) )) {
			return false; 
		}
		int productNumber = productDatabase.findProduct(tempBarCode); // Now that we have done the error checking, convert the barcode to a position in the database
		int checkProduct; // create this for use below
		if(productNumber != -1) {
			tempInput = productDatabase.getProduct(productNumber);
			checkProduct = checkOuts.productEqualTo(tempBarCode); // check that the product was not entered into the database before. 
	//																					//If it was, just add the quantity to the one in the database
			if(checkProduct != -1) {
				checkOuts.addQuantity(checkProduct, 1);
			}
			else checkOuts.addProduct(productDatabase.getProductRef(productNumber), 1); //otherwise, add the product as normal. 
			return true;
		}
		return false;
	}
	public final int userNumber()
	{
		return userNumber;
	}
	public final double getPrice()
	{
		long price = checkOuts.getPrice();
		return ((double)price)/100;
	}
	public final void addPersonToDatabase(String name, long PMKeyS)
	{
		personDatabase.setDatabasePerson(personDatabase.emptyPerson(), name, 0,0, PMKeyS, true);
	}
	public final void addProductToDatabase(String name, long barCode, long price)
	{
		productDatabase.setDatabaseProduct(productDatabase.emptyProduct(), name, price, barCode);
	}
	public final void adminWriteOutDatabase(String type) {
		switch(type) {
			case("Person"):personDatabase.adminWriteOutDatabase("adminPersonDatabase.txt");
							break;
			case("Product"):productDatabase.adminWriteOutDatabase("adminProductDatabase.txt");
							break;
			default:personDatabase.adminWriteOutDatabase("adminPersonDatabase.txt");
		}
	}
					
				
}
