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
*/
import java.io.*;
import java.util.Arrays;

final class ProductDatabase
{
    /**
     * Stores the path of the database as a string, based on the OS being run.
     */
	private String databaseLocation;

    /**
     * Constructor for ProductDatabase.
     * Will create a Person database with the ability to read and write people to the database location given in the preferences file of Settings
     */
	public ProductDatabase()
	{
		try {
            Settings config = new Settings();
			databaseLocation = config.productSettings();
		} catch (FileNotFoundException e) {
			Log.print(e);
		}
	}

    /**
     * Set a new product within the database.
     * Precondition: augments int productNo, String name, String artist, double size, double duration are input
     * Postcondition: Data for the currant working product in this database will be set.
     * @param name The name of the new product
     * @param price The price of the new product
     * @param barCode The barcode of the new product
     */
	public final void setDatabaseProduct(String name, long price, long barCode) // take the products data and pass it to the products constructor
	{
		/**
		Class ProductDatabase: Method setDatabase
		Precondition: augments int productNo, String name, String artist, double size, double duration are input
		Postcondition: Data for the currant working product in this database will be set. 
		*/
		Product newProduct;
		if(!productExists(name, barCode)) { // alter this to check whether a file with the name name/barcode exists
			newProduct = new Product(name, price, barCode); // pass off the work to the constructor: "make it so."
			writeOutDatabaseProduct(newProduct);
		}

	}

