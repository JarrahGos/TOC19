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

	public final String getDatabase(int sort) throws IOException {
		/**
		 * Class PersonDatabase: Method getDatabase Precondition: setDatabase has been run Postcondition: the user will be see an output of the persons in the database.
		 */
		File root = new File ("./");
		File[] list = root.listFiles();
		Person[] database = readDatabase(list);
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < database.length; i++) { // loop until the all of the databases data has been output
			if (database[i] != null) {
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
		Person del = readDatabasePerson(personNo);
		String command = "rm " + personNo + ".ser && rm " + del.getName() + ".ser";
		Process p;
		p = Runtime.getRuntime().exec(command);
		p.waitFor();
		p.destroy();
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
		String[] output = new String[logicalSize];
		File root = new File ("./");
		File[] list = root.listFiles();
		Person[] database = readDatabase(list);
		for(int i = 0; i < logicalSize; i++) {
			output[i] = database[i].getName();
		}
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
		File root = new File ("./");
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(extPersonName) || file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}
	public final boolean personExists(long extBarCode) {
		File root = new File ("./");
		File[] list = root.listFiles();
		for(File file : list) {
			if(file.getName().equals(String.valueOf(extBarCode))) return true;
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

	public final int writeOutDatabasePerson(Person persOut) {
            try {
                FileOutputStream personOut = new FileOutputStream(persOut.getName());
                ObjectOutputStream out = new ObjectOutputStream(personOut);
                out.writeObject(persOut);
                out.close();
                personOut.close();
                // create a simlink to the person to allow the program to search for either username or barcode
                String command = "ln -s " + persOut.getName() + " " + persOut.getBarCode();
                Process p;
                p = Runtime.getRuntime().exec(command);
                p.waitFor();
                p.destroy();

            }
            catch (Exception e) {
                System.out.println(e);
                return 1;
            }
            return 0;
        }
	public final int adminWriteOutDatabase(String path) throws IOException {
		PrintWriter outfile = null;
		double total = 0;
		File root = new File ("./");
		File[] list = root.listFiles();
		Person[] database = readDatabase(list);
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created.
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
		outfile.println("Barcode, Name, Total, Bill");
		for(Person person : database) {
			outfile.println(person.getBarCode() + "," + person.getName() + ","
					+ person.totalCostRunning() + "," + person.totalCostWeek());
			total += person.totalCostWeek();
		}
		outfile.println("Total, " + total);
		outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive
		return 0; // let the program and thus the user know that everything is shiny.
	}
        public final Person readDatabasePerson(long barcode){
            Person importing = null;
            try {
                FileInputStream personIn = new FileInputStream(String.valueOf(barcode) + ".ser");
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
				 FileInputStream personIn = new FileInputStream(name + ".ser");
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
	public final Person[] readDatabase(File[] databaseList){
		Person[] importing = new Person[databaseList.length];
		for(File person : databaseList) {
			Person inPers = null;
			try {
				FileInputStream personIn = new FileInputStream(person);
				ObjectInputStream in = new ObjectInputStream(personIn);
				inPers = (Person) in.readObject();
				in.close();
				personIn.close();
			} catch (IOException e) {
				System.out.println(e);
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			int i = 0;
			boolean alreadyExists = false;
			for(Person pers : importing) {
				if (inPers.getBarCode() == pers.getBarCode()) {
					alreadyExists = true;
					break;
				}
				else i++;
			}
			if(!alreadyExists)
				importing[i] = inPers;
		}
		return importing;
	}
	public final void addCost(long personNo, long cost) {
		Person adding = readDatabasePerson(personNo);
		adding.addPrice(cost);
		writeOutDatabasePerson(adding);
	}

	public final void resetBills() {
		File root = new File ("./");
		File[] list = root.listFiles();
		Person[] database = readDatabase(list);
		for (Person person : database) {
			person.resetWeekCost();
			writeOutDatabasePerson(person);
		}
	}
	public final void setAdminPassword(String extPassword) {
		admin.setName(extPassword);
		try {
			config.adminSetPassword(extPassword);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
