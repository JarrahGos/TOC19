package TOC19;


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
	private int logicalSize;
	
	public WorkingUser() {
		productDatabase = new ProductDatabase();
		personDatabase = new PersonDatabase();
		checkOuts = new CheckOut();
		logicalSize = 0;
	}
	private long getPMKeyS(String input) // take this recursion and make it iteration. 
    {
        boolean correct = false;
		if(input == null) { // First check that the PMKeyS was properly entered. This is for the cancel button
			return -1; // 2001 esq error message for a bad PMKeyS
		}
		else if(!input.equals("") && (input.charAt(0) == 'c' || input.charAt(0) == 'n' || input.charAt(0) == 'C' || input.charAt(0) == 'N')) {
			input = input.substring(1);
		}
		if(input.equals("") || !isLong(input) || (input.length() != 7 && input.length() != 5 && input.length() != 6) || !personDatabase.personExists(Integer.parseInt(input))) { // checks for valid numbers in the PMKeyS
			return -1;
		}
		correct = true;
        return Long.parseLong(input);
    }
	private static String getSecurePassword(String passwordToHash)
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
	private final boolean isInteger(String s) 
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
	private final boolean isDouble(String s) 
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
	private final boolean isLong(String s)
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
		private ScrollPane printDatabase(String type)
	{
		TextArea textArea;
		switch(type) {
			case("Product"):textArea = new TextArea(productDatabase.getDatabase(1));
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
        private void buyProducts(int personNumber, long price)
        {
            personDatabase.addCost(personNumber, price);// add the bill to the persons account
            checkOuts.productBought(); // clear the quantities and checkout
            productDatabase.writeOutDatabase("productDatabase.txt"); // write out the databases. 
            personDatabase.writeOutDatabase("personDatabase.txt");
            checkOuts = new CheckOut(); // ensure checkout clear
        }
}
