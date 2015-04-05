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
 * This program will allow for the input and retrieval of persons in the person database.
 */


import java.io.*;
import java.util.*;

public class TransactionDatabase {

    private String databaseLocation;
    private boolean enabled;


    public TransactionDatabase() {
        try {
            Settings config = new Settings();
            String[] settings = config.transactionSettings();
            databaseLocation = settings[0];
            enabled = settings[1].equals("true");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Records a transaction in the transaction database (.csv)
     * <p>
     * Format is - foodName: quantity: unitPrice
     *
     * @param transaction The transaction object to be stored
     * @return 0 if successful, 1 if an error occurred                    
     */
    public final int addTransactionToDatabase(Transaction transaction) {
        try {
            File db = new File(databaseLocation + transaction.getUser().getBarCode() + ".csv");

            if (!db.exists()) { //if it doesnt exist, lets reinvent the wheel :)
                db.getParentFile().mkdirs();
                db.createNewFile();
            }
            Date date = new Date();
            String time = date.toString();
            
            StringBuilder transactionString = new StringBuilder();
            transactionString.append(time + ",");
            
            Product[] products = transaction.getProducts();
            Integer[] quantities = transaction.getQuantities();
            
            
            //Now write the data to the DB
            FileOutputStream out = new FileOutputStream(databaseLocation + transaction.getUser().getBarCode() + ".csv", true);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bout, "UTF-8"));

            for (int i = 0; i < products.length; i++) {
                transactionString.append(time + ":" + products[i].getName() + ":" + quantities[i] + ":" + products[i].productPrice() / 100D + ",");
            }
            
            writer.write(transactionString.toString() + '\n');
            
            //Flush and close everything we created, mimicked a slower fs and needed to flush and close all otherwise could run into issues
            writer.flush();
            writer.close();
            bout.flush();
            bout.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * Not yet sure what to return for this, it will enable the reading of the database for display to admin
     */
    public final void readTransactions() {

    }
}
