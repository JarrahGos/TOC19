package TOC19;

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
* Class: Person
* Description: This program will allow for the input and retreval of persons in the person database.
*/

public final class Person implements java.io.Serializable
{

	// create the variables that are needed in order of use
	private String name;
//	private StringBuilder output;
	private long totalCostRunning, totalCostWeek; // Running can be yearly, or can be perminant. Up to TOC. Weekly will be reset each stocktake
	private long barCode; // PMKeys number off ID
	private boolean canBuy;
    private static final long serialVersionUID = 126491946;

	public Person(String extName, long extBarCode, long running, long week, boolean extCanBuy) // construtor which will give the person its values
	{
		name = extName;
		barCode = extBarCode;
		totalCostRunning = running;
		totalCostWeek = week;
		canBuy = extCanBuy;
	}
	// Begin methods
	public final void setData(String name, int barCode, boolean extCanBuy) // redundant method which has been left in case persons are edited in some way which would require this to be used.
	{
		/**
		Class Person: Method setData
		Precondition: Augments String name, String artist, double totalCostRunning, double time are input
		Postcondition: the person that this method was invoked with now has been stored.
		*/
		this.name = name;
		this.barCode = barCode;
		canBuy = extCanBuy;
	}

	public final String getData() // output the person data as a string
	{
		/**
		Class Person: Method getData
		Procondition: setData has been run for invoking person or the person constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking person will be returned. 
		*/
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
		public final String getDataUser(Boolean html) // output the person data as a string
	{
		/**
		Class Person: Method getData
		Procondition: setData has been run for invoking person or the person constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking person will be returned. 
		*/
			
		StringBuilder output = new StringBuilder();
		if (html) {
			output.append("");
			output.append(name);
			output.append("<br>	Current Bill Total: $");
			output.append((double)totalCostWeek/100);
		}
		else {
			output.append("");
			output.append(name);
			output.append("\n	Running Cost: $");
			output.append((double)totalCostRunning/100);
			output.append("\n	Current Bill Total: $");
			output.append((double)totalCostWeek/100);
		}
		return output.toString(); //return a string with all of the person's data in it
	}
	public final String getName() // return the name of the person
	{
		/**
		Class Person: Method getData
		Precondition: setData has been run for invoking person
		Post condition: the method will return a string contianing the name.
		*/

		return name;
	}
	public final long getBarCode() // return the barcode assoiated with the person
	{
		 /**
		 Class Person: Method getBarCode
		 Precondition: SetData has been run for the invoking person
		 Psotcondition: The method will return an int containing the barcode of the item. 
		 */
		return barCode;
	}
	public final double totalCostRunning()
	{
		/**
		Class Person: Method totalCostRunning
		Precondition: setData has been run for the invoking person
		Postcondition: this method will return the price of the invoking person.
		*/
		
		return (double)totalCostRunning/100;
	}
	public final double totalCostWeek() // retung the size of the person. 
	{
		/**
		Class Person: Method totalCostRunning
		Precondition: setData has been run for the invoking person
		Postcondition: this method will return the price of the invoking person.
		*/
		
		return (double)totalCostWeek/100;
	}
	public final void addPrice(long cost)
	{
		totalCostRunning += cost;
		totalCostWeek += cost;
	}
	public final void resetWeekCost()
	{
		totalCostWeek = 0;
	}
	public final void setName(String extName)
	{
		name = extName;
	}
	public final boolean canBuy()
	{
		return canBuy;
	}
	public final void setCanBuy(boolean extCanBuy)
	{
		canBuy = extCanBuy;
	}

}