    /**
     * Alter an existing product within the database
     * Precondition: augments int productNo, String name, String artist, double size, double duration are input
     * Postcondition: Data for the currant working product in this database will be set.
     * @param name The new name of the product.
     * @param oldName The old name of the product
     * @param price The new price of the product
     * @param barCode The new barcode of the product
     * @param oldBarCode The old barcode of the product
     */
    public final void changeDatabaseProduct(String name, String oldName, long price, long barCode, long oldBarCode) // take the products data and pass it to the products constructor
    {
        Product newProduct;
        newProduct = new Product(name, price, barCode); // pass off the work to the constructor: "make it so."
        File check = new File(databaseLocation + oldName);
        if(check.exists()) check.delete();
        check = new File(databaseLocation + oldBarCode);
        if(check.exists()) check.delete();
        check = null;
        writeOutDatabaseProduct(newProduct);

    }
    /**
     * Get the entire database as a string
     * Precondition: setDatabase has been run
     * Postcondition: the user will be see an output of the persons in the database.
     * @return A string containing the entire database
     */
	public final String getDatabase() {

		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		Product[] database = readDatabase(list);
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < database.length; i++) { // loop until the all of the databases data has been output
			if(database[i] != null) {
				output.append(String.format("\nProduct %d:\n",1+i));
				output.append(database[i].getData());
			}
		}
		return output.toString(); // send the calling program one large string containing the ingredients of all the products in the database
	}

    /**
     * Deletes the specified product from the database
     * Preconditions: setDatabase has been run
     * Postconditions: the chosen product will no longer exist.
     * @param name The barcode of the person you wish to delete
     */
	public final void delProduct(String name) {
        try {
            File toDelLn = new File(databaseLocation + name);
            Product del = readDatabaseProduct(name);
            File toDel = new File(databaseLocation + String.valueOf(del.getBarCode()));
            toDel.delete();
            toDelLn.delete();
        }
        catch (NullPointerException e ) {
            Log.print("File " + name + " not found when trying to delete");
        }
	}

    /**
     * Get the price of the specified product
     * Preconditions: setDatabase has been run for the invoking product
     * Postconditions: the price of the invoking product will be returned as a double
     * @param productNo The barcode or name of the product desired as a string
     * @return the price of the product as a double
     */
    public final double getProductPrice(String productNo)
    {
        Product getting = readDatabaseProduct(productNo);
        if (getting != null) { // check that the desired person exists
            return getting.productPrice(); // now that we know it does, give it to the interface
        }
        else return 0;

    }
    // TODO: why is this still a thing.
	public final long getBarCode(int productNo)
	{
		Product getting = readDatabaseProduct(productNo);
		if (getting != null) { // check that the desired person exists
			return getting.getBarCode(); // now that we know it does, give it to the interface
		}
		else {
			return 0;
		}
	
	}

    /**
     * Determine Whether a product Exists given only their barcode
     * @param extBarCode The barcode of the person you wish to check for
     * @param extProductName The name of the product you wish to check for
     * @return A boolean value of whether the product exists or not
     */
	final boolean productExists(String extProductName, Long extBarCode)
	{
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(extProductName) || file.getName().equals(extBarCode.toString())) return true;
		}
	    return false; // if you are running this, no product was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

    /**
     * Write out the given product to the database
     * @param productOut The person you wish to write out
     * @return An integer, 0 meaning correct completion, 1 meaning an exception. Exception will be printed.
     */
	final int writeOutDatabaseProduct(Product productOut) {
        if(productOut.productPrice() == 0) return 1;
            try {
                File check = new File(databaseLocation + productOut.getName());
                if(check.exists()) check.delete();
                check = new File(databaseLocation + productOut.getBarCode());
                if(check.exists()) check.delete();
                check = null;
                FileOutputStream personOut = new FileOutputStream(databaseLocation + productOut.getName());
                ObjectOutputStream out = new ObjectOutputStream(personOut);
                out.writeObject(productOut);
                out.close();
                personOut.close();
                FileOutputStream personOut1 = new FileOutputStream(databaseLocation + productOut.getBarCode());
                ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
                out1.writeObject(productOut);
                out1.close();
                personOut.close();
            }
            catch (Exception e) {
                Log.print(e);
                return 1;
            }
            return 0;
        }

    /**
     * Write out the given products array to the database
     * @param productsOut The person you wish to write out
     */
	public final void writeOutDatabase(Product[] productsOut) {
		for (Product productOut : productsOut) {
            if(productOut.productPrice() == 0) continue;
			try {
                File check = new File(databaseLocation + productOut.getName());
                if(check.exists()) check.delete();
                check = new File(databaseLocation + productOut.getBarCode());
                if(check.exists()) check.delete();
                check = null;
                FileOutputStream personOut = new FileOutputStream(databaseLocation + productOut.getName());
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(personOut));
                out.writeObject(productOut);
                out.flush();
                out.close();
                personOut.close();
                FileOutputStream personOut1 = new FileOutputStream(databaseLocation + productOut.getBarCode()); // do it all a second time for the barcode.
                // it may be quicker to do this with the java.properties setup that I have made. The code for that will sit unused in settings.java.
                ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
                out1.writeObject(productOut);
                out1.close();
                personOut.close();
			} catch (Exception e) {
				Log.print(e);
			}
		}
	}
    /**
     * Write out a CSV version of the database for future import.
     * @param path The path to the directory you wish to output to
     * @return An integer of 1 if the file was not found and 0 if it worked.
     */
	public final int adminWriteOutDatabase(String path)  {
		FileWriter outfile = null;
        BufferedWriter bufOut = null;
		double total = 0;
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		Product[] database = readDatabase(list);
		try {
			File file = new File(path);
			outfile = new FileWriter(file); // attempt to open the file that has been created.
            bufOut = new BufferedWriter(outfile);
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
            Log.print(e);
			if (bufOut != null) {
                try {
                    bufOut.close();
                    outfile.close();
                } catch (IOException e1) {
                    Log.print(e1);
                }
            }
			return 1;
		} catch (IOException e) {
            Log.print(e);
        }
        String out = "Name, Price, Barcode, Stock Count";
        try {
            outfile.write(out, 0, out.length());
            bufOut.newLine();
        } catch (IOException e) {
            Log.print(e);
        }
        for(Product product : database) {
            if(product != null) {
                out = product.getName() + "," + product.productPrice() + ","
                        + product.getBarCode() + "," + product.getNumber();
                try {
                    outfile.write(out, 0, out.length());
                    bufOut.newLine();
                } catch (IOException e) {
                    Log.print(e);
                }
                total += product.productPrice() * product.getNumber();
            }
		}
        out = "Total stock value, " + total;
        try {
            outfile.write(out, 0, out.length());
            outfile.flush();
            bufOut.close();
            outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive
        } catch (IOException e) {
            Log.print(e);
        }
		return 0; // let the program and thus the user know that everything is shiny.
	}
    /**
     * Reads one product from the database.
     * @param barcode The barcode of the product you wish to read
     * @return The person in the database which correlates with the barcode, or null if the person is not found
     */
	final Product readDatabaseProduct(long barcode){
            Product importing = null;
            try {
                FileInputStream productIn = new FileInputStream(databaseLocation + String.valueOf(barcode));
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(productIn));
                importing = (Product)in.readObject();
                in.close();
                productIn.close();
            }
            catch (IOException e) {
                Log.print(e);
                return null;
            } catch (ClassNotFoundException e) {
				Log.print(e);
			}
			 return importing;
        }

    /**
     * Reads one product from the database.
     * @param name The name of the product you wish to read
     * @return The person in the database which correlates with the barcode, or null if the person is not found
     */
    public final Product readDatabaseProduct(String name){
            Product importing = null;
            try {
                FileInputStream productIn = new FileInputStream(databaseLocation + name);
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(productIn));
                importing = (Product)in.readObject();
                in.close();
                productIn.close();
            }
            catch (IOException e) {
                Log.print(e);
                return null;
            } catch (ClassNotFoundException e) {
				Log.print(e);
			}
			 return importing;
        }
    /**
     * Create an array of products from the provided string of paths
     * @param databaseList A File array of files which are to be put into the array
     * @return An array of all products found from the given file array
     */
	final Product[] readDatabase(File[] databaseList){
		Product[] importing = new Product[databaseList.length];
        int i = 0;
		for(File product : databaseList) {
            Product inProd = null;
            try {
                FileInputStream productIn = new FileInputStream(product);
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(productIn));
                inProd = (Product) in.readObject();
                in.close();
                productIn.close();
            } catch (IOException e) {
                Log.print(e);
                return null;
            } catch (ClassNotFoundException e) {
                Log.print(e);
            }
            boolean alreadyExists = false;
            if(inProd != null) {
                for (Product prod : importing) {
                    if (prod != null && inProd.getBarCode() == prod.getBarCode()) {
                        alreadyExists = true;
                        break;
                    }
                }
            }
            else alreadyExists = true;
            if (!alreadyExists) {
                importing[i] = inProd;
                i++;
            }
		}
		return importing;
	}
    /**
     * Create an array of products from the provided string of paths
     * @param databaseList A string array of paths to files which are to be put into the array
     * @return An array of all Products found from the given string
     */
    final Product[] readDatabase(String[] databaseList){
        Product[] importing = new Product[databaseList.length];
        int i = 0;
        for(String product : databaseList) {
            Product inProd = null;
            try {
                FileInputStream productIn = new FileInputStream(product);
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(productIn));
                inProd = (Product) in.readObject();
                in.close();
                productIn.close();
            } catch (IOException e) {
                Log.print(e);
                return null;
            } catch (ClassNotFoundException e) {
                Log.print(e);
            }
            boolean alreadyExists = false;
            if(inProd != null) {
                for (Product prod : importing) {
                    if (prod != null && inProd.getBarCode() == prod.getBarCode()) {
                        alreadyExists = true;
                        break;
                    }
                }
            }
            else alreadyExists = true;
            if (!alreadyExists) {
                importing[i] = inProd;
                i++;
            }
        }
        return importing;
    }

    /**
     * Get the number of a given product left in stock
     * @param productName the name of the product you wish to check
     * @return The number as an int of the product left in stock
     */
	public final int getNumber(String productName)
	{
		Product getting = readDatabaseProduct(productName);
        return getting.getNumber();
	}

    /**
     * Set the number of a specified item you have in stock
     * @param name The name of the item you wish to set
     * @param number The number of that item you now have.
     */
	public final void setNumber(String name, int number)
	{
		Product setting = readDatabaseProduct(name);
        setting.setNumber(number);
        writeOutDatabaseProduct(setting);
	}

    /**
     * Returns the product specified
     * @param productNo the barcode of the product you would like
     * @return The product specified
     */
	public final Product getProductRef(long productNo)
	{
        try {
            return readDatabaseProduct(productNo);
        }
        catch (Exception e) {
            Log.print(e);
            return null;
        }
    }

    /**
     * A list of the names of all products in the database
     * @return A String array of the names of all products in the database.
     */
	public final String[] getProductNames() {
        File root = new File (databaseLocation);
        File[] list = root.listFiles();
        String[] stringList = new String[list.length];
        for(int i = 0; i < list.length; i++) {
            stringList[i] = list[i].getPath();
        }
        String[] output = new String[list.length];
        Product[] database = readDatabase(stringList);
        for(int i = 0; i < database.length; i++) {
            if(database[i] != null)
                output[i] = database[i].getName();
        }
        output = Arrays.stream(output)
                .filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);
        return output;
	}
}