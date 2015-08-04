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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionDatabase {

    private static TransactionDatabase instance = new TransactionDatabase();
    private String databaseLocation;
    private boolean enabled;

    public TransactionDatabase() {
        try {
            Settings config = new Settings();
            String[] settings = config.transactionSettings();
            databaseLocation = Compatibility.getFilePath(settings[0]);
            enabled = settings[1].equals("true");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static TransactionDatabase getInstance() {
        return instance;
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
            File db = new File(databaseLocation + transaction.getUser().getBarCode());

            if (!db.exists()) { //if it doesnt exist, lets reinvent the wheel :)
                db.getParentFile().mkdirs();
                db.createNewFile();
            }

            //Now write the data to the DB
            FileOutputStream out = new FileOutputStream(db, true);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bout, "UTF-8"));

            StringBuilder transactionString = new StringBuilder();
            transactionString.append(transaction.getTimestamp() + ",");

            ArrayList<Product> products = transaction.getProducts();
            Integer[] quantities = transaction.getQuantities();

            for (int i = 0; i < products.size(); i++) {
                transactionString.append(products.get(i).getName() + ":" + quantities[i] + ":" + products.get(i).productPrice() + ",");
            }

            writer.write(transactionString.toString() + '\n');

            //close everything we created, mimicked a slower fs and needed to flush and close all sequentially otherwise could run into issues
            writer.close();
            bout.close();
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

        ArrayList<Transaction> transactions = new ArrayList<>();
        File root = new File(databaseLocation);
        File[] list = root.listFiles();
        for (File userDB : list) {
            try {
                BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(userDB)));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Map<Product, Integer> products = new HashMap<>();
                    String[] elements = line.split(",");
                    String dateTimeString = elements[0];

                    for (int i = 1; i < elements.length; i++) {
                        String[] productString = elements[i].split(":");
                        Product product = new Product(productString[0], Long.parseLong(productString[2]), -1);
                        Integer quantity = Integer.parseInt(productString[1]);
                        products.put(product, quantity);
                    }
                    Person user = PersonDatabase.getInstance().readDatabasePerson(Integer.parseInt(userDB.getName()));
                    ArrayList<Product> productsInTrans = (ArrayList<Product>) products.keySet();
                    Integer[] quantities = products.values().toArray(new Integer[]{});
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                    Transaction transaction = new Transaction(user, productsInTrans, quantities, dateTime);
                    transactions.add(transaction);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return transactions;
    }

    public final ArrayList<Transaction> readTransactionDatabase(String barcode) {

        ArrayList<Transaction> transactions = new ArrayList<>();
        File userDB = new File(databaseLocation + "/" + barcode);
        try {
            BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(userDB)));
            String line = "";
            while ((line = reader.readLine()) != null) {
                Map<Product, Integer> products = new HashMap<>();
                String[] elements = line.split(",");
                String dateTimeString = elements[0];

                for (int i = 1; i < elements.length; i++) {
                    String[] productString = elements[i].split(":");
                    Product product = new Product(productString[0], Long.parseLong(productString[2]), 1);
                    Integer quantity = Integer.parseInt(productString[1]);
                    products.put(product, quantity);
                }
                Person user = PersonDatabase.getInstance().readDatabasePerson(Integer.parseInt(userDB.getName()));
                ArrayList<Product> productsInTrans = new ArrayList<>(products.keySet());
                Integer[] quantities = products.values().toArray(new Integer[]{});
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                Transaction transaction = new Transaction(user, productsInTrans, quantities, dateTime);
                transactions.add(transaction);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return transactions;
    }

    public void resetTransactionDatabase() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        File root = new File(databaseLocation);
        File[] list = root.listFiles();
        for (File userDB : list) {
            userDB.delete();
        }
    }
}
