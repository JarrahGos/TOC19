package TOC19;

/***
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
 * Class: PersonDatabase
 * Description: This program will allow for the input and retreval of the person database and will set the limits of the database.
 */

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public final class PersonDatabase {

//	private static TOC19.Person[] allPersons;
	private static TOC19.Person admin;
	private static int logicalSize;
//	private String output;
//	private File file;
//	private PrintWriter outfile;
	private Scanner readOutFile;
	private Settings config = new Settings();
	private String databaseLocation;

	public PersonDatabase() throws FileNotFoundException {
		logicalSize = 0;
//		output = "";
		String settings = config.adminSettings();
		System.out.println(settings);
	//	for(String string : settings) {
			if (settings != null) System.out.println(settings);
			else System.out.print("null");
	//	}
	//	admin.setBarCode(Long.parseLong(settings[0]));
		admin = new Person(settings, 0, 0, 0, false);
		try {
			databaseLocation = config.personSettings();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public final void setDatabasePerson(String name, long running, long week, long barCode, boolean canBuy) // take the persons data and pass it to the persons constructor
	{
		/**
		 * Class PersonDatabase: Method setDatabase Precondition: augments int personNo, String name, String artist, double size, double duration are input Postcondition: Data for the currant working
		 * person in this database will be set.
		 */
				Person newPerson;
		if (!personExists(name, barCode)) { // check whether the person already exists
			newPerson = new Person(name, barCode, running, week, canBuy); // pass off the work to the constructor: "make it so."
			logicalSize++; // We have a new person, Now we have something to show for it.
			writeOutDatabasePerson(newPerson);
		}
	}

	public final String getDatabase() throws IOException {
		/**
		 * Class PersonDatabase: Method getDatabase Precondition: setDatabase has been run Postcondition: the user will be see an output of the persons in the database.
		 */
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

	public final String getPerson(long personNo) {
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their chosen
		 * person output.
		 */
		Person getting = readDatabasePerson(personNo);
		if (getting != null) { // check that the person exists
			return getting.getData(); // now that we know that it does, send it to the interface
		} else {
			return "the person that you have identified does not exist"; // We cannot find the person that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}

	public final String getPersonUser(long personNo, boolean html) {
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their 
		 *							chosen
		 * person output.
		 */

		if(personNo == -1 || personNo == -2) return "admin";
		Person getting = readDatabasePerson(personNo);
		if (getting != null) { // check that the person exists
			return getting.getDataUser(html);
		} else {
			return "the person that you have identified does not exist"; // We cannot find the person that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}

	public final void delPerson(long personNo) throws IOException, InterruptedException {
		/**
		 * Class PersonDatabase: Method delPerson Preconditions: setDatabase has been run, personNo is an integer paremeter Postconditions: the chosen person will no longer exist. The success or
		 * failure of this will be given by a 0 or 1 returned respectively.
		 */
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

	public final String getPersonName(long personNo) {
		/**
		 * Class PersonDatabase: Method getPersonName Preconditions: setDatabase has been run for the invoking person Postconditions: the person name will be returned
		 */
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
	public final String[] getUserNames() {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		String[] stringList = new String[list.length];
		for(int i = 0; i < list.length; i++) {
			stringList[i] = list[i].getPath();
			System.out.println(stringList[i]);
		}
		String[] output = new String[list.length];
		Person[] database = readDatabase(stringList);
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
	public final double getPersonPriceYear(long personNo) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		Person getting = readDatabasePerson(personNo);
		if (getting != null) { // to understand its workings, see getPersonName. it works the same but this returns a double
			return getting.totalCostRunning();
		} else {
			return 0;
		}

	}

	public final double getPersonPriceWeek(long personNo) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		Person getting = readDatabasePerson(personNo);
		if(getting != null) return getting.totalCostWeek();
		else return 0;
	}

	public final long getBarCode(int personNo) {
		/**
		 * Class PersonDatabase: Method getPersonBarCode Precondition: setDatabase has been run for the invoking person Postcondition: the duration of the invoking person will be returned as a double
		 */
		Person getting = readDatabasePerson(personNo);
		if (getting != null) { // to understand its workings, see getPersonName. it works the same but this returns a double
			return getting.getBarCode();
		} else {
			return 0;
		}

	}
	public final boolean personExists(String extPersonName, long extBarCode) {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(extPersonName) || file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}
	public final boolean personExists(long extBarCode) {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

	public final int writeOutDatabasePerson(Person persOut) { //TODO make the barcode work with this.
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
	public final int adminWriteOutDatabase(String path) throws IOException {
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
        public final Person readDatabasePerson(long barcode){
            Person importing = null;
            try {
                FileInputStream personIn = new FileInputStream(databaseLocation + String.valueOf(barcode) );
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
	public final Person[] readDatabase(String[] databaseList){
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
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
	public final void addCost(long personNo, long cost) {
		Person adding = readDatabasePerson(personNo);
		adding.addPrice(cost);
		writeOutDatabasePerson(adding);
	}

	public final void resetBills() {
		File root = new File (databaseLocation);
		File[] list = root.listFiles();
		String[] stringList = new String[list.length];
		for(int i = 0; i < list.length; i++) {
			stringList[i] = list[i].getPath();
		}
		Person[] database = readDatabase(stringList);
		for (Person person : database) {
			person.resetWeekCost();
			writeOutDatabasePerson(person);
		}
	}
	public final void setAdminPassword(String extPassword) {
		admin.setName(extPassword);
        	writeOutDatabasePerson(admin);
	}

	public final void setPersonCanBuy(long personNumber, boolean canBuy)
	{
		Person set = readDatabasePerson(personNumber);
		set.setCanBuy(canBuy);
		writeOutDatabasePerson(set);
	}
	public final void setPersonCanBuy(String userName, boolean canBuy)
	{
		Person set = readDatabasePerson(userName);
		set.setCanBuy(canBuy);
		writeOutDatabasePerson(set);
	}
}
