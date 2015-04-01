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

final class PersonDatabase {

	/** Stores the admin user for the TOC program. Used for getting the password. */
	private static TOC19.Person admin;
	/** Stores the path of the database as a string, based on the OS being run. */
	private String databaseLocation;

    /**
     * Constructor for PersonDatabase.
     * Will create a Person database with the ability to read and write people to the database location given in the preferences file of Settings
     */
	public PersonDatabase() {
		try {
            Settings config = new Settings();
			databaseLocation = config.personSettings();
			admin = readDatabasePerson(7000000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

    /**
     * Set a new person within the database
     * Precondition: augments int personNo, String name, String artist, double size, double duration are input
     * Postcondition: Data for the currant working person in this database will be set.
     * @param name The name of the new person
     * @param running The running bill of the person
     * @param week The current bill of the person
     * @param barCode The barcode of the person
     * @param canBuy Whether the person can buy or not
     */
	public final void setDatabasePerson(String name, long running, long week, long barCode, boolean canBuy) // take the persons data and pass it to the persons constructor
	{

				Person newPerson;
		if (!personExists(name, barCode)) { // check whether the person already exists
			newPerson = new Person(name, barCode, running, week, canBuy); // pass off the work to the constructor: "make it so."
			writeOutDatabasePerson(newPerson);
		}
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
        String[] stringList = new String[list.length];
        for(int i = 0; i < list.length; i++) {
            stringList[i] = list[i].getPath();
        }
        Person[] database = readDatabase(stringList);
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < database.length; i++) { // loop until the all of the databases data has been output
			if (database[i] != null && database[i].getBarCode() != 7000000) {
				output.append(String.format("\nPerson %d:\n", 1 + i));
				output.append(database[i].getData());
			}
		}

		return output.toString(); // send the calling program one large string containing the ingredients of all the persons in the database
	}

    /**
     * Deletes the specified person from the database
     * Preconditions: setDatabase has been run
     * Postconditions: the chosen person will no longer exist.
     * @param personNo The barcode of the person you wish to delete
     */
    public final void delPerson(String personNo) {
        try {
            File toDelLn = new File(databaseLocation + String.valueOf(personNo));
            Person del = readDatabasePerson(personNo);
            File toDel = new File(databaseLocation + String.valueOf(del.getBarCode()));
            toDel.delete();
            toDelLn.delete();
        }
        catch (NullPointerException e ) {
            System.out.println("fileNotFound");
        }
    }

    /**
     * Get the name of the specified person
     * Preconditions: setDatabase has been run for the invoking person
     * Postconditions: the person name will be returned
     * @param personNo The barcode of the person you wish to get
     * @return The name of the person with the specified barcode as a string or error if the person does not exist.
     */
	public final String getPersonName(long personNo) {
		if(personNo == -2) {
			return admin.getName(); // returns password{
		}
		Person getting = readDatabasePerson(personNo);
		if (getting != null) { // check that the desired person exists
			return getting.getName(); // now that we know it does, give it to the interface
		}
		else {
			return "error"; // nope, the person does not exist. Most likely PICNIC
		}

	}

    /**
     * Get a list of the usernames of those in the database
     * @return A String array of the names of those in the database
     */
	public final String[] getUserNames() {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		String[] stringList = new String[list.length];
		for(int i = 0; i < list.length; i++) {
			stringList[i] = list[i].getPath();
		}
		String[] output = new String[list.length];
		Person[] database = readDatabase(stringList);
		for(int i = 0; i < database.length; i++) {
			if(database[i] != null && database[i].getBarCode() != 7000000)
				output[i] = database[i].getName();
		}
        output = Arrays.stream(output)
                .filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);
        return output;
	}

    /**
     * Determine whether a given person exists
     * @param extPersonName The name of the person you are checking for
     * @param extBarCode The barcode of the person you are checking for
     * @return A boolean value of whether the person exists or not
     */
	final boolean personExists(String extPersonName, long extBarCode) {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(extPersonName) || file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

    /**
     * Determine Whether a person Exists given only their barcode
     * @param extBarCode The barcode of the person you wish to check for
     * @return A boolean value of whether the person exists or not
     */
	public final boolean personExists(long extBarCode) {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

    /**
     * Write out the given person to the database
     * @param persOut The person you wish to write out
     * @return An integer, 0 meaning correct completion, 1 meaning an exception. Stack trace will be printed on error.
     */
	public final int writeOutDatabasePerson(Person persOut) {
            try {
                File check = new File(databaseLocation + persOut.getName());
                if(check.exists()) check.delete();
                check = new File(databaseLocation + persOut.getBarCode());
                if(check.exists()) check.delete();
                check = null;
                if(persOut.getBarCode() != 7000000) {
                    FileOutputStream personOut = new FileOutputStream(databaseLocation + persOut.getName());
                    ObjectOutputStream out = new ObjectOutputStream(personOut);
                    out.writeObject(persOut);
                    out.close();
                    personOut.close();
                }
                // it may be quicker to do this with the java.properties setup that I have made. The code for that will sit unused in settings.java.
				FileOutputStream personOut1 = new FileOutputStream(databaseLocation + persOut.getBarCode());
				ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
				out1.writeObject(persOut);
				out1.close();
				personOut1.close();
			}
            catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
            return 0;
    }

    /**
     * Write out a CSV version of the database for future import.
     * @param path The path to the directory you wish to output to
     * @return An integer of 1 if the file was not found and 0 if it worked.
     */
	public final int adminWriteOutDatabase(String path) { //TODO: Ensure this works as a CSV
		PrintWriter outfile = null;
		double total = 0;
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		String[] stringList = new String[list.length];
		for(int i = 0; i < list.length; i++) {
			stringList[i] = list[i].getPath();
		}
		Person[] database = readDatabase(stringList);
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created.
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
		outfile.println("Barcode, Name, Total, Bill");
		for(Person person : database) {
            if(person != null) {
                outfile.println(person.getBarCode() + "," + person.getName() + ","
                        + person.totalCostRunning() + "," + person.totalCostWeek());
                total += person.totalCostWeek();
            }
		}
		outfile.println("Total, " + total);
		outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive
		return 0; // let the program and thus the user know that everything is shiny.
	}

    /**
     * Reads one person from the database.
     * @param barcode The barcode of the person you wish to read
     * @return The person in the database which correlates with the barcode, or null if the person is not found
     */
        public final Person readDatabasePerson(long barcode){
            Person importing = null;
            try {
                FileInputStream personIn = new FileInputStream(databaseLocation + barcode);
                ObjectInputStream in = new ObjectInputStream(personIn);
                importing = (Person)in.readObject();
                in.close();
                personIn.close();
            }
            catch (IOException e) {
                System.out.println(e);
                return null;
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return importing;
        }

    /**
     * Reads one person from the database
     * @param name The name of the preson you wish to read
     * @return The person in the database which correlates with the name, or null if the person is not found
     */
         public final Person readDatabasePerson(String name) {
			 Person importing = null;
			 try {
				 FileInputStream personIn = new FileInputStream(databaseLocation + name );
				 ObjectInputStream in = new ObjectInputStream(personIn);
				 importing = (Person) in.readObject();
				 in.close();
				 personIn.close();
			 } catch (IOException e) {
				 System.out.println(e);
				 return null;
			 } catch (ClassNotFoundException e) {
				 e.printStackTrace();
			 }
			 return importing;
		 }

    /**
     * Create an array of people from the provided string of paths
     * @param databaseList A string array of paths to files which are to be put into the array
     * @return An array of all people found from the given string
     */
	final Person[] readDatabase(String[] databaseList){
		Person[] importing = new Person[databaseList.length];
        int i = 0;
		for(String person : databaseList) {
			Person inPers = null;
			try {
				FileInputStream personIn = new FileInputStream(person);
				ObjectInputStream in = new ObjectInputStream(personIn);
				inPers = (Person) in.readObject();
				in.close();
				personIn.close();
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
            boolean alreadyExists = false;
			if(inPers != null) {
				for (Person pers : importing) {
					if ( pers != null && inPers.getBarCode() != 7000000 && inPers.getBarCode() == pers.getBarCode()) {
						alreadyExists = true;
						break;
					}
				}
			}
			else alreadyExists = true;
			if(!alreadyExists) {
                importing[i] = inPers;
                i++;
            }
		}
		return importing;
	}

    /**
     * Reset the current bill of the entire database to zero. Will not effect the running bill.
     */
	public final void resetBills() {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		String[] stringList = new String[list.length];
		for(int i = 0; i < list.length; i++) {
			stringList[i] = list[i].getPath();
		}
		Person[] database = readDatabase(stringList);
		for (Person person : database) {
            if(person != null) {
                person.resetWeekCost();
                writeOutDatabasePerson(person);
            }
		}
	}

    /**
     * Changes the Admin password to the one specified
     * @param extPassword The new password, prehashed.
     */
	public final void setAdminPassword(String extPassword) {
		admin.setName(extPassword);
        	writeOutDatabasePerson(admin);
	}

    /**
     * Set weather the specified person can buy from the program
     * @param userName The name (or barcode) of the person you are changing
     * @param canBuy A boolean of whether the person should be able to buy.
     */
	public final void setPersonCanBuy(String userName, boolean canBuy)
	{
		Person set = readDatabasePerson(userName);
		set.setCanBuy(canBuy);
		writeOutDatabasePerson(set);
	}
}
