package TOC19;

/* 
* Author: Jarrah Gosbell 
* Student Number: z5012558
* Class: Interface
* Description: This program will allow the user to interact with the program, creating, deleting and modifying products and checkOuts.
*/
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.*;

public class Interface
{
	// Create the necessary instance variables.
	private ProductDatabase productDatabase;
	private PersonDatabase personDatabase;
	private Scanner console;
	private CheckOut checkOuts;
	private int logicalSize;
		
	public Interface() 
	{
		//initalize the variables created above
		productDatabase = new ProductDatabase();
		personDatabase = new PersonDatabase();
		console = new Scanner(System.in);
		checkOuts = new CheckOut();
		logicalSize = 0;
	}	
	
	
	private void run() 
	{
		/**
		Class Interface: Method run
		Preconditions: Interface constructor has been run
		Postconditions: the program will have run and all user interactions will have been acted upon. If ended correctly, the program will write the productDatabase to productDatabase.txt. The program will end.
		*/
		
		// create the variables that will be used throughout the program
		String tempName; // names of things which we will be entering and reusing	
		double tempProductPrice; // the above but as a price
		String tempInput; // what will be storing any input for testing and conversion
		int tempBarCode, quantity; // the bar code and number of each item that we have. 
		int personNumber = -1; // the position of the person in the database
		int productNumber; // as above but for products
		int q1, q2; // test question integer for menus.
		int i, error, added; //minor variables which will be used in multiple parts of the program. 
		error = 0; // setting up error
		boolean admin = false; // how the program knows that it is in admin mode
		boolean sameUser = true; // How the program knows that it is serving the same user. 
		int another = 0; // whether another item will be added
		String[] options = new String[12]; // admin options are stored here. 
		
		// Import the productDatabase
		q2 = 0;
		error = productDatabase.readDatabase("productDatabase.txt"); // read in the product database
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
		while(true) // perminantly loop through this code. 
		{
			boolean first = true; // first item to be added to the cart
			tempInput = ""; // initialise tempInput
				tempInput = JOptionPane.showInputDialog("Enter your PMKeyS");
				if(tempInput == null) { // First check that the PMKeyS was properly entered. This is for the cancel button
					JOptionPane.showMessageDialog(null, "I cannot allow you to close the program Dave. Sorry", "Error", JOptionPane.ERROR_MESSAGE); // 2001 esq error message for a bad PMKeyS
					continue; //start at the top of the while loop. 
				}
				else if(tempInput.equals("") || !isInteger(tempInput) || !personDatabase.personExists(Integer.parseInt(tempInput)) ) { // checks for valid numbers in the PMKeyS
					JOptionPane.showMessageDialog(null, "Please enter your valid PMKeyS number", "Errror", JOptionPane.ERROR_MESSAGE);
					continue;
				}
				tempBarCode = Integer.parseInt(tempInput); // take string from JOptionPane, and make it an integer which is easer to work with
				personNumber = personDatabase.findPerson(tempBarCode); // convert this integer to the person number in the databate.
				sameUser = true; // tells the program that a user is logged in. 
				if(-2 == personNumber) { // checks whether that user is an admin
					admin = true; // the above conversion will return -2 for all admins. This will enact that. 
					sameUser = false; // skip the normal user interface for non admin personnel. 
				}
			while(sameUser) { // avoids people having to re-enter their PMKeyS to get to the shopping cart if they stuff something up. 
				while(!admin) {
					tempInput = JOptionPane.showInputDialog("Hello " + personDatabase.getPersonUser(personNumber) + "\nEnter the bar code of the product you would like");
					if(tempInput != null && !tempInput.equals("") && isInteger(tempInput)) tempBarCode = Integer.parseInt(tempInput); // disallows the user from entering nothing or clicking cancel. 
					else if(tempInput == null && !first) break; // if canceled and not on the first run of adding items. 
					else { // what do do if the user does the above on the first run.
						sameUser = false;
						break;
					}
					productNumber = productDatabase.findProduct(tempBarCode); // Now that we have done the error checking, convert the barcode to a position in the database
					if(productNumber == -1) { // -1 is output by the above on error
						JOptionPane.showMessageDialog(null, "That product does not exist, please try again.");
						continue;
					}
					tempInput = JOptionPane.showInputDialog("Enter the quantity of " + productDatabase.getProductName(productNumber) +" you are purchasing");
					if(tempInput != null && !tempInput.equals("") && isInteger(tempInput)) { // check that a valid integer was entered
						quantity = Integer.parseInt(tempInput);
					}
					else if(tempInput == null && !first) break;
					else if(tempInput == null) { // added in latest version. Lets see whether they like it. 
						sameUser = false; 
						break;
					}
					else quantity = 1; // defult to adding one of the product
					int checkProduct; // create this for use below
					tempInput = productDatabase.getProduct(productNumber);
					checkProduct = checkOuts.productEqualTo(tempInput); // check that the product was not entered into the database before. If it was, just add the quantity to the one in the database
					if(checkProduct != -1) {
						checkOuts.addQuantity(checkProduct, quantity);
					}
					else checkOuts.addProduct(checkOuts.emptyProduct(), productDatabase.getProductRef(productNumber), quantity); //otherwise, add the product as normal. 
					another = JOptionPane.showConfirmDialog(null, "Would you like to add another item?", "Continue", JOptionPane.YES_NO_OPTION);
					if(another != 0) break; // let the user buy already. 
					else first = false; // make the add another product. 
				}
				if(!admin && sameUser) {
					another = JOptionPane.showConfirmDialog(null, "You are purchasing " + checkOuts.getCheckOut(1) + "\nAre you happy with this?", "cart", JOptionPane.YES_NO_OPTION);
					if(another == 0) { // purchise and clean up the system.
						personDatabase.addCost(personNumber, checkOuts.getPrice());// add the bill to the persons account
						checkOuts.productBought(); // clear the quantities and checkout
						productDatabase.writeOutDatabase("productDatabase.txt"); // write out the databases. 
						personDatabase.writeOutDatabase("personDatabase.txt");
						checkOuts = new CheckOut(); // ensure checkout clear
						sameUser = false; // reset to enter PMKeyS
					}
					if(another == 1) { // reset the cart for the user. 
						JOptionPane.showMessageDialog(null, "Your cart has been reset", "reset", JOptionPane.INFORMATION_MESSAGE);
						checkOuts = new CheckOut();
					}
				}
			}
			while(admin) {
				options = new String[]{"add products", "change product", "remove products", "add people", "remove people", "save person database", "save product database", "print the person database to the screen", "print the product database to the screen", "reset bills", "Enter stock counts (bulk)", "Enter stock count (individual)", "leave admin mode", "close the program"}; // admin options

				tempInput = (String)JOptionPane.showInputDialog(null, "Select Admin Option", "Options:", JOptionPane.PLAIN_MESSAGE, null, options, "ham"); // Don't ask me what ham does. 
				if(tempInput == null || tempInput.length() < 1) {
					admin = false;
					break;
				}
				else if(tempInput.equals("add products")) {
					int done = 0;
						while(done != 1) {
						q2 = productDatabase.emptyProduct(); // find the next available product.
						if(q2 != -1) {
							tempInput = JOptionPane.showInputDialog("Please enter the name of the product that you would like to create: ");
							if(tempInput == null || tempInput.length() < 1) continue; // testing that a string was entered
							tempName = tempInput;
							tempInput = JOptionPane.showInputDialog("Please enter the price of the new product: (no dollar Sign, decimals are fine) ");
							if(!isDouble(tempInput) ) continue; // Ensure that the string is a double
							tempProductPrice = Double.parseDouble(tempInput);
							tempInput = JOptionPane.showInputDialog("Please enter the bar code of " + tempName);
							if(!isInteger(tempInput)) continue; // ensure that the string is an integer. 
							tempBarCode = Integer.parseInt(tempInput);
							added = productDatabase.setDatabaseProduct(q2, tempName, tempProductPrice, tempBarCode); 
							// send the values to productDatabase where they will be sent to the product constructor. tempBarCode is multiplied by 60 to get the time in seconds.
							productDatabase.writeOutDatabase("productDatabase.txt"); // ensure that the database has been saved to file
							if(added == 0) { // output on success
									JOptionPane.showMessageDialog(null, tempName + " is now a product in your productDatabase"); 
							}
							else { // output on error
								JOptionPane.showMessageDialog(null, tempName + " is already a product in your productDatabase");
							}
							done = 1; // close the loop
						}
					}
				}
				else if(tempInput.equals("remove products")) {	
					error = 1;
					tempInput = JOptionPane.showInputDialog("Enter the bar code of the item you would like to delete");
					if(!isInteger(tempInput)) continue; // check the input
					tempBarCode = Integer.parseInt(tempInput);
					q2 = productDatabase.findProduct(tempBarCode);	
					if(q2 != -1) {
						error = productDatabase.delProduct(q2); // recieve a one value on an error
						productDatabase.writeOutDatabase("productDatabase.txt");
					}
					if(error != 1 && q2 != 0) {
						JOptionPane.showMessageDialog(null, "The product that you asked for has been deleted.");
						}
					if(error ==  1) {
						JOptionPane.showMessageDialog(null, "The product that you asked for could not be found, please try again");
						q2 = 0;
					}
				}
				else if(tempInput.equals("add people")) {
					int done = 0;
					tempName = "error";
					tempProductPrice = 0;

					added = 1;
					while(done != 1) {
						q2 = personDatabase.emptyPerson(); // find the next available person.
						if(q2 != -1) {
							tempName = JOptionPane.showInputDialog("Enter the name of the person that you would like to add");
							if(tempName == null || tempName.length() == 0) {
								added = 2;
								break;
							}
							tempInput = JOptionPane.showInputDialog("Please enter the PMKeyS of " + tempName);
							if(!isInteger(tempInput)) continue;
							tempBarCode = Integer.parseInt(tempInput);
							added = personDatabase.setDatabasePerson(q2, tempName, 0, 0, tempBarCode); 
							// send the values to productDatabase where they will be sent to the product constructor. tempBarCode is multiplied by 60 to get the time in seconds.
							personDatabase.writeOutDatabase("personDatabase.txt");
						}
						if(added == 0) { // output on success
						JOptionPane.showMessageDialog(null, "Success", tempName + " is now in your person database", JOptionPane.INFORMATION_MESSAGE); // q2 +1 due to the off by one error
						}
						else if(added == 2) continue;
						else { // output on error
							JOptionPane.showMessageDialog(null,"Error",  tempName + " is already in your person database", JOptionPane.INFORMATION_MESSAGE);
						}
						done = 1; // close the loop
					}
				}
				else if(tempInput.equals("remove people")) {
					error = 1;
					tempInput = JOptionPane.showInputDialog("Enter the PMKeyS of the person you would like to delete");
					if(!isInteger(tempInput)) continue;
					tempBarCode = Integer.parseInt(tempInput);
						q2 = personDatabase.findPerson(tempBarCode);	
					if(q2 != -1) {
						error = personDatabase.delPerson(q2); // recieve a one value on an error
						q2++; // confirmation output error
						personDatabase.writeOutDatabase("personDatabase.txt");
					}
					if(error != 1 && q2 != 0) {
						JOptionPane.showMessageDialog(null, "The person that you asked for has been deleted.");
					}
						if(error ==  1) {
						JOptionPane.showMessageDialog(null, "The person that you asked for could not be found, please try again");
						q2 = 0;
						}
				}	
					else if(tempInput.equals("save product database")) {
						error = productDatabase.writeOutDatabase("adminProductDatabase.txt");
						if(error == 0) { // Inform the user of the write out.
						JOptionPane.showMessageDialog(null, "Your Database has been written to adminProductDatabase.txt");
						}
						else { //inform the user of an error in writing the productDatabase to disk.
							JOptionPane.showMessageDialog(null, "Your productDatabase has not been written to disk as there was an error finding productDatabase.txt");
						}
					}	
					else if(tempInput.equals("save person database")) {
						error = personDatabase.adminWriteOutDatabase("adminPersonDatabase.txt");
						if(error == 0) { // Inform the user of the write out.
						JOptionPane.showMessageDialog(null, "Your Database has been written to adminPersonDatabase.txt");
						}
						else { //inform the user of an error in writing the productDatabase to disk.
							JOptionPane.showMessageDialog(null, "Your productDatabase has not been written to disk as there was an error finding person&Database.txt");
						}
					}		
				else if(tempInput.equals("change product")) {
					int tempNumber = 0;
					tempInput = JOptionPane.showInputDialog("Enter the bar code of the product you would like to edit");
					if(!isInteger(tempInput)) {
						continue;
					}
					productNumber = productDatabase.findProduct(Integer.parseInt(tempInput));  // find the position of the product in the database
					if(productNumber == -1) {
						JOptionPane.showMessageDialog(null, "The product that you asked for does not exist");
						continue;
					}
				     // get the new detials of the product.
					tempInput = JOptionPane.showInputDialog("Enter the new name for this product");
					if(tempInput == null || tempInput.length() < 1) continue;
					tempName = tempInput;
					tempInput = JOptionPane.showInputDialog("Enter the new item Price witout the dollar sign");
					if(!isDouble(tempInput)) continue;
					tempProductPrice = Double.parseDouble(tempInput);
					tempInput = JOptionPane.showInputDialog("Enter the new barCode of the product");
					if(!isInteger(tempInput)) continue;
					tempBarCode = Integer.parseInt(tempInput);
					tempNumber = productDatabase.getNumber(productNumber); //returns the number of this product in stock. 
					productDatabase.delProduct(productNumber);
					added = productDatabase.setDatabaseProduct(productDatabase.emptyProduct(), tempName, tempProductPrice, tempBarCode);
					productDatabase.setNumber(productDatabase.findProduct(tempBarCode), tempNumber);
					productDatabase.writeOutDatabase("productDatabase.txt");
					if(added == 0) {
						JOptionPane.showMessageDialog(null, "Your product has been changed in the database");
					}
					else { // output when the user tries to edit the product to one that already exists.
						JOptionPane.showMessageDialog(null, "The new data that you tried to use for this product is already in use in the productDatabase");
					}
				}
				else if(tempInput.equals("reset bills")) {
					another = 1;
					another = JOptionPane.showConfirmDialog(null, "Are you sure that you would like to reset the TOC bills?", "Are you sure?", JOptionPane.YES_NO_OPTION);
					if(another == 0) {
						personDatabase.resetBills(); // reset the bills
						personDatabase.writeOutDatabase("personDatabase.txt"); // save the new database to file
						JOptionPane.showMessageDialog(null, "Bills reset");
					}
					else {
						JOptionPane.showMessageDialog(null, "Bills have not been reset.");
						continue;
					}
				}
				else if(tempInput.equals("print the person database to the screen")) {
					JTextArea textArea = new JTextArea(personDatabase.getDatabase(1)); // create the text to be displayed
					JScrollPane scrollPane = new JScrollPane(textArea); // create the scrolling window for the text
					textArea.setLineWrap(true); // force line wrap, this should not be needed, but is enabled anyway. 
					textArea.setWrapStyleWord(true); // make wrap work on a perword basis rather than percharacter
					scrollPane.setPreferredSize(new Dimension(500,500)); //set the size of the scrollpane
					JOptionPane.showMessageDialog(null, scrollPane, "Person Database", JOptionPane.INFORMATION_MESSAGE); // output the scrollpane. 
				}
				else if(tempInput.equals("print the product database to the screen")) { // see above
					JTextArea textArea = new JTextArea(productDatabase.getDatabase(1));
					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					scrollPane.setPreferredSize(new Dimension(500,500));
					JOptionPane.showMessageDialog(null, scrollPane, "Product Database", JOptionPane.INFORMATION_MESSAGE);
				}
				else if(tempInput.equals("Enter stock counts (bulk)")) {
					int tempNumber = 0;
					for(i = 0; productDatabase.productExists(i); i++) { // for each product ask for the new number of items you have. 
						tempInput = JOptionPane.showInputDialog("You have " + productDatabase.getNumber(i) + " " + productDatabase.getProductName(i) + " left from last stocktake\n Including these, how many do you have now?");
						if(!isInteger(tempInput)) continue;
						tempNumber = Integer.parseInt(tempInput);
						productDatabase.setNumber(i, tempNumber);
					}
					productDatabase.writeOutDatabase("productDatabase.txt");
				}
				else if(tempInput.equals("Enter stock count (individual)")) {
					int tempNumber = 0;
					tempInput = JOptionPane.showInputDialog("Enter the bar code of the product you would like to set");
					if(!isInteger(tempInput)) continue;
					tempBarCode = Integer.parseInt(tempInput);
					productNumber = productDatabase.findProduct(tempBarCode); // corrilate the product number with that in the database
					if(productNumber == -1) {
						JOptionPane.showMessageDialog(null, "The product that you asked for does not exist");
						continue;
					}
					tempInput = JOptionPane.showInputDialog("You had " + productDatabase.getNumber(productNumber) + " " + productDatabase.getProductName(productNumber) + " left from last stocktake\n Inclunding these, how many do you have now?"); // ask the user how many they had, and how many they now have
					if(!isInteger(tempInput)) continue;
					tempNumber = Integer.parseInt(tempInput);
					productDatabase.setNumber(productNumber, tempNumber); // enter all this into the database and write it out.
					productDatabase.writeOutDatabase("productDatabase.txt");
				}
				else if(tempInput.equals("close the program")) {
					personDatabase.writeOutDatabase("personDatabase.txt"); // write any somehow missed changes out and then exit
					productDatabase.writeOutDatabase("productDatabase.txt");
					System.exit(0);
				}
				else {
					admin = false; // somehow picked an option which doesn't exist or simpily pressed cancel. 
					break;
				}
			}
		}
	}
	public static void main(String[] args)
	{

		Interface intFace = new Interface(); // initalise task
	
		intFace.run(); // Engage

	}
	public CheckOut[] resizeCheckOut(Boolean action, CheckOut[] resizing)
	{
		if(action) {
			CheckOut[] resized = new CheckOut[resizing.length + 1]; // extend the checkOut length by one
			for(int i = 0; i < logicalSize; i++) {
				resized[i] = resizing[i]; // copy old to new
			}
			return resized;
		}
		else {
			CheckOut[] resized = new CheckOut[resizing.length - 1]; // shorten the checkOut by one.
			for(int i = 0; i < logicalSize-1; i++) {
				resized[i] = resizing[i]; // copy old to new.
			}
			return resized;
		}
		
	}
	public boolean isInteger(String s) 
	{
		try { 
			Integer.parseInt(s); // try to parse the string, catching a failure
		} 
		catch(NumberFormatException e) { 
		 return false; 
		}
		// only got here if we didn't return false
		return true;
	}
	public boolean isDouble(String s) 
	{
		try { 
			Double.parseDouble(s);  // try to parse the string, catching a failure
		} 
		catch(NumberFormatException e) { 
		 return false; 
		}
		// only got here if we didn't return false
		return true;
	}
} // and that's a wrap. Computer, disable all command functions and shut down for the night. I'll see you again in the morning.      
