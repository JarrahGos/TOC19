package TOC19;

import java.util.Arrays;
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

/*
* Author: Jarrah Gosbell
* Student Number: z5012558
* Class: CheckOut
* Description: This program will allow for the creation, retreval, modification and deletion of checkOuts created from products in the product database.
*/

final class CheckOut
{
	// create the necessary variables in the order of use
	private Product[] products;
	private int[] quantities;
	private int logicalSize;
	private long totalPrice;
    private int first;

    /**
     * Construct a new checkout with no products
     */
	public CheckOut()
	{
		// Initalise the needed variables
	    products = new Product[4];
            quantities = new int[4];
	    logicalSize = 0;
	    totalPrice = 0;
        first = 0;
	}

    /**
     * Add a new product to the checkout
     * @param item The product to be added to the checkout
     */
	public final void addProduct(Product item)
	{
        int quantity = 1; // this can be changed when the user can input a number.
		if(logicalSize == products.length) {
			products = resizeCheckOut(true, products);
			quantities = resizeQuantities(true, quantities);
		}
		int i = 0;
		boolean alreadyExists = false;
		for(Product prod : products) {
			if (prod != null && item.getBarCode() == prod.getBarCode()) {
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
        if(totalPrice >= 20000 && 0 == first) {
            first = 1;
            addProduct(new Product("The high roller has come to town.", 0, 0));
        }
        else if(totalPrice > 110000 && 1 == first) {
            first = 2;
            addProduct(new Product("That's a paycheck", 0, 1651198189));

        }
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

		return output.toString();
	}

    /**
     * Get the names and quantities of everything in the checkout
     * @return A String array of all names and quantities
     */
	public final String[] getCheckOutNames()
	{
		String[] output = new String[logicalSize];
		for (int i = 0; i < logicalSize; i++) {
			output[i] = (products[i].getDataName() + quantities[i] + "\n");
		}
		return output;
	}

    /**
     * Get the prices of all items in the checkout
     * @return A string array of the prices.
     */
    public final String[] getCheckOutPrices()
    {
        String[] output = new String[logicalSize];
        for (int i = 0; i < logicalSize; i++) {
            output[i] = "Price: $" + products[i].getDataPrice() * quantities[i];
        }
        return output;
    }

    /**
     * Get the total price of the checkout
     * @return The summation of the prices for all items.
     */
    public long getPrice()
    {
        return totalPrice;
    }

    /**
     * Delete a product within the checkout.
     * @param productNo The index of the item within the checkout.
     */
    public final void delProduct(int productNo)
	{
		/**
		Class CheckOut: Method delProduct
		Preconditions: productNo has been entered as an integer paremiter
		PostConditions: the product corrisponding to productNo will have been deleted
		*/
		
		if(productNo < logicalSize) { // check that the product exists
            if(quantities[productNo] != 1) {
                totalPrice -= products[productNo].productPrice(); // remove the products data from the summaries of the checkOut
                quantities[productNo]--;
            }
            else {
                totalPrice -= products[productNo].productPrice();
                System.arraycopy(products, productNo + 1, products, productNo, logicalSize - productNo);
                logicalSize--; // should something not be done with quantities here?
            }
		}
		if(logicalSize < products.length/2) { // if needed, lower the size of the database.
			products = resizeCheckOut(false, products);
		}
	}

    /**
     * Alter the size of the checkout up or down
     * @param action Whether to increase (true) or decrease (false) the size of the checkout.
     * @param resizing The checkout to be resized.
     * @return The new resized checkout.
     */
	final Product[] resizeCheckOut(Boolean action, Product[] resizing)
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

    /**
     * Alter the size of the quantities array
     * @param action Whether to increase (true) or decrease (false) the size of the checkout.
     * @param resize The array to be resized
     * @return The resized array
     */
	final int[] resizeQuantities(Boolean action, int[] resize)
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

    /**
     * Partition portion of the quick sort function.
     * @param left The lower end of the array to sort
     * @param right The higher end of the array to sort
     * @return The next pivot point being the right most point.
     */
	final int partitionByName(int left, int right) // used by sort by name
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

    /**
     * The recursive function of the quicksort funciton.
     * @param left The left most part of the array to sort
     * @param right The right most point of the array to sort.
     */
	final void quickSortByName(int left, int right)
	{
		if (left < right) // start the sort.
		{
			int pivot = partitionByName(left, right); // run the first instance of the sort
			quickSortByName(left, pivot-1); // recursively sort smaller sections of the array untill the sections are one element long
			quickSortByName(pivot+1, right); // do the above for the right of the pivot
		}
	}
	final int partitionByPrice(int left, int right) // see the name version of this method
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

	final void quickSortByPrice(int left, int right) // see the name version of this method
	{
		if (left < right)
		{
			int pivot = partitionByPrice(left, right);
			quickSortByPrice(left, pivot-1);
			quickSortByPrice(pivot+1, right);
		}
	}
	final int partitionByBarCode(int left, int right) // see the name version of this method
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

	final void quickSortByBarCode(int left, int right) // see the name version of this method
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

    /**
     * Reduce the stock counts for the purchased products and return the product array to be stored
     * @return The product array, having been reduced in stock. 
     */
	public final Product[] productBought()
	{
		for(int i = logicalSize -1; i > 0; i--) {
			for (int z = 0; z < quantities[i]; z++) {
				products[i].decrementNumber();
			}
		}
		return products;
	}
}
