package TOC19;

import java.util.Arrays;
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
* Class: CheckOut
* Description: This program will allow for the creation, retreval, modification and deletion of checkOuts created from products in the product database.
*/

public final class CheckOut
{
	// create the necessary variables in the order of use
	private Product[] products;
	private Product[] resized; // used for creating larger databases. 
	private int[] quantities;
//	private String output;
	private int logicalSize;
	private long totalPrice;

	public CheckOut()
	{
		// Initalise the needed variables
//		output = "";
	    products = new Product[4];
            quantities = new int[4];
	    logicalSize = 0;
	    totalPrice = 0;
	}
	    
	public final void addProduct(Product item, int quantity)
	{
		if(logicalSize == products.length) {
			products = resizeCheckOut(true, products);
			quantities = resizeQuantities(true, quantities);
		}
		int i = 0;
		boolean alreadyExists = false;
		for(Product prod : products) {
			if (item.getBarCode() == prod.getBarCode()) {
				alreadyExists = true;
				break;
			}
			else i++;
		}
		if(!alreadyExists) {
			products[logicalSize] = item;
			quantities[logicalSize] = quantity;
			++logicalSize;
		}
		else {
			quantities[i] += quantity;
		}
		totalPrice += item.productPrice()*quantity;
		return 0;
	}
	public final int addProduct(String name, long price, int barCode)
	{
		/** 
		Class CheckOut: Method addProduct
		Preconditions: Paramiters productNo, name, artist, size and duration have been entered as int, string, string, double, double respectively
		Postconditions: The data for the given productNo will have been set.
		*/
		
		if(logicalSize == products.length) { // resize the checkOut when needed. 
			products = resizeCheckOut(true, products);
		} //max time times 60 due to the fact that it is stored in minutes while the product stores time in seconds. 
		// pass the values which are given to the product object.
		products[logicalSize] = new Product(name, price, barCode);
		totalPrice += price;
		++logicalSize; // incerment the logicalSize value to show the added product.
		return 0;
	}
	public final String getCheckOut(int sort) 
	{
		/**
		Class CheckOut: Method getCheckOut
		Preconditions: add product has been run for the invoking checkOut at least for one product.
		Postconditions: the metadata of the products in the invoking checkOut will be returned as a String. If the precondition has not been met nothing will be returned. 
		*/
		//this.sortBy(sort); // sort the database before printing it in the order specified by the user.
		StringBuilder output = new StringBuilder(); //clear the output incase it has values.
		for(int i = 0; i < logicalSize; i++) { // loop untill all products have been output.
			output.append(products[i].getDataUser());
		}
		// Output the summary data of the checkOut
//		if(totalPrice != 0) {
//			output += "\nThe total price is: $";
//			output +=  (double)totalPrice/100; 
//		}
		return output.toString();
	}
	public final String[] getCheckOutNames()
	{
		String[] output = new String[logicalSize];
		for (int i = 0; i < logicalSize; i++) {
			output[i] = (products[i].getDataName());
		}
		return output;
	}
	public final String[] getCheckOutPrices()
	{
		String[] output = new String[logicalSize];
		for (int i = 0; i < logicalSize; i++) {
			output[i] = (products[i].getDataPrice());
		}
		return output;
	}
	public final long getPrice()
	{
		return totalPrice;
	}
	public final void delProduct(int productNo)
	{
		/**
		Class CheckOut: Method delProduct
		Preconditions: productNo has been entered as an integer paremiter
		PostConditions: the product corrisponding to productNo will have been deleted
		*/
		
		if(productNo < logicalSize) { // check that the product exists
			totalPrice -= products[productNo].productPrice(); // remove the products data from the summaries of the checkOut
			for(int i = productNo; i < logicalSize; i++) { // move the products in the database back one such that the deleted product is overwritten.
				products[i] = products[i+1];
			}
			logicalSize--; // should something not be done with quantities here?
		}
		if(logicalSize < products.length/2) { // if needed, lower the size of the database.
			products = resizeCheckOut(false, products);
		}

	}
	public final int emptyProduct() // return the logicalSize to if another class needs it. logicalSize will always point to the next free position.
	{
		/**
		Class CheckOut: Method emptyProduct
		Preconditions: none
		Postconditions: the first found null product will be output, if none are found, 0 will be output as an error.
		*/
		
		return logicalSize;
	}

