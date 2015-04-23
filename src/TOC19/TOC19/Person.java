package TOC19;

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
 * This program will allow for the input and retrieval of persons in the person database.
* @author Jarrah Gosbell
*/

public final class Person implements java.io.Serializable
{
	/** The name of the person */
	private String name;
	/** The total amount the person has spent using the program. */
	private long totalCostRunning;
	/** The cost of the current bill for the person */
	private long totalCostWeek;
	/** The persons user number or barcode, used to log in */
	private long barCode; // PMKeys number off ID
	/** whether or not the user can buy from the program */
	private boolean canBuy;
	/** A version number to stop java breaking the serialisation storage */
    private static final long serialVersionUID = 126491946;

    /**
     * Create a new person with the given name, barcode, total cost, bill cost and the ability to buy or not.
     * @param extName The name of the person
     * @param extBarCode the barcode of the person
     * @param running The total bill of the person
     * @param week The current bill of the person.
     * @param extCanBuy Whether the person can buy or not.
     */
	public Person(String extName, long extBarCode, long running, long week, boolean extCanBuy) // constructor which will give the person its values
	{
		name = extName;
		barCode = extBarCode;
		totalCostRunning = running;
		totalCostWeek = week;
		canBuy = extCanBuy;
	}

    /**
     * Get the users data in a human readable format
     * @return The name, running cost and bill cost of the person.
     */
	public final String getData() // output the person data as a string
	{
		StringBuilder output = new StringBuilder();
		output.append("");
		output.append("\n	Name: ");
		output.append(name);
		output.append("\n	Running Cost: $");
		output.append((double)totalCostRunning/100);
		output.append("\n	Weekly Cost: $");
		output.append((double)totalCostWeek/100);

		return output.toString(); //return a string with all of the person's data in it
	}

    /**
     * Get the name of the person.
     * @return The name of the person
     */
	public final String getName() // return the name of the person
	{
		/**
		Class Person: Method getData
		Precondition: setData has been run for invoking person
		Post condition: the method will return a string containing the name.
		*/

		return name;
	}

    /**
     * Get the barcode of the person.
     * @return The barcode of the person.
     */
	public final long getBarCode() // return the barcode associated with the person
	{
		 /**
		 Class Person: Method getBarCode
		 Precondition: SetData has been run for the invoking person
		 Psotcondition: The method will return an int containing the barcode of the item. 
		 */
		return barCode;
	}

    /**
     * Get the total bill of the person since their addition to the program.
     * @return The total bill of the person as a double
     */
	public final double totalCostRunning()
	{
		/**
		Class Person: Method totalCostRunning
		Precondition: setData has been run for the invoking person
		Postcondition: this method will return the price of the invoking person.
		*/
		
		return (double)totalCostRunning/100;
	}

    /**
     * Get the current bill cost of the person.
     * @return The current bill cost of the person as a double.
     */
	public final double totalCostWeek() // return the size of the person.
	{
		/**
		Class Person: Method totalCostRunning
		Precondition: setData has been run for the invoking person
		Postcondition: this method will return the price of the invoking person.
		*/
		
		return (double)totalCostWeek/100;
	}

    /**
     * add the given cost to the bill of the person.
     * @param cost The amount to be added as a long with the final two digits representing cents.
     */
	public final void addPrice(long cost)
	{
		totalCostRunning += cost;
		totalCostWeek += cost;
	}

    /**
     * Reset the bill for this person.
     */
	public final void resetWeekCost()
	{
		totalCostWeek = 0;
	}

    /**
     * Set the name of the person.
     * @param extName the new name for the person.
     */
	public final void setName(String extName)
	{
		name = extName;
	}

    /**
     * Get whether the person can buy from the program.
     * @return Whether the user is allowed to buy.
     */
	public final boolean canBuy()
	{
		return canBuy;
	}

    /**
     * Set whether the user can buy from the program.
     * @param extCanBuy Whether you want the user to be able to buy from the program.
     */
	public final void setCanBuy(boolean extCanBuy)
	{
		canBuy = extCanBuy;
	}

}