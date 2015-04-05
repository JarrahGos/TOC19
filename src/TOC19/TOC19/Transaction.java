package TOC19;

/*
*    TOC19 is a simple program to run TOC payments within a small group. 
*    Copyright (C) 2015  Michael Brock
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
 * @author Michael Brock
 *  This class is for convenience in transporting transaction information throughtout the program.
 */


public class Transaction {

    /** The user that made the purchase */
    private Person user;
    /**The products bought in the transaction */
    private Product[] products;
    /** The quantities of the products bought */
    private Integer[] quantities;

    public Transaction(Person person, Product[] productArray, Integer[] amounts) {
        user = person;
        products = productArray;
        quantities = amounts;
    }

    public Product[] getProducts() {
        return products;
    }

    public Person getUser() {
        return user;
    }

    public Integer[] getQuantities() {
        return quantities;
    }
}