	public final int productEqualTo(long extBarCode)
	{
		/**
		Class CheckOut: Method productEqualTo
		Preconditions: the string extProduct has been input as a paremiter and contains products data, addProduct has been run atleast once
		PostConditions: the integer number of the product that is equal to extProduct will be returned. On the error that no products match 0 will be returned
		*/
		
		for(int i = 0; i < logicalSize; i++) { //Loop untill the product that matches the one given as a paremeter is found
			if(products[i] != null && products[i].getBarCode() == extBarCode) {
				return i; // return the matching product.
			}
		}
		return -1; // return on unfound match.

	}
	public final Product[] resizeCheckOut(Boolean action, Product[] resizing)
	{
		if(action) { //Make the checkOut bigger
			return (Arrays.copyOf(resizing, resizing.length + 4));
		}
		
		else if(resizing.length/2 > 4) { // make the checkOut smaller if it will not become lower than 4 places long
			return (Arrays.copyOf(resizing, resizing.length/2));
		}
		else { // finally, make the database 4 places long if it cannot be halved without going lower than 4 places.
			return (Arrays.copyOf(resizing, 4));
		}
		
	}
	public final int[] resizeQuantities(Boolean action, int[] resize)
	{
		if(action) {
			return (Arrays.copyOf(resize, resize.length + 4));
		}
		else if(resize.length / 2 > 4) {
			return (Arrays.copyOf(resize, resize.length/2));
		}
		else {
			return (Arrays.copyOf(resize, 4));
		}

	}

	public final int partitionByName(int left, int  right) // used by sort by name
	{
		int max = logicalSize;
		Product pivotElement = products[left]; //Store the left most product as the object that all other objects will be tested against
		int lb = left, ub = right; // store imovable places which are used for the final move.
		Product temp;

		while (left < right)
		{
			while((products[left].getName().compareToIgnoreCase(pivotElement.getName()) <= 0) && left+1 < max) { // test the order of the products on left side
				left++;
			}
			while((products[right].getName().compareToIgnoreCase(pivotElement.getName()) > 0) && right -1 >= 0) { // test the order of the products on the right side
				right--;
			}
			if (left < right) // swap the left and right products
			{
				temp        = products[left];
				products[left]  = products[right];
				products[right] = temp;
			}
		}
		for(left = lb; left <= right && left +1 < max; left++) { //move the products one place to the left for all products after the pivot element
			products[left] = products[left +1];
		}
		products[right] = pivotElement; // move the pivot element to its place at right, where it is now in the correct order.
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
	public final int partitionByPrice(int left, int  right) // see the name version of this method
	{
		Product pivotElement = products[left];
		int max = logicalSize;
		int lb = left, ub = right;
		Product temp;

		while (left < right)
		{
			while((products[left].productPrice() <= pivotElement.productPrice()) && left +1 < max) {
				left++;
			}
			while((products[right].productPrice() > pivotElement.productPrice()) && right -1 >= 0) {
				right--;
			}
			if (left < right)
			{
				temp        = products[left];
				products[left]  = products[right];
				products[right] = temp;
			}
		}
		for(left = lb; left <= right && left+1 < max; left++) {
			products[left] = products[left +1];
		}
		products[right] = pivotElement;
		return right;
	}

	public final void quickSortByPrice(int left, int right) // see the name version of this method
	{
		if (left < right)
		{
			int pivot = partitionByPrice(left, right);
			quickSortByPrice(left, pivot-1);
			quickSortByPrice(pivot+1, right);
		}
	}
	public final int partitionByBarCode(int left, int  right) // see the name version of this method
	{
		Product pivotElement = products[left];
		int max = logicalSize;
		int lb = left, ub = right;
		Product temp;

		while (left < right)
		{
			while((products[left].getBarCode() <= pivotElement.getBarCode()) && left +1 < max) {
				left++;
			}
			while((products[right].getBarCode() > pivotElement.getBarCode()) && right -1 >= 0) {
				right--;
			}
			if (left < right)
			{
				temp        = products[left];
				products[left]  = products[right];
				products[right] = temp;
			}
		}
		for(left = lb; left <= right && left +1 < max; left++) {
			products[left] = products[left +1];
		}
		products[right] = pivotElement;
		return right;
	}

	public final void quickSortByBarCode(int left, int right) // see the name version of this method
	{
		if (left < right)
		{
			int pivot = partitionByBarCode(left, right);
			quickSortByBarCode(left, pivot-1);
			quickSortByBarCode(pivot+1, right);
		}
	}

	public final void sortBy(int sort) // Rather than place this switch every time the sort is used, Call this.
	{
		switch(sort) {
			case (1): this.quickSortByName(0, logicalSize-1); // when sort is one: call sort by name
						break;
			case (2): this.quickSortByPrice(0, logicalSize-1); // when sort is three: call sort by size
						break;
			case (3): this.quickSortByBarCode(0, logicalSize-1); // when sort is four: call sort by duration
						break;
			default: this.quickSortByName(0, logicalSize-1); // when the user imputs the wrong value or there is another issue, default to sort by name.
						break;
		}
	}
	public final Product[] productBought()
	{
		for(int i = logicalSize -1; i > 0; i--) {
			for (int z = 0; z < quantities[i]; z++) {
				products[i].decrementNumber();
			}
		}
		return products;
	}
	public final void addQuantity(int productNo, int add)
	{
		products[productNo].setQuantity(products[productNo].getQuantity() + add);
		totalPrice += products[productNo].productPrice();
	}
}
