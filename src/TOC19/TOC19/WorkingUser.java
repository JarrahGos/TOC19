package TOC19;


import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	private static Person user;
	
	public WorkingUser() throws FileNotFoundException {
		productDatabase = new ProductDatabase();
		personDatabase = new PersonDatabase();
		checkOuts = new CheckOut();

		user = null;
	}
	public final void getPMKeyS(String input)
    {
		if(( input != null && !input.equals("")) && (!input.matches("[0-9]+"))) {
			input = input.substring(1);
		}
		if(input == null || input.equals("") || !isLong(input) || (input.length() != 7 && input.length() != 5 && input.length() != 6) || !personDatabase.personExists(Long.parseLong(input))) { // checks for valid numbers in the PMKeyS
			user =  null;
		}
		else {
			user =  personDatabase.readDatabasePerson(Long.parseLong(input));
		}
    }
	public final String[] getUserNames() {
		return personDatabase.getUserNames();
	}
	public final String[] getProductNames() {
		return productDatabase.getProductNames();
	}
	public final String getSecurePassword(String passwordToHash)
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
	public final boolean passwordsEqual(String PW) 
	{
		String testing = getSecurePassword(PW);
		return (testing.equals(personDatabase.getPersonName(-2)));
	}
	public final void setAdminPassword(String PW)
	{
		personDatabase.setAdminPassword(PW);
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
	public ScrollPane printDatabase(String type) throws IOException {
		TextArea textArea;
		switch(type) {
			case("Product"):textArea = new TextArea(productDatabase.getDatabase(3));
							break;
			case("Person"):textArea = new TextArea(personDatabase.getDatabase());
							break;
			default:textArea = new TextArea(personDatabase.getDatabase());
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
            user = null;
            checkOuts = new CheckOut();
        }
	public final void buyProducts()
	{
            user.addPrice(checkOuts.getPrice());
//		personDatabase.addCost(user, checkOuts.getPrice());// add the bill to the persons account
		Product[] purchased = checkOuts.productBought(); // clear the quantities and checkout
		productDatabase.writeOutDatabase(purchased);
		personDatabase.writeOutDatabasePerson(user);
		checkOuts = new CheckOut(); // ensure checkout clear
        user = null;
	}
	public final String getCheckOut()
	{
		return checkOuts.getCheckOut(1);
	}
	public final String[] getCheckOutNames()
	{
		return checkOuts.getCheckOutNames();
	}
	public final String[] getCheckOutPrices()
	{
		return checkOuts.getCheckOutPrices();
	}
	public final String userName()
	{
		return user == null ? "error" : user.getName();
	}
	public final boolean addToCart(String input) 
	{
		long tempBarCode = -1; 
		if(input != null && !input.equals("") && isLong(input)) {
			tempBarCode = Long.parseLong(input); // disallows the user from entering nothing or clicking cancel. 
		}
		else if((input == null ) || ("".equals(input) )) {
			return false; 
		}
		Product adding = productDatabase.getProductRef(tempBarCode);
		if(adding != null) {
			checkOuts.addProduct(adding); //otherwise, add the product as normal.
			return true;
		}
		return false;
	}
	public final double getPrice()
	{
		long price = checkOuts.getPrice();
		return ((double)price)/100;
	}
	public final void addPersonToDatabase(String name, long PMKeyS)
	{
		personDatabase.setDatabasePerson(name, 0,0, PMKeyS, true);
	}
	public final void addProductToDatabase(String name, long barCode, long price)
	{
		productDatabase.setDatabaseProduct(name, price, barCode);
	}
	public final void adminWriteOutDatabase(String type) throws IOException, InterruptedException {
		switch(type) {
			case("Person"):personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
							break;
			case("Product"):productDatabase.adminWriteOutDatabase("adminProductDatabase.csv");
							break;
			default:personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
		}
	}
	public final void removePerson(int index) throws IOException, InterruptedException {
		personDatabase.delPerson(index);
	}
	public final void removeProduct(int index) throws IOException, InterruptedException {
		productDatabase.delProduct(index);
	}		
	public final long getProductBarCode(int index)
	{
		return productDatabase.getBarCode(index);
	}
	public final double getProductPrice(int index)
	{
		return productDatabase.getProductPrice(index);
	}
	public final int getProductNumber(int index)
	{
		return productDatabase.getNumber(index);
	}
	public final void setNumberOfProducts(int index, int numberOfProducts)
	{
		productDatabase.setNumber(index, numberOfProducts);
	}
	public final boolean userCanBuy()
	{
		return user.canBuy(); //not sure whether this will do the requested job. 
	}
	public final boolean userCanBuyAdmin(String name)
	{
		Person usr = personDatabase.readDatabasePerson(name);
		return usr.canBuy();
	}
		public final void setUserCanBuy(String userName, boolean canBuy)
	{
		personDatabase.setPersonCanBuy(userName, canBuy);
	}
	public final boolean userLoggedIn()
	{
		return user != null;
	}
}
