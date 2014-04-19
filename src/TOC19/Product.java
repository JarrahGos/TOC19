package TOC19;
//    TOC19 is a simple program to run TOC payments within a small group. 
//    Copyright (C) 2014  Jarrah Gosbell
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
/*
* Author: Jarrah Gosbell
* Student Number: z5012558
* Class: Product
* Description: This program will allow for the input and retreval of products in the product database.
*/

public class Product
{

	// create the variables that are needed in order of use
	private String name;
	private String output;
	private double productPrice;
	private long barCode;
	private int numberOfItems;
	private int quantity;

	public Product(String extName, double extProductPrice, long extBarCode) // construtor which will give the product its values
	{
		output = "";
		name = extName;
		productPrice = extProductPrice;
		barCode = extBarCode;
	}
	// Begin methods
	public void setData(String name, String artist, double productPrice, long barCode) 
	{ // redundant method which has been left in case products are edited in some way which would require this to be used.
		/**
		Class Song: Method setData
		Precondition: Augments String name, String artist, double productPrice, double time are input
		Postcondition: the product that this method was invoked with now has been stored.
		*/
		this.name = name;
		this.productPrice = productPrice;
		this.barCode = barCode;
	}
	public void setNumber(int number)
	{
		numberOfItems = number;
	}
        public void setName(String name)
        {
            this.name = name;
        }
        public void setPrice(double extPrice)
        {
            this.productPrice = extPrice;
        }
	public int getNumber()
	{
		return numberOfItems;
	}
	public String getData() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		
		output = "";
		output += "\n	Product name: ";
		output += name;
		output += "\n	Bar Code: ";
		output += barCode;
		output += "\n	Price: $";
		output += productPrice;
		output += "\n	Quantity: ";
		output += numberOfItems;

		return output; //return a string with all of the product's data in it
	}
	public String getDataScreen() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		
		output = "";
		output += "\n	Product name: ";
		output += name;
		output += " x ";
		output += quantity;
		output += "\n	Price: $";
		output += productPrice*quantity;
		
		return output; //return a string with all of the product's data in it
	}
	public String getName() // return the name of the product
	{
		/**
		Class Song: Method getData
		Precondition: setData has been run for invoking product
		Post condition: the method will return a string contianing the name.
		*/

		return name;
	}
	public long getBarCode() // return the barcode assoiated with the product
	{
		 /**
		 Class Song: Method getBarCode
		 Precondition: SetData has been run for the invoking product
		 Psotcondition: The method will return an int containing the barcode of the item. 
		 */
		return barCode;
	}
	public double productPrice() // retung the size of the product. 
	{
		/**
		Class Song: Method productPrice
		Precondition: setData has been run for the invoking product
		Postcondition: this method will return the price of the invoking product.
		*/
		
		return productPrice;
	}
	public void decrementNumber()
	{
		numberOfItems--;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public int getQuantity()
	{
		return quantity;
	}
}
