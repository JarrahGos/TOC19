//package TOC19;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import java.io.FileNotFoundException;
import TOC19.Settings;

/***
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

/*
 * Author: Jarrah Gosbell
 * Student Number: z5012558
 * Class: PersonDatabase
 * Description: This program will allow for the input and retreval of the person database and will set the limits of the database.
 */
public class WorkingUser {
	
	private ProductDatabase productDatabase;
	private PersonDatabase personDatabase;
	private CheckOut checkOuts;
	private	static long pmKeyS;
	private Settings config = new Settings();
	
	public WorkingUser() throws FileNotFoundException
	{
		productDatabase = new ProductDatabase();
		personDatabase = new PersonDatabase();
		checkOuts = new CheckOut();

		pmKeyS = -1;
	}
//	public final void addDatabases()
//	{
//		int error = productDatabase.readDatabase("productDatabase.txt"); // read in the product database
//		if(error == -1) { // tell the user there was an error reading the above
//			System.out.println("There was an error reading the productDatabase");
//		}
//		else { // tell the user the above went swimingly. 
//			System.out.printf("I have imported %d products\n", error);
//		}
//		error = personDatabase.readDatabase("personDatabase.txt"); // as above for the person database
//		if(error == -1) {
//			System.out.println("There was an error reading the personDatabase");
//		}
//		else {
//			System.out.printf("I have imported %d people\n", error);
//		}
//	}
	public final void getPMKeyS(String input) 
    {
        boolean correct = false;
		if(( input != null && !input.equals("")) && (input.charAt(0) == 'c' || input.charAt(0) == 'n' || input.charAt(0) == 'C' || input.charAt(0) == 'N')) {
			input = input.substring(1);
		}
		if(input == null || input.equals("") || !isLong(input) || (input.length() != 7 && input.length() != 5 && input.length() != 6) || !personDatabase.personExists(Long.parseLong(input))) { // checks for valid numbers in the PMKeyS
			pmKeyS =  -1;
		}
		else {
			correct = true;
			pmKeyS =  personDatabase.findPerson(Long.parseLong(input));
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
            pmKeyS = -1;
            checkOuts = new CheckOut();
        }
	public final void buyProducts()
	{
		personDatabase.addCost(pmKeyS, checkOuts.getPrice());// add the bill to the persons account
//		checkOuts.productBought(); // This no longer works as checkouts does not have access to the database. 
		productDatabase.productBought(checkOuts.productBought());
//		productDatabase.writeOutDatabase("productDatabase.txt"); // write out the databases. 
//		personDatabase.writeOutDatabase("personDatabase.txt");
		checkOuts = new CheckOut(); // ensure checkout clear
        pmKeyS = -1;
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
		if(pmKeyS == -1) return "error";
		else return personDatabase.getPersonName(pmKeyS);
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
//		int productNumber = productDatabase.binarySearch(tempBarCode); // Now that we have done the error checking, convert the barcode to a position in the database
		int checkProduct; // create this for use below
//		if(productNumber != -1) {
//			tempInput = productDatabase.getProduct(productNumber);
			checkProduct = checkOuts.productEqualTo(tempBarCode); // check that the product was not entered into the database before. 
	//																					//If it was, just add the quantity to the one in the database
			if(checkProduct != -1) {
				checkOuts.addQuantity(checkProduct, 1);
			}
			else {
				checkOuts.addProduct(productDatabase.getProductName(Long.toString(tempBarCode)), (long)productDatabase.getProductPrice(Long.toString(tempBarCode))*100, tempBarCode);  
				return true;
			}
			return false;
	}
//	public final int pmKeyS()
//	{
//		return pmKeyS;
//	}
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
		productDatabase.setDatabaseProduct(barCode, name, price);
	}
	public final void adminWriteOutDatabase(String type) {
		switch(type) {
			case("Person"):personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
							break;
			case("Product"):productDatabase.adminWriteOutDatabase("adminProductDatabase.csv");
							break;
			default:personDatabase.adminWriteOutDatabase("adminPersonDatabase.csv");
		}
	}
	public final void removePerson(long barCode)
	{
		personDatabase.delPerson(barCode);
	}
	public final void removePerson(String name)
	{
		personDatabase.delPerson(name);
	}
	public final void removeProduct(long barCode)
	{
		productDatabase.delProduct(barCode);
	}		
	public final void removeProduct(String name)
	{
		productDatabase.delProduct(name);
	}
	public final double getProductPrice(long barCode)
	{
		return productDatabase.getProductPrice(Long.toString(barCode));
	}
	public final int getProductNumber(long barCode)
	{
		return productDatabase.getNumber(barCode);
	}
	public final int getProductNumber(String name)
	{
		return productDatabase.getNumber(name);
	}
	public final long getProductBarCode(String name)
	{
		return Long.parseLong(productDatabase.getProductBarCode(name));
	}
	public final long getPersonBarCode(String name)
	{
		return Long.parseLong(personDatabase.getPersonBarCode(name));
	}
	public final void setNumberOfProducts(long barCode, int numberOfProducts)
	{
		productDatabase.setNumber(barCode, numberOfProducts);
	}
	public final void setNumberOfProducts(String name, int numberOfProducts)
	{
		productDatabase.setNumber(name, numberOfProducts);
	}
	public final boolean userCanBuy(long barCode)
	{
		return personDatabase.personCanBuy(barCode);
	}
	public final boolean userCanBuy(String name)
	{
		return personDatabase.personCanBuy(name);
	}
	public final void setUserCanBuy(long barCode, boolean canBuy)
	{
		personDatabase.setPersonCanBuy(barCode, canBuy);
	}
	public final void setUserCanBuy(String name, boolean canBuy)
	{
		personDatabase.setPersonCanBuy(name, canBuy);
	}
	public final long userNumber()
	{
		return pmKeyS;
	}
}
