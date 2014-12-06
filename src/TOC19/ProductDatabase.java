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
import TOC19.SQLInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public final class ProductDatabase
{
	private int logicalSize;
	private SQLInterface sql;
	
	public ProductDatabase()
	{
		logicalSize = 0;
	}
	
	
	public final void setDatabaseProduct(long barCode, String name, long price) // take the products data and pass it to the products constructor
	{
		sql.SQLInsert("product", "(" + name + ", " + + barCode + ", " + price + ")");
	}

	public final String getDatabase(int sort) 
	{
		/**
		Class ProductDatabase: Method getDatabase
		Precondition: setDatabase has been run
		Postcondition: the user will be see an output of the products in the database. 
		*/
		// this should convert the whole table to a string. 

		return sql.SQLReadSet("product", "", "", "").toString();
	}
	
	public final String getProduct(int barCode) 
	{
		/**
		Class ProductDatabase: Method getProduct
		Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4
		Postconditions: the user will see the details of their chosen product output.
		*/

		return sql.SQLRead("products", "", "barcode", Integer.toString(barCode));

	}
	public final void delProduct(long barCode)
	{
		/**
		Class ProductDatabase: Method delProduct
		Preconditions: setDatabase has been run, barCode is an integer paremeter
		Postconditions: the chosen product will no longer exist. The success or failure of this will be given by a 0 or 1 returned respectively.
		*/ 
		
		sql.SQLDelete("product", "barcode",Long.toString(barCode));

	}
	public final void delProduct(String name) {
		/**
		 * Class PersonDatabase: Method delPerson Preconditions: setDatabase has been run, personNo is an integer paremeter Postconditions: the chosen person will no longer exist. The success or
		 * failure of this will be given by a 0 or 1 returned respectively.
		 */
		sql.SQLDelete("product", "name", name);
	}
	public final String getProductName(String barCode) 
	{
		/**
		Class ProductDatabase: Method getProductName
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the product name will be returned
		*/
			return sql.SQLRead("product", "name", "barcode", barCode);
	}
	public final String getProductBarCode(String name) 
	{
		/**
		Class ProductDatabase: Method getProductName
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the product name will be returned
		*/
			return sql.SQLRead("product", "name", "name", name);
	}

	public final double getProductPrice(String extBarCode)
	{
		/** 
		Class ProductDatabase: Method getProductSize
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the size of the invoking product will be returned as a double
		*/
			return Double.parseDouble(sql.SQLRead("product", "price", "barcode", extBarCode));
	}
	public final boolean productExists(String extProductName)
	{
	    
	    for(int i = 0; i < logicalSize; i++) { //loop until a product that matches the artist and name specified
			if(sql.SQLRead("products", "name", "name", extProductName) != null) { // not sure whether this will actually return null if the product does not exist. Check. 
				return true; // when one is found, send true back to the caller
			} // change this to check whether nothing is returned. 
	    }
	    return false; // if you are running this, no product was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

	public final int adminWriteOutDatabase(String path) 
	{
		return sql.SQLOutputCSV("product", path) ? 0 : 1;
	}
	public final int getNumber(long barCode)
	{
		return Integer.parseInt(sql.SQLRead("products", "number", "barcode", Long.toString(barCode)));
	}
	public final int getNumber(String name)
	{
		return Integer.parseInt(sql.SQLRead("products", "number", "name", name));
	}
	public final void setNumber(long barCode, int number)
	{
//		allProducts[barCode].setNumber(number);
		sql.SQLSet("product", "number", Integer.toString(number), "barcode", Long.toString(barCode));
	}
	public final void setNumber(String name, int number)
	{
//		allProducts[barCode].setNumber(number);
		sql.SQLSet("product", "number", Integer.toString(number), "name", name);
	}
	public final String[] getProductNames() {
		return sql.SQLReadSet("product", "name", "", "");
	}
	public final void productBought(String[][] itemNumbers)
	{
		for (String[] itemNumber : itemNumbers) {
			int number;
			number = Integer.parseInt(sql.SQLRead("product", "number", "name", itemNumber[0]));
			number -= Long.parseLong(itemNumber[1]);
			sql.SQLSet("product", "number", Integer.toString(number), "name", itemNumber[0]);
		}
	}
	
}