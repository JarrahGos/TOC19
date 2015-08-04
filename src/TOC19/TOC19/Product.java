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
 * @author Jarrah Gosbell
 *         This program will allow for the input and retrieval of products in the product database.
 */

public final class Product implements java.io.Serializable {

    // create the variables that are needed in order of use
    /**
     * A version number of this class to avoid java breaking the serialisation storage.
     */
    private static final long serialVersionUID = 165169198;
    /**
     * The name of the product
     */
    private final String name;
    /**
     * The price of the product
     */
    private final long productPrice;
    /**
     * the barcode of the product
     */
    private final long barCode;
    /**
     * The number of the product currently in stock
     */
    private int numberOfItems;

    /**
     * Create a product with the given name, price and barcode
     *
     * @param extName         The name of the new product
     * @param extProductPrice The price of the new product
     * @param extBarCode      The barcode of the new product
     */
    public Product(String extName, long extProductPrice, long extBarCode) {
        name = extName;
        productPrice = extProductPrice;
        barCode = extBarCode;
    }

    /**
     * Get the number of the item you currently have in stock.
     *
     * @return The number of items you have in stock.
     */
    public final int getNumber() {
        return numberOfItems;
    }

    /**
     * Set the number of the product you have in stock
     *
     * @param number The number you currently have in stock.
     */
    public final void setNumber(int number) {
        numberOfItems = number;
    }

    /**
     * Get the human readable string form of the product
     * Procondition: setData has been run for invoking product or the product constructor outlined above has been run
     * Postcondition: The data that has been entered for the invoking product will be returned.
     *
     * @return The human readable string form of the product.
     */
    public final String getData() // output the product data as a string
    {
        StringBuilder output = new StringBuilder();
        output.append("\nProduct name: ");
        output.append(name);
        output.append("\n	Bar Code: ");
        output.append(barCode);
        output.append("\n	Price: $");
        output.append((double) productPrice / 100);
        output.append("\n	Quantity: ");
        output.append(numberOfItems);

        return output.toString(); //return a string with all of the product's data in it
    }

    /**
     * Get the name formatted ready to have the quantity attached to it.
     *
     * @return the name of the item formatted ready to have the quantity appended.
     */
    public final String getDataName() {
        StringBuilder output = new StringBuilder();
        output.append(name);
        output.append(" x ");
        return output.toString();
    }

    /**
     * Get the price of the item as a double.
     *
     * @return The price of the item as a double.
     */
    public final double getDataPrice() {
        return (((double) ((productPrice))) / 100);
    }

    /**
     * Get the name of the item.
     *
     * @return The name of the item.
     */
    public final String getName() // return the name of the product
    {
        return name;
    }

    /**
     * Get the barcode of the item.
     *
     * @return The barcode of the item.
     */
    public final long getBarCode() // return the barcode assisted with the product
    {
        return barCode;
    }

    /**
     * Get the price of the item as a long.
     * This is useful for correct calculations, but not for display.
     *
     * @return The price of the item as a long (multiplied by 100 such that the decimal places are the last two digits).
     */
    public final long productPrice() // return the size of the product.
    {
        return productPrice;
    }

    /**
     * Reduce the number of the item in stock
     */
    public final void decrementNumber() {
        numberOfItems--;
    }

    /**
     * Determine whether a product is equal to this one
     *
     * @param check The product to check
     * @return The boolean of whether the name, barcode and price are the same.
     */
    public boolean equals(Product check) {
        return check.getName().equals(name) && check.getBarCode() == barCode && check.productPrice() == productPrice;
    }

    /**
     * Determine whether a product is roughly equal to this one
     *
     * @param check The product to check
     * @return The boolean of whether the name || barcode are the same.
     */
    public boolean weakEquals(Product check) {
        return check.getName().equals(name) || check.getBarCode() == barCode;
    }

    @Override
    public String toString() {
        return name + "-" + barCode + "-" + getDataPrice();
    }
}
