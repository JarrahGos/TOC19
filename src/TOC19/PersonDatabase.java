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

import TOC19.SQLInterface;
import java.io.FileNotFoundException;
import TOC19.Settings;

public final class PersonDatabase {

	private static Person admin;
	private static int logicalSize;
	private SQLInterface sql;
	private Settings config;

	public PersonDatabase() throws FileNotFoundException
	{
		logicalSize = 0;
		String[] settings = null;
		config.adminSettings();
		admin.setBarCode(Long.parseLong(settings[0]));
		admin.setName(settings[1]);
	}

	public final void setDatabasePerson(String name, long running, long week, long barCode, boolean canBuy) // take the persons data and pass it to the persons constructor
	{
		sql.SQLInsert("person", "(" + name + ", " + running + ", " + week + ", " + barCode + ", " + canBuy + ")");
	}
	public final String getDatabase(int sort) {
		/**
		 * Class PersonDatabase: Method getDatabase Precondition: setDatabase has been run Postcondition: the user will be see an output of the persons in the database.
		 */

		return sql.SQLReadSet("person", "", "", "").toString();
	}

	public final String getPerson(long barCode) {
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their chosen
		 * person output.
		 */

		return sql.SQLRead("person", "", "barcode", Long.toString(barCode));
	}

	public final String getPersonUser(long barCode, boolean html) {  
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their 
		 *							chosen
		 * person output.
		 */

		if(barCode == 7000000) return "admin";
		
		StringBuilder output = new StringBuilder();
		if(html) {
			output.append(sql.SQLRead("person", "name", "barcode", Long.toString(barCode)));
			output.append("<br>	Current Bill Total: $");
			output.append(Double.parseDouble(sql.SQLRead("person", "priceBill", "barcode", Long.toString(barCode)))/100);
		}
		else {
			output.append(sql.SQLRead("person", "name", "barcode", Long.toString(barCode)));
			output.append("\n	Running Cost: $");
			output.append(Double.parseDouble(sql.SQLRead("person", "priceYear", "barcode",Long.toString(barCode)))/100);
			output.append("\n	Current Bill Total: $");
			output.append(Double.parseDouble(sql.SQLRead("person", "priceBill", "barcode", Long.toString(barCode)))/100);
		}
		return output.toString();
		// now that we know that it does, send it to the interface
		
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD

	}

	public final void delPerson(long barCode) {
		/**
		 * Class PersonDatabase: Method delPerson Preconditions: setDatabase has been run, personNo is an integer paremeter Postconditions: the chosen person will no longer exist. The success or
		 * failure of this will be given by a 0 or 1 returned respectively.
		 */
		sql.SQLDelete("person", "barcode", Long.toString(barCode));
	}
	public final void delPerson(String name) {
		/**
		 * Class PersonDatabase: Method delPerson Preconditions: setDatabase has been run, personNo is an integer paremeter Postconditions: the chosen person will no longer exist. The success or
		 * failure of this will be given by a 0 or 1 returned respectively.
		 */
		sql.SQLDelete("person", "name", name);
	}
	public final String getPersonName(long barCode) {
		/**
		 * Class PersonDatabase: Method getPersonName Preconditions: setDatabase has been run for the invoking person Postconditions: the person name will be returned
		 */

		return sql.SQLRead("person", "name", "barcode", Long.toString(barCode));
	}
	public final String getPersonBarCode(String name) 
	{
		/**
		Class ProductDatabase: Method getProductName
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the product name will be returned
		*/
			return sql.SQLRead("person", "name", "name", name);
	}
	public final double getPersonPriceYear(long extBarCode) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		return Double.parseDouble(sql.SQLRead("person", "priceYear", "barcode", Long.toString(extBarCode)));
	}

	public final double getPersonPriceWeek(long extBarCode) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		return Double.parseDouble(sql.SQLRead("person", "pricebill", "barcode", Long.toString(extBarCode)));
	}
	public final boolean personExists(String extPersonName) {
		// not sure whether this will actually return null if the product does not exist. Check.
		
	    return sql.SQLRead("products", "name", "name", extPersonName) != null;
	}

	public final boolean personExists(long extBarCode) {
		// not sure whether this will actually return null if the product does not exist. Check.
		
	    return sql.SQLRead("products", "name", "barcode", Long.toString(extBarCode)) != null;
	}

	public final int adminWriteOutDatabase(String path) {
		return sql.SQLOutputCSV("person", path) ? 0 : 1;
	}
	public final long findPerson(long barCode) {
		if (7000000 == barCode) {
			return -2;
		}
		else return Long.parseLong(sql.SQLRead("person", "barcode", "barcode", Long.toString(barCode))); // if this does not return a user, this should return -1
	}

	public final void addCost(long barCode, long cost) {
		long price;
		price = Integer.parseInt(sql.SQLRead("person", "priceBill", "barcode", Long.toString(barCode))) + cost; 
		sql.SQLSet("person", "priceBill", Long.toString(price) + "'", "barcode", Long.toString(barCode));
		price = Integer.parseInt(sql.SQLRead("person", "priceYear", "barcode", Long.toString(barCode))) + cost;
		sql.SQLSet("person", "priceYear", Long.toString(price) + "'", "barCode", Long.toString(barCode));
	}

	public final void resetBills() {
		sql.SQLSet("person", "priceBill", "0", "", ""); // not sure whether this should be "" or "*"
	}
	public final void setAdminPassword(String extPassword) {
		admin.setName(extPassword); // a read and write file should be added to this. Maybe write that into the JSON config? (thas is, if I make one)
		try {
			config.adminSetPassword(extPassword); 
		}
		catch (FileNotFoundException e) {
			System.out.print("could not save the new admin password. Use the old one\n" + e.toString());
		}
	}
	public final boolean personCanBuy(long barCode)
	{
		return Boolean.parseBoolean(sql.SQLRead("person", "canBuy", "barcode", Long.toString(barCode)));
	}
	public final boolean personCanBuy(String name)
	{
		return Boolean.parseBoolean(sql.SQLRead("person", "canBuy", "name", name));
	}
	public final void setPersonCanBuy(long barCode, boolean canBuy)
	{
		sql.SQLSet("person", "canBuy", Boolean.toString(canBuy) + "'", "barcode", Long.toString(barCode));
	}
	public final void setPersonCanBuy(String name, boolean canBuy)
	{
		sql.SQLSet("person", "canBuy", Boolean.toString(canBuy) + "'", "name", name);
	}
	public final String[] getUserNames()
	{
		return sql.SQLReadSet("person", "name", "", "");
	}
	
}
