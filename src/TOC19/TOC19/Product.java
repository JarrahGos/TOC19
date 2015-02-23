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
* Class: Product
* Description: This program will allow for the input and retreval of products in the product database.
*/

public final class Product implements java.io.Serializable
{

	// create the variables that are needed in order of use
	private String name;
//	private StringBuilder output;
	private long productPrice;
	private long barCode;
	private int numberOfItems;
	private int quantity;
    private static final long serialVersionUID = 165169198;

	public Product(String extName, long extProductPrice, long extBarCode) // construtor which will give the product its values
	{
		//output = new StringBuilder("");
		name = extName;
		productPrice = extProductPrice;
		barCode = extBarCode;
	}
	// Begin methods
	public final void setNumber(int number)
	{
		numberOfItems = number;
	}
	public final int getNumber()
	{
		return numberOfItems;
	}
	public final String getData() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		StringBuilder output = new StringBuilder();
		output.append("\n	Product name: ");
		output.append(name);
		output.append("\n	Bar Code: ");
		output.append(barCode);
		output.append("\n	Price: $");
		output.append((double)productPrice/100);
		output.append("\n	Quantity: ");
		output.append(numberOfItems);

		return output.toString(); //return a string with all of the product's data in it
	}
	public final String getDataScreen() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		StringBuilder output = new StringBuilder();
		output.append("");
		output.append("\n	Product name: ");
		output.append(name);
		output.append(" x ");
		output.append(quantity);
		output.append("\n	Price: $");
		output.append(((double)productPrice/100)*quantity);
		
		return output.toString(); //return a string with all of the product's data in it
	}
        public final String getDataUser() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		StringBuilder output = new StringBuilder();
		output.append(name);
		output.append(" x ");
		output.append(quantity);
		output.append("                                                                 	Price: $");
		output.append(((double)((productPrice*quantity)))/100);
                output.append("\n");
		
		return output.toString(); //return a string with all of the product's data in it
	}
	public final String getDataName()
	{
		StringBuilder output = new StringBuilder();
		output.append(name);
		output.append(" x ");
		return output.toString();
	}
//	public final String getDataPrice()
//	{
//		StringBuilder output = new StringBuilder();
//		output.append("Price: $");
//		output.append(((double)((productPrice*quantity)))/100);
//		output.append("\n");
//		return output.toString();
//	}
    public final double getDataPrice()
    {
        return (((double)((productPrice)))/100);
    }
	public final String getName() // return the name of the product
	{
		/**
		Class Song: Method getData
		Precondition: setData has been run for invoking product
		Post condition: the method will return a string contianing the name.
		*/

		return name;
	}
	public final long getBarCode() // return the barcode assoiated with the product
	{
		 /**
		 Class Song: Method getBarCode
		 Precondition: SetData has been run for the invoking product
		 Psotcondition: The method will return an int containing the barcode of the item. 
		 */
		return barCode;
	}
	public final long productPrice() // retung the size of the product. 
	{
		/**
		Class Song: Method productPrice
		Precondition: setData has been run for the invoking product
		Postcondition: this method will return the price of the invoking product.
		*/
		
		return productPrice;
	}
	public final void decrementNumber()
	{
		numberOfItems--;
	}
	public final void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public final int getQuantity()
	{
		return quantity;
	}
}
