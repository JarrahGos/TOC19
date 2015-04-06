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
import java.time.LocalDateTime;
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
            StringBuilder transactionString = new StringBuilder();
            transactionString.append(transaction.getTimestamp() + ",");
            
            Product[] products = transaction.getProducts();
            Integer[] quantities = transaction.getQuantities();
            
            
            //Now write the data to the DB
            FileOutputStream out = new FileOutputStream(databaseLocation + transaction.getUser().getBarCode(), true);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bout, "UTF-8"));

            for (int i = 0; i < products.length; i++) {
                transactionString.append(products[i].getName() + ":" + quantities[i] + ":" + products[i].productPrice() + ",");
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
    public final ArrayList<Transaction> readTransactions() {

        //Need a better method than this but works for now
        PersonDatabase personDatabase = new PersonDatabase();
        
        ArrayList<Transaction> transactions = new ArrayList<>();
        File root = new File(databaseLocation);
        File[] list = root.listFiles();
        for(File userDB: list){
            try {
                BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(userDB)));
                String line = "";
                while((line = reader.readLine()) != null){
                    Map<Product, Integer> products = new HashMap<>();
                   
                    String[] elements = line.split(",");

                    for (int i = 1; i < elements.length; i++) {
                        String[] productString = elements[i].split(":");
                        Product product = new Product(productString[0],Long.parseLong(productString[2]),-1);
                        Integer quantity = Integer.parseInt(productString[1]);
                        products.put(product, quantity);
                    }
                    //TL:DR Get rid of this ugly mess of a constructor....... Also work in the timestamp management
                    Transaction transaction = new Transaction(personDatabase.readDatabasePerson(Integer.parseInt(userDB.getName())),products.keySet().toArray(new Product[]{}),products.values().toArray(new Integer[]{}), null);
                    transactions.add(transaction);
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return transactions;
    }
    
 public final ArrayList<Transaction> readTransactionDatabase(String barcode){
     //Need a better method than this but works for now
     PersonDatabase personDatabase = new PersonDatabase();

     ArrayList<Transaction> transactions = new ArrayList<>();
     File userDB = new File(databaseLocation + barcode);
         try {
             BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(userDB)));
             String line = "";
             while((line = reader.readLine()) != null){
                 Map<Product, Integer> products = new HashMap<>();

                 String[] elements = line.split(",");

                 for (int i = 1; i < elements.length; i++) {
                     String[] productString = elements[i].split(":");
                     Product product = new Product(productString[0],Long.parseLong(productString[2]),-1);
                     Integer quantity = Integer.parseInt(productString[1]);
                     products.put(product, quantity);
                 }
                 //TL:DR Get rid of this ugly mess of a constructor....... Also work in the timestamp management
                 Transaction transaction = new Transaction(personDatabase.readDatabasePerson(Integer.parseInt(userDB.getName())),products.keySet().toArray(new Product[]{}),products.values().toArray(new Integer[]{}), null);
                 transactions.add(transaction);
             }

         } catch (IOException e) {
             e.printStackTrace();
         }


     return transactions;
 }
}
