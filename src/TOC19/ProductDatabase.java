/****
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
* Class: ProductDatabase
* Description: This program will allow for the input and retreval of the product database and will set the limits of the database.
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public final class ProductDatabase
{
	private Product[] allProducts;
	private int logicalSize;
//	private String output;
//	private File file;
//	private PrintWriter outfile;
//	private Scanner readOutFile;
//	private int i;
	
	public ProductDatabase()
	{
		allProducts = new Product[10];
		logicalSize = 0;
//		output = "";
	}
	
	
	public final int setDatabaseProduct(int productNo, String name, long price, long barCode) // take the products data and pass it to the products constructor
	{
		/**
		Class ProductDatabase: Method setDatabase
		Precondition: augments int productNo, String name, String artist, double size, double duration are input
		Postcondition: Data for the currant working product in this database will be set. 
		*/
		int test = 0; // using test because something keeps changing my i...
		if(!productExists( name)) { // check whether the product already exists
		    allProducts[logicalSize] = new Product(name, price, barCode); // pass off the work to the constructor: "make it so."
		    logicalSize++; // We have a new product, Now we have something to show for it.
			test = 1;
			writeOutDatabase("productDatabase.txt");
		}
		if(logicalSize >= allProducts.length) { // of the database is getting to big for it's array, something has to budge
			allProducts = resizeDatabase(true, allProducts); // This will make the array budge, I can't leave that to the user, they never do.
		}
		if(test == 1) { 
			return 0; // tell the program that everyithng went well so that it can inform the user
		}
		else return 1; // Something went wrong, probably that user trying to add two products with the same name and artist. Now I have to tell them off.
	}
	public final String getDatabase(int sort) 
	{
		/**
		Class ProductDatabase: Method getDatabase
		Precondition: setDatabase has been run
		Postcondition: the user will be see an output of the products in the database. 
		*/
		
		this.sortBy(sort); // find the sorting method that they asked for and use it to sort the database.
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < logicalSize; i++) { // loop until the all of the databases data has been output
			if(allProducts[i] != null) {
				output.append(String.format("\nProduct %d:\n",1+i));
				output.append(allProducts[i].getData());
			}
		}
		sortBy(3); // binary search
		return output.toString(); // send the calling program one large string containing the ingredients of all the products in the database
	}
	
	public final String productsUnder(long price, int sort)
	{
		/**
		Class ProductDatabase: Method productsUnder
		Precondition: setDatabase has been run, paremeters time and sort type have been passed as double and int respectively
		Postcondition: the user will be given a list of all of the products under the specified time in the order requested.
		*/
		
		int test = 1; // check whether any of the products matched the users search.
		StringBuilder output = new StringBuilder("");
		this.sortBy(sort); // Find the requested sorting method and call it. Much shorter this way, same code, implemented once. 
		for(int i = 0; i < logicalSize; i++) { // loop untill all products have been tested.
			if(allProducts[i] != null && allProducts[i].getBarCode() < price) { // test whether each product is under the specified size
				test = 0;
				output.append(allProducts[i].getData()); // output the data of the recently tested product
			}
		}
		if(test == 1) {
			output.append("No products match your search"); // output on a search that gives no products
		}
		sortBy(3); // binary search
		return output.toString(); // pass the results back to the interface
	}

	public final String getProduct(int productNo) 
	{
		/**
		Class ProductDatabase: Method getProduct
		Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4
		Postconditions: the user will see the details of their chosen product output.
		*/

		if(productNo < logicalSize) { // check that the product exists
			return allProducts[productNo].getData(); // now that we know that it does, send it to the interface
		}
		else {
		    return "error"; // We cannot find the product that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}
	public final int delProduct(int productNo)
	{
		/**
		Class ProductDatabase: Method delProduct
		Preconditions: setDatabase has been run, productNo is an integer paremeter
		Postconditions: the chosen product will no longer exist. The success or failure of this will be given by a 0 or 1 returned respectively.
		*/ 
		if(productNo < logicalSize) { //check that the product the user doesn't want anymore actually exists in the first place.
			for (int i = productNo; i < logicalSize; i++) { // move all products back one place. The first move will overwrite the now unwanted product
				allProducts[i] = allProducts[i+1]; // I really hope that they didn't have any desire to see this one agian. It's already in the bin. 
			}
			--logicalSize; // likewise for the logicalSize of the database.
			if(allProducts.length > 2 * logicalSize) { // if the array for the database is getting a little big, save some memory and shorten it. 
				allProducts = resizeDatabase(false, allProducts);
			}
			writeOutDatabase("productDatabase.txt");
			return 0; // everything went fine. We will have to tell someone about that. 
		}
		else return 1; // Telling the user yet again that their product doesn't exist. At least thay don't need to delete it now.

	}
	public final String getProductName(int productNo) 
	{
		/**
		Class ProductDatabase: Method getProductName
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the product name will be returned
		*/
		if(productNo < logicalSize) { // check that the desired product exists
			return allProducts[productNo].getName(); // now that we know it does, give it to the interface
		}
		else {
			return "error"; // nope, the product does not exist. Most likely PICNIC
		}

	}

	public final double getProductPrice(int productNo)
	{
		/** 
		Class ProductDatabase: Method getProductSize
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the size of the invoking product will be returned as a double
		*/
		if(productNo < logicalSize) { // to understand its workings, see getProductName. it works the same but this returns a double
			return allProducts[productNo].productPrice();
		}
		else return 0;
	
	}
	public final long getBarCode(int productNo)
	{
		/** 
		Class ProductDatabase: Method getProductBarCode
		Precondition: setDatabase has been run for the invoking product
		Postcondition: the duration of the invoking product will be returned as a double
		*/
		if(productNo < logicalSize) { // to understand its workings, see getProductName. it works the same but this returns a double
			return allProducts[productNo].getBarCode();
		}
		else {
			return 0;
		}
	
	}
	public final int emptyProduct()
	{
		/**
		Class ProductDatabase: Method emptyProduct
		Preconditions: none
		Postconditions: the number of the first found empty product will be returned an as integer or -1 will be returned if there are no empty products
		*/
		for(int i = 0; i < allProducts.length; i++) { // keep looping until an empty product has been found
			if(allProducts[i] == null) {
				return i; // pass back the empty product to the caller
			}
		}
		return -1; // there are no empty products. have a -1 so you know what happened.
	}
	public final boolean productExists(String extProductName)
	{
	    
	    for(int i = 0; i < logicalSize; i++) { //loop until a product that matches the artist and name specified
			if(allProducts[i] != null && allProducts[i].getName().equals(extProductName)) {
				return true; // when one is found, send true back to the caller
			}
	    }
	    return false; // if you are running this, no product was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}
	public void resizeDatabase(boolean action)
	{
		allProducts = resizeDatabase(action, allProducts); // when calling for a resise outside this class, one needs to be able to access the database array to send it.
															//Thanks to this little trick of signatures, they now have it. 
	}
	
	public Product[] resizeDatabase(Boolean action, Product[] resizing)
	{
		if(action) { // make the database 4 products bigger so that we have more room. 
			return (Arrays.copyOf(resizing, resizing.length + 4));
		}
		else if (resizing.length > 4) { // do exactly the same as the last if statement, but make the array half the size that it is now
			return (Arrays.copyOf(resizing, resizing.length/2));
		}
		else { // if all eles fails, there must be less than 4 products and the array must be short, make it 4 places long
			return (Arrays.copyOf(resizing, 4));
		}
		
	}
	public final int partitionByName(int lb, int  ub)
	{
		Product pivotElement = allProducts[lb]; // store the left most value as the pivot element
		int max = logicalSize;
		int left = lb, right = ub; // store left and right as the unchanging values of lb and ub respectively. This is used for the final move of this method
		Product temp; // create a temp for swapping thing around.

		while (left < right)
		{
			while((allProducts[left].getName().compareToIgnoreCase(pivotElement.getName()) <= 0) && left+1 < max) { // test the order of the products on left side
				left++;
			}
			while((allProducts[right].getName().compareToIgnoreCase(pivotElement.getName()) > 0) && right -1 >= 0) { // test the order of the products on right side
				right--;
			}
			if (left < right) // swap the left and right products, remember that temp variable, this is where it shines.
			{
				temp        = allProducts[left];
				allProducts[left]  = allProducts[right];
				allProducts[right] = temp;
			}
		}
		for(left = lb; left <= right && left+1 < max; left++) { //move the products one place to the left for all products after the pivot element
			allProducts[left] = allProducts[left +1];
		}
		allProducts[right] = pivotElement; // move the pivot element to its place at right, where it is now in the correct order.
		return right; // return the new pivot (see quick sort)
	}

	public final void quickSortByName(int left, int right)
	{
		if (left < right) // start the sort.
		{
			int pivot = partitionByName(left, right); // run the first instance of the sort
			quickSortByName(left, pivot-1); // recursively sort smaller sections of the array untill the sections are one element long
			quickSortByName(pivot+1, right); // do the above for the right of the pivot
		}
	}
	public final int partitionByPrice(int lb, int  ub)
	{ // for an outline of how this works, see the partition method for name
		Product pivotElement = allProducts[lb];
		int max = logicalSize;
		int left = lb, right = ub;
		Product temp;

		while (left < right)
		{
			while((allProducts[left].productPrice() <= pivotElement.productPrice()) && left+1 < max) {
				left++;
			}
			while((allProducts[right].productPrice() > pivotElement.productPrice()) && right -1 >= 0) {
				right--;
			}
			if (left < right)
			{
				temp        = allProducts[left];
				allProducts[left]  = allProducts[right];
				allProducts[right] = temp;
			}
		}
		for(left = lb; left <= right && left +1 < max; left++) {
			allProducts[left] = allProducts[left +1];
		}
		allProducts[right] = pivotElement;
		return right;
	}

	public final void quickSortByPrice(int left, int right)
	{ // for an outline of how this works, see the sorting method for name
		if (left < right)
		{
			int pivot = partitionByPrice(left, right);
			quickSortByPrice(left, pivot-1);
			quickSortByPrice(pivot+1, right);
		}
	}
	public final int partitionByBarCode(int lb, int  ub)
	{ // for an outline of how this works, see the partition method for name
		Product pivotElement = allProducts[lb];
		int max = logicalSize;
		int left = lb, right = ub;
		Product temp;

		while (left < right)
		{
			while((allProducts[left].getBarCode() <= pivotElement.getBarCode()) && left +1 < max) {
				left++;
			}
			while((allProducts[right].getBarCode() > pivotElement.getBarCode()) && right -1 >= 0) {
				right--;
			}
			if (left < right)
			{
				temp        = allProducts[left];
				allProducts[left]  = allProducts[right];
				allProducts[right] = temp;
			}
		}
		for(left = lb; left <= right && left+1 < max; left++) {
			allProducts[left] = allProducts[left +1];
		}
		allProducts[right] = pivotElement;
		return right;
	}

	public final void quickSortByBarCode(int left, int right)
	{ // for an outline of how this works, see the sorting method for name
		if (left < right)
		{
			int pivot = partitionByBarCode(left, right);
			quickSortByBarCode(left, pivot-1);
			quickSortByBarCode(pivot+1, right);
		}
	}

	public final int writeOutDatabase(String path) 
	{
		this.quickSortByName(0, logicalSize-1); // ensure that the database is sorted.
		PrintWriter outfile = null;
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created. 
		}
		catch(FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
			outfile.println("ProductDatabase File"); // print the file header
			for(int b = 0; b < logicalSize; b++) { // repeatedly print the data of the products in the database to the file. 
				outfile.println("-------------------------------------");
				outfile.println(allProducts[b].getName());
				outfile.println((double)allProducts[b].productPrice()/100);
				outfile.println(allProducts[b].getBarCode());
				outfile.println(allProducts[b].getNumber());
				
			}
			outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive 
			sortBy(3); // binary search
			return 0; // let the program and thus the user know that everything is shiny. 
	}
	public final int adminWriteOutDatabase(String path) 
	{
		this.quickSortByName(0, logicalSize-1); // ensure that the database is sorted.
		PrintWriter outfile = null;
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created. 
		}
		catch(FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
			outfile.println("ProductDatabase File"); // print the file header
			for(int b = 0; b < logicalSize; b++) { // repeatedly print the data of the products in the database to the file. 
				outfile.println("-------------------------------------");
				outfile.println("Product Name: " + allProducts[b].getName());
				outfile.println("Product Price: " + (double)allProducts[b].productPrice()/100);
				outfile.println("Product Bar Code: " + allProducts[b].getBarCode());
				outfile.println("Stock Count: " + allProducts[b].getNumber());
				
			}
			outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive 
			quickSortByBarCode(0, logicalSize-1);
			return 0; // let the program and thus the user know that everything is shiny. 
	}
	public final int readDatabase(String path) 
	{
		String tempName, tempInput;
		long tempProductPrice;
		double doubleProductPrice;
		long tempBarCode;
		int tempNumberOfProduct;
		boolean negative = false;
		int count = 0;
		int z;
		Scanner readOutFile = null; 
		try {
			File file = new File(path); // if this fails, chances are the user hit 2 and imput a file that doesn't exist. 
			readOutFile = new Scanner(file); // create the scanner 
			readOutFile.nextLine(); // header
			for(z = 0; readOutFile.hasNext(); z++) { // until all of the lines have been read, I want to read the lines.
				readOutFile.nextLine(); // someone decided to put a redundant line in each product of the file, this throws it away.
				tempName = readOutFile.nextLine();
				doubleProductPrice = Double.parseDouble(readOutFile.nextLine());
				tempProductPrice = (long)(doubleProductPrice*100);
				tempBarCode = Long.parseLong(readOutFile.nextLine());
				tempInput = readOutFile.nextLine();
				if('-' == tempInput.charAt(0)) {
					tempInput = tempInput.substring(1);
					negative = true;
				}
				tempNumberOfProduct = Integer.parseInt(tempInput);
				if(negative) {
					tempNumberOfProduct *= -1;
					negative = false;
				}
				count += this.setDatabaseProduct(z, tempName, tempProductPrice, tempBarCode); // send the big pile of lines that we just read to the product constructor. 
				allProducts[z].setNumber(tempNumberOfProduct);
			}
			readOutFile.close(); // clean up by closing the file
			quickSortByBarCode(0,logicalSize-1);
			return z - count; // tell the program how many products we just got. If it's more than a thousand, I hope the sort doesn't take too long. 
		}
		catch(FileNotFoundException e) {
			if (readOutFile != null) readOutFile.close(); // Well, if something goes wrong, someone should find out. 
			return -1; // this is what we use to tell them that something we didn't expect happened. Like the user assuring me that the file exists.
		}
	}
	public final void sortBy(int sort)
	{
		switch(sort) { // Rather than place this switch every time the sort is used, Call this.
			case (1): this.quickSortByName(0, logicalSize-1); // when sort is one: call sort by name
						break;
			case (2): this.quickSortByPrice(0, logicalSize-1); // when sort is Three: call sort by size
						break;
			case (3): this.quickSortByBarCode(0, logicalSize-1); // when sort is Four: call sort by duration
						break;
			default: this.quickSortByName(0, logicalSize-1); // when the user imputs the wrong value or there is another issue, default to sort by name.
						break;
		}
	}
	public final int findProduct(long barCode)
	{
		for(int i = logicalSize -1; i > 0; i--) {
			if(allProducts[i].getBarCode() == barCode) return i;
		}
		return -1;
	}
	public final boolean productExists(int number)
	{
		return (number < logicalSize);
	}
	public final int getNumber(int productNo)
	{
		return allProducts[productNo].getNumber();
	}
	public final void setNumber(int productNo, int number)
	{
		allProducts[productNo].setNumber(number);
	}
	public final Product getProductRef(int productNo)
	{
		return allProducts[productNo];       
    }
	public final int binarySearch(long extBarCode)
	{
		int iMax = logicalSize-1;
		int iMin = 0;
		int mid;
		while (iMax >= iMin) {
			mid = (iMax+iMin)/2;
			if(allProducts[mid].getBarCode() == extBarCode)
				return mid;
			else if (allProducts[mid].getBarCode() > extBarCode)
				iMax = mid-1;
			else if (allProducts[mid].getBarCode() < extBarCode)
				iMin = mid+1;
		}
		return -1;
	}
	public final String[] getProductNames() {
		String[] output = new String[logicalSize];
		for(int i = 0; i < logicalSize; i++) {
			output[i] = allProducts[i].getName();
		}
		return output;
	}
}