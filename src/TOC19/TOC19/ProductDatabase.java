package TOC19;

/****
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
* Class: ProductDatabase
* Description: This program will allow for the input and retreval of the product database and will set the limits of the database.
*/
import java.io.*;
import java.util.Arrays;

public final class ProductDatabase
{
	private Product[] allProducts;
	private int logicalSize;
	private String databaseLocation;
	private Settings config = new Settings();

	public ProductDatabase()
	{
		allProducts = new Product[10];
		logicalSize = 0;
//		output = "";
		try {
			databaseLocation = config.productSettings();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
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
			logicalSize++; // We have a new product, Now we have something to show for it.
			writeOutDatabaseProduct(newProduct);
		}

	}
	public final String getDatabase(int sort) throws IOException {
		/**
		Class ProductDatabase: Method getDatabase
		Precondition: setDatabase has been run
		Postcondition: the user will be see an output of the products in the database. 
		*/
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
	

	public final String getProduct(int productNo) 
	{
		/**
		Class ProductDatabase: Method getProduct
		Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4
		Postconditions: the user will see the details of their chosen product output.
		*/

		if(productNo < logicalSize) { // check that the product exists
			return allProducts[productNo].getData(); // now that we know that it does, send it to the interface
		}
		else {
		    return "error"; // We cannot find the product that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}
	public final void delProduct(long productNo) throws IOException, InterruptedException {
		/**
		Class ProductDatabase: Method delProduct
		Preconditions: setDatabase has been run, productNo is an integer paremeter
		Postconditions: the chosen product will no longer exist. The success or failure of this will be given by a 0 or 1 returned respectively.
		*/
		File toDel = new File(String.valueOf(productNo));
		Product del = readDatabaseProduct(productNo);
		File toDelLn = new File(del.getName());
		toDel.delete();
		toDelLn.delete();
	}
	public final void delProduct(String name) throws IOException, InterruptedException {
		/**
		 Class ProductDatabase: Method delProduct
		 Preconditions: setDatabase has been run, productNo is an integer paremeter
		 Postconditions: the chosen product will no longer exist. The success or failure of this will be given by a 0 or 1 returned respectively.
		 */
		File toDelLn = new File(name);
		Product del = readDatabaseProduct(name);
		File toDel = new File(String.valueOf(del.getBarCode()));
		toDel.delete();
		toDelLn.delete();
	}
	public final String getProductName(long productNo)
	{
		/**
		Class ProductDatabase: Method getProductName
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the product name will be returned
		*/
		Product getting = readDatabaseProduct(productNo);
		if (getting != null) { // check that the desired person exists
			return getting.getName(); // now that we know it does, give it to the interface
		}
		else {
			return "error"; // nope, the product does not exist. Most likely PICNIC
		}

	}

	public final double getProductPrice(int productNo)
	{
		/** 
		Class ProductDatabase: Method getProductSize
		Preconditions: setDatabase has been run for the invoking product
		Postconditions: the size of the invoking product will be returned as a double
		*/
		Product getting = readDatabaseProduct(productNo);
		if (getting != null) { // check that the desired person exists
			return getting.productPrice(); // now that we know it does, give it to the interface
		}
		else return 0;
	
	}
	public final long getBarCode(int productNo)
	{
		/** 
		Class ProductDatabase: Method getProductBarCode
		Precondition: setDatabase has been run for the invoking product
		Postcondition: the duration of the invoking product will be returned as a double
		*/
		Product getting = readDatabaseProduct(productNo);
		if (getting != null) { // check that the desired person exists
			return getting.getBarCode(); // now that we know it does, give it to the interface
		}
		else {
			return 0;
		}
	
	}
	public final boolean productExists(String extProductName, Long extBarCode)
	{
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(extProductName) || file.getName().equals(extBarCode.toString())) return true;
		}
	    return false; // if you are running this, no product was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}
	public final int writeOutDatabaseProduct(Product productOut) {
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
                System.out.println(e);
                return 1;
            }
            return 0;
        }
	public final void writeOutDatabase(Product[] productsOut) {
		for (Product productOut : productsOut) {
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
                FileOutputStream personOut1 = new FileOutputStream(databaseLocation + productOut.getBarCode()); // do it all a second time for the barcode.
                // it may be quicker to do this with the java.properties setup that I have made. The code for that will sit unused in settings.java.
                ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
                out1.writeObject(productOut);
                out1.close();
                personOut.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	public final int adminWriteOutDatabase(String path) throws IOException {
		PrintWriter outfile = null;
		double total = 0;
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		Product[] database = readDatabase(list);
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created.
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
		outfile.println("Name, Price, Barcode, Stock Count");
		for(Product product : database) {
            if(product != null) {
                outfile.println(product.getName() + "," + product.productPrice() + ","
                        + product.getBarCode() + "," + product.getNumber());
                total += product.productPrice() * product.getNumber();
            }
		}
		outfile.println("Total stock value, " + total);
		outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive
		return 0; // let the program and thus the user know that everything is shiny.
	}
	public final Product readDatabaseProduct(long barcode){
            Product importing = null;
            try {
                FileInputStream productIn = new FileInputStream(databaseLocation + String.valueOf(barcode));
                ObjectInputStream in = new ObjectInputStream(productIn);
                importing = (Product)in.readObject();
                in.close();
                productIn.close();
            }
            catch (IOException e) {
                System.out.println(e);
                return null;
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			 return importing;
        }
    public final Product readDatabaseProduct(String name){
            Product importing = null;
            try {
                FileInputStream productIn = new FileInputStream(databaseLocation + name);
                ObjectInputStream in = new ObjectInputStream(productIn);
                importing = (Product)in.readObject();
                in.close();
                productIn.close();
            }
            catch (IOException e) {
                System.out.println(e);
                return null;
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			 return importing;
        }
	public final Product[] readDatabase(File[] databaseList){
		Product[] importing = new Product[databaseList.length];
        int i = 0;
		for(File product : databaseList) {
            Product inProd = null;
            try {
                FileInputStream productIn = new FileInputStream(product);
                ObjectInputStream in = new ObjectInputStream(productIn);
                inProd = (Product) in.readObject();
                in.close();
                productIn.close();
            } catch (IOException e) {
                System.out.println(e);
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
    public final Product[] readDatabase(String[] databaseList){
        Product[] importing = new Product[databaseList.length];
        int i = 0;
        for(String product : databaseList) {
            Product inProd = null;
            try {
                FileInputStream productIn = new FileInputStream(product);
                ObjectInputStream in = new ObjectInputStream(productIn);
                inProd = (Product) in.readObject();
                in.close();
                productIn.close();
            } catch (IOException e) {
                System.out.println(e);
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
	public final int getNumber(int productNo)
	{
		return allProducts[productNo].getNumber();
	}
	public final void setNumber(int productNo, int number)
	{
		allProducts[productNo].setNumber(number);
	}
	public final Product getProductRef(long productNo)
	{
        try {
            return readDatabaseProduct(productNo); //TODO: if the product does not exist, do something.
        }
        catch (Exception e) {
            return null;
        }
    }
	public final String[] getProductNames() {
        File root = new File (databaseLocation);
        File[] list = root.listFiles();
        String[] stringList = new String[list.length];
        for(int i = 0; i < list.length; i++) {
            stringList[i] = list[i].getPath();
            System.out.println(stringList[i]);
        }
        String[] output = new String[list.length];
        Product[] database = readDatabase(stringList);
        System.out.println(database);
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