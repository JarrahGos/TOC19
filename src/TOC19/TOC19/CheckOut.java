package TOC19;

import java.util.ArrayList;
import java.util.LinkedList;
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
* Description: This program will allow for the creation, retrieval, modification and deletion of checkOuts created from products in the product database.
*/

final class CheckOut
{
	// create the necessary variables in the order of use
    /** A list of all products which are currently in the checkout */
	private ArrayList<Product> products;
    /** A list corrisponding to products which contains integers, each denoting the number of it's respective product being bought */
	private LinkedList<Integer> quantities;
    /** The size of the above two lists */
	private int logicalSize;
    /** The sum of the prices of the products stored. Calculated as sum(products[i] = quantities[i]) */
	private long totalPrice;

    private int first;

    /**
     * Construct a new checkout with no products
     */
	public CheckOut()
	{
	    products = new ArrayList<>();
        quantities = new LinkedList<>();
	    logicalSize = 0;
	    totalPrice = 0;
        first = 0;
	}

    /**
     * Add a new product to the checkout
     * @param item The product to be added to the checkout
     */
	public final void addProduct(Product item) {
        int quantity = 1; // this can be changed when the user can input a number.
        boolean alreadyExists = false;
        int i = 0;
        found:
        for (Product prod : products) {// replace this with the library method
            if (prod.equals(item)) {
                alreadyExists = true;
                break found;
            } else i++;
        }
		if(!alreadyExists) {
			products.add(item);
			quantities.add(quantity);
			++logicalSize;
		}
		else {
            int quantityStored = quantities.get(i) + quantity;
			quantities.add(i, quantityStored);
            quantities.remove(i+1);
		}
		totalPrice += item.productPrice()*quantity;
        if(totalPrice >= 20000 && 0 == first) {
            first = 1;
            addProduct(new Product("The high roller has come to town.", 0, -3546654));
        }
        else if(totalPrice > 110000 && 1 == first) {
            first = 2;
            addProduct(new Product("That's a paycheck", 0, -1651198189));

        }
	}

    /**
     * Get the names and quantities of everything in the checkout
     * @return A String array of all names and quantities
     */
	public final String[] getCheckOutNames()
	{
		String[] output = new String[logicalSize];
		for (int i = 0; i < logicalSize; i++) {
			output[i] = (products.get(i).getDataName() + quantities.get(i) + "\n");
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
            output[i] = "Price: $" + products.get(i).getDataPrice() * quantities.get(i);
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
    public final void delProduct(int productNo) // array store exception
	{
		/**
		Class CheckOut: Method delProduct
		Preconditions: productNo has been entered as an integer paremiter
		PostConditions: the product corrisponding to productNo will have been deleted
		*/
		
		if(productNo < logicalSize) { // check that the product exists
            if(quantities.get(productNo) != 1) {
                totalPrice -= products.get(productNo).productPrice(); // remove the products data from the summaries of the checkOut
                quantities.add(productNo, quantities.get(productNo) -1);
                quantities.remove(productNo + 1);
            }
            else {
                totalPrice -= products.get(productNo).productPrice();
                products.remove(productNo);
                quantities.remove(productNo);
                logicalSize--;
            }
		}
	}


    /**
     * Reduce the stock counts for the purchased products and return the product array to be stored
     * @return The product array, having been reduced in stock. 
     */
	public final Product[] productBought() //TODO: remove easter eggs before they are written to the database. The below doesn't do it.
    {
        for (int i = logicalSize - 1; i > 0; i--) {
            for (int z = 0; z < quantities.get(i); z++) {
                products.get(i).decrementNumber();
            }
        }
        if (products.contains(new Product("That's a paycheck", 0, -1651198189))) {
            products.remove(products.indexOf(new Product("That's a paycheck", 0, -1651198189)));
            products.remove(products.indexOf(new Product("The high roller has come to town.", 0, -3546654)));
        }
        else if(products.contains(new Product("The high roller has come to town.", 0, -3546654))) {
            products.remove(products.indexOf(new Product("The high roller has come to town.", 0, -3546654)));
        }
        if(products.contains(new Product("Buying yourself are you? You can't do that.", 0, WorkingUser.getLogedInBarcode()))) {
            products.remove(products.indexOf(new Product("Buying yourself are you? You can't do that.", 0, WorkingUser.getLogedInBarcode())));
        }
		return products.toArray(new Product[products.size()]);
	}
}
