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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public final class PersonDatabase {

	private Person[] allPersons;
	private Person admin;
	private int logicalSize;
//	private String output;
//	private File file;
//	private PrintWriter outfile;
	private Scanner readOutFile;

	public PersonDatabase() {
		allPersons = new Person[45];
		logicalSize = 0;
//		output = "";
	}

	public final int setDatabasePerson(int personNo, String name, long running, long week, long barCode, boolean canBuy) // take the persons data and pass it to the persons constructor
	{
		/**
		 * Class PersonDatabase: Method setDatabase Precondition: augments int personNo, String name, String artist, double size, double duration are input Postcondition: Data for the currant working
		 * person in this database will be set.
		 */
		int test = 0; // using test because something keeps changing my i...
		if (!personExists(name, barCode)) { // check whether the person already exists
			allPersons[logicalSize] = new Person(name, barCode, running, week, canBuy); // pass off the work to the constructor: "make it so."
			logicalSize++; // We have a new person, Now we have something to show for it.
			test = 1;
		}
		if (logicalSize >= allPersons.length) { // of the database is getting to big for it's array, something has to budge
			allPersons = resizeDatabase(true, allPersons); // This will make the array budge, I can't leave that to the user, they never do.
		}
		if (test == 1) {
			return 0; // tell the program that everyithng went well so that it can inform the user
		} else {
			return 1; // Something went wrong, probably that user trying to add two persons with the same name and artist. Now I have to tell them off.
		}
	}

	public final String getDatabase(int sort) {
		/**
		 * Class PersonDatabase: Method getDatabase Precondition: setDatabase has been run Postcondition: the user will be see an output of the persons in the database.
		 */

		this.sortBy(sort); // find the sorting method that they asked for and use it to sort the database.
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < logicalSize; i++) { // loop until the all of the databases data has been output
			if (allPersons[i] != null) {
				output.append(String.format("\nPerson %d:\n", 1 + i));
				output.append(allPersons[i].getData());
			}
		}
		return output.toString(); // send the calling program one large string containing the ingredients of all the persons in the database
	}

	public final String personsUnder(long price, int sort) {
		/**
		 * Class PersonDatabase: Method personsUnder Precondition: setDatabase has been run, paremeters time and sort type have been passed as double and int respectively Postcondition: the user will
		 * be given a list of all of the persons under the specified time in the order requested.
		 */

		int test = 1; // check whether any of the persons matched the users search.
		StringBuilder output = new StringBuilder();
		this.sortBy(sort); // Find the requested sorting method and call it. Much shorter this way, same code, implemented once. 
		for (int i = 0; i < logicalSize; i++) { // loop untill all persons have been tested.
			if (allPersons[i] != null && allPersons[i].getBarCode() < price) { // test whether each person is under the specified size
				test = 0;
				output.append(allPersons[i].getData()); // output the data of the recently tested person
			}
		}
		if (test == 1) {
			output.append("No persons match your search"); // output on a search that gives no persons
		}
		return output.toString(); // pass the results back to the interface
	}

	public final String getPerson(int personNo) {
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their chosen
		 * person output.
		 */

		if (personNo < logicalSize) { // check that the person exists
			return allPersons[personNo].getData(); // now that we know that it does, send it to the interface
		} else {
			return "the person that you have identified does not exist"; // We cannot find the person that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}

	public final String getPersonUser(int personNo, boolean html) {
		/**
		 * Class PersonDatabase: Method getPerson Preconditions: setDatabase has been run, paremeter is an interger between from 1 to 4 Postconditions: the user will see the details of their 
		 *							chosen
		 * person output.
		 */

		if(personNo == -1 || personNo == -2) return "admin";
		else if(personNo < logicalSize) { // check that the person exists
			return allPersons[personNo].getDataUser(html); // now that we know that it does, send it to the interface
		} else {
			return "the person that you have identified does not exist"; // We cannot find the person that you asked for, so we will give you this instead. Probably a PEBKAC anyway.
			//PEBKAC: It is possible to commit no errors and still lose. That is not a weakness. That is life. --CAPTAIN PICARD
		}

	}

	public final int delPerson(int personNo) {
		/**
		 * Class PersonDatabase: Method delPerson Preconditions: setDatabase has been run, personNo is an integer paremeter Postconditions: the chosen person will no longer exist. The success or
		 * failure of this will be given by a 0 or 1 returned respectively.
		 */
		if (personNo < logicalSize) { //check that the person the user doesn't want anymore actually exists in the first place.
			for (int i = personNo; i < logicalSize; i++) { // move all persons back one place. The first move will overwrite the now unwanted person
				allPersons[i] = allPersons[i + 1]; // I really hope that they didn't have any desire to see this one agian. It's already in the bin. 
			}
			--logicalSize; // likewise for the logicalSize of the database.
			if (allPersons.length > 2 * logicalSize) { // if the array for the database is getting a little big, save some memory and shorten it. 
				allPersons = resizeDatabase(false, allPersons);
			}
			return 0; // everything went fine. We will have to tell someone about that. 
		} else {
			return 1; // Telling the user yet again that their person doesn't exist. At least thay don't need to delete it now.
		}
	}

	public final String getPersonName(int personNo) {
		/**
		 * Class PersonDatabase: Method getPersonName Preconditions: setDatabase has been run for the invoking person Postconditions: the person name will be returned
		 */
		if(personNo == -2) {
			return admin.getName(); // returns password{
		}
		else if (personNo < logicalSize) { // check that the desired person exists
			return allPersons[personNo].getName(); // now that we know it does, give it to the interface
		}
		else {
			return "error"; // nope, the person does not exist. Most likely PICNIC
		}

	}

	public final double getPersonPriceYear(int personNo) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		if (personNo < logicalSize) { // to understand its workings, see getPersonName. it works the same but this returns a double
			return allPersons[personNo].totalCostRunning();
		} else {
			return 0;
		}

	}

	public final double getPersonPriceWeek(int personNo) {
		/**
		 * Class PersonDatabase: Method getPersonSize Preconditions: setDatabase has been run for the invoking person Postconditions: the size of the invoking person will be returned as a double
		 */
		if (personNo < logicalSize) { // to understand its workings, see getPersonName. it works the same but this returns a double
			return allPersons[personNo].totalCostWeek();
		} else {
			return 0;
		}

	}

	public final long getBarCode(int personNo) {
		/**
		 * Class PersonDatabase: Method getPersonBarCode Precondition: setDatabase has been run for the invoking person Postcondition: the duration of the invoking person will be returned as a double
		 */
		if (personNo < logicalSize) { // to understand its workings, see getPersonName. it works the same but this returns a double
			return allPersons[personNo].getBarCode();
		} else {
			return 0;
		}

	}

	public final int emptyPerson() {
		/**
		 * Class PersonDatabase: Method emptyPerson Preconditions: none Postconditions: the number of the first found empty person will be returned an as integer or -1 will be returned if there are no
		 * empty persons
		 */
		for (int i = 0; i < allPersons.length; i++) { // keep looping until an empty person has been found
			if (allPersons[i] == null) {
				return i; // pass back the empty person to the caller
			}
		}
		return -1; // there are no empty persons. have a -1 so you know what happened.
	}

	public final boolean personExists(String extPersonName, long extBarCode) {

		for (int i = 0; i < logicalSize; i++) { //loop until a person that matches the artist and name specified
			if (allPersons[i] != null && allPersons[i].getName().equals(extPersonName) && allPersons[i].getBarCode() == extBarCode) {
				return true; // when one is found, send true back to the caller
			}
		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

	public final boolean personExists(long extBarCode) {
		if (extBarCode == 7000000) {
			return true;
		}
		for (int i = 0; i < logicalSize; i++) { //loop until a person that matches the artist and name specified
			if (allPersons[i] != null && allPersons[i].getBarCode() == extBarCode && allPersons[i].canBuy()) {
				return true; // when one is found, send true back to the caller
			}

		}
		return false; // if you are running this, no person was found and therefore it is logical to conclude none exist.
		// similar to Kiri-Kin-Tha's first law of metaphysics.
	}

	public final void resizeDatabase(boolean action) {
		allPersons = resizeDatabase(action, allPersons); // when calling for a resise outside this class, one needs to be able to access the database array to send it. 
		// Thanks to this little trick of signatures, they now have it. 
	}

	public final Person[] resizeDatabase(Boolean action, Person[] resizing) {
		if (action) { // make the database 4 persons bigger so that we have more room. 
			return (Arrays.copyOf(resizing, resizing.length + 4));
		} 
		else if (resizing.length > 4) { // do exactly the same as the last if statement, but make the array half the size that it is now
			return (Arrays.copyOf(resizing, resizing.length/2));
		} 
		else { // if all eles fails, there must be less than 4 persons and the array must be short, make it 4 places long
			return (Arrays.copyOf(resizing, 4));
		}
	}

	public final int partitionByName(int lb, int ub) {
		Person pivotElement = allPersons[lb]; // store the left most value as the pivot element
		int max = logicalSize;
		int left = lb, right = ub; // store left and right as the unchanging values of lb and ub respectively. This is used for the final move of this method
		Person temp; // create a temp for swapping thing around.

		while (left < right) {
			while ((allPersons[left].getName().compareToIgnoreCase(pivotElement.getName()) <= 0) && left + 1 < max) { // test the order of the persons on left side
				left++;
			}
			while ((allPersons[right].getName().compareToIgnoreCase(pivotElement.getName()) > 0) && right - 1 >= 0) { // test the order of the persons on right side
				right--;
			}
			if (left < right) // swap the left and right persons, remember that temp variable, this is where it shines.
			{
				temp = allPersons[left];
				allPersons[left] = allPersons[right];
				allPersons[right] = temp;
			}
		}
		for (left = lb; left <= right && left + 1 < max; left++) { //move the persons one place to the left for all persons after the pivot element
			allPersons[left] = allPersons[left + 1];
		}
		allPersons[right] = pivotElement; // move the pivot element to its place at right, where it is now in the correct order.
		return right; // return the new pivot (see quick sort)
	}

	public final void quickSortByName(int left, int right) {
		if (left < right) // start the sort.
		{
			int pivot = partitionByName(left, right); // run the first instance of the sort
			quickSortByName(left, pivot - 1); // recursively sort smaller sections of the array untill the sections are one element long
			quickSortByName(pivot + 1, right); // do the above for the right of the pivot
		}
	}

	public final int partitionByCost(int lb, int ub) { // for an outline of how this works, see the partition method for name
		Person pivotElement = allPersons[lb];
		int max = logicalSize;
		int left = lb, right = ub;
		Person temp;

		while (left < right) {
			while ((allPersons[left].totalCostWeek() <= pivotElement.totalCostWeek()) && left + 1 < max) {
				left++;
			}
			while ((allPersons[right].totalCostWeek() > pivotElement.totalCostWeek()) && right - 1 >= 0) {
				right--;
			}
			if (left < right) {
				temp = allPersons[left];
				allPersons[left] = allPersons[right];
				allPersons[right] = temp;
			}
		}
		for (left = lb; left <= right && left + 1 < max; left++) {
			allPersons[left] = allPersons[left + 1];
		}
		allPersons[right] = pivotElement;
		return right;
	}

	public final void quickSortByPrice(int left, int right) { // for an outline of how this works, see the sorting method for name
		if (left < right) {
			int pivot = partitionByCost(left, right);
			quickSortByPrice(left, pivot - 1);
			quickSortByPrice(pivot + 1, right);
		}
	}

	public final int partitionByBarCode(int lb, int ub) { // for an outline of how this works, see the partition method for name
		Person pivotElement = allPersons[lb];
		int max = logicalSize;
		int left = lb, right = ub;
		Person temp;

		while (left < right) {
			while ((allPersons[left].getBarCode() <= pivotElement.getBarCode()) && left + 1 < max) {
				left++;
			}
			while ((allPersons[right].getBarCode() > pivotElement.getBarCode()) && right - 1 >= 0) {
				right--;
			}
			if (left < right) {
				temp = allPersons[left];
				allPersons[left] = allPersons[right];
				allPersons[right] = temp;
			}
		}
		for (left = lb; left <= right && left + 1 < max; left++) {
			allPersons[left] = allPersons[left + 1];
		}
		allPersons[right] = pivotElement;
		return right;
	}

	public final void quickSortByBarCode(int left, int right) { // for an outline of how this works, see the sorting method for name
		if (left < right) {
			int pivot = partitionByBarCode(left, right);
			quickSortByBarCode(left, pivot - 1);
			quickSortByBarCode(pivot + 1, right);
		}
	}

	public final int writeOutDatabase(String path) {
		this.quickSortByName(0, logicalSize - 1); // ensure that the database is sorted.
		PrintWriter outfile = null;
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created. 
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null) outfile.close();
			return 1;
		}
		outfile.println("PersonDatabase File"); // print the file header
		outfile.println("---------------------------------------------");
		outfile.println("7000000"); // print out the admin barcode
		outfile.println(admin.getName()); // print out the admin password. // Stored and read as name
		for (int b = 0; b < logicalSize; b++) { // repeatedly print the data of the persons in the database to the file. 
			outfile.println("------------------------------------------");
			outfile.println(allPersons[b].getBarCode());
			outfile.println(allPersons[b].getName());
			outfile.println(allPersons[b].totalCostRunning());
			outfile.println(allPersons[b].totalCostWeek());
			outfile.println(allPersons[b].canBuy());

		}
		outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive 
		return 0; // let the program and thus the user know that everything is shiny. 
	}

	public final int adminWriteOutDatabase(String path) {
		this.quickSortByName(0, logicalSize - 1); // ensure that the database is sorted.
		PrintWriter outfile = null;
		double total = 0;
		try {
			File file = new File(path);
			outfile = new PrintWriter(file); // attempt to open the file that has been created. 
		} catch (FileNotFoundException e) { // if the opening fails, close the file and return 1, telling the program that everything went wrong.
			if (outfile != null)outfile.close();
			return 1;
		}
		outfile.println("PersonDatabase File"); // print the file header
		for (int b = 0; b < logicalSize; b++) { // repeatedly print the data of the persons in the database to the file. 
			outfile.println("------------------------------------------");
			outfile.println("Bar Code: " + allPersons[b].getBarCode());
			outfile.println("Name: " + allPersons[b].getName());
			outfile.println("Total: $" + allPersons[b].totalCostRunning());
			outfile.println("Bill: $" + allPersons[b].totalCostWeek());
			total += allPersons[b].totalCostWeek();

		}
		outfile.println("------------------------------------------");
		outfile.println("Total For this bill is: $" + total);
		outfile.close(); // close the file to ensure that it actually writes out to the file on the hard drive 
		return 0; // let the program and thus the user know that everything is shiny. 
	}

	public final int readDatabase(String path) {
		String tempName, tempInput;
		long tempTotalCostRunning, tempTotalCostWeek;
		double doubleCosts;
		int tempBarCode;
		int count = 0;
		boolean tempCanBuy;
		int z;
		try {
			File file = new File(path); // if this fails, chances are the user hit 2 and imput a file that doesn't exist. 
			readOutFile = new Scanner(file); // create the scanner 
			readOutFile.nextLine(); // exclude the header of the file
			readOutFile.nextLine(); // exclude dashes
			tempInput = readOutFile.nextLine();
			tempBarCode = Integer.parseInt(tempInput);
			tempName = readOutFile.nextLine();
			admin = new Person(tempName, tempBarCode, 0, 0, true);
			for (z = 0; readOutFile.hasNext(); z++) { // until all of the lines have been read, I want to read the lines.
				readOutFile.nextLine(); // someone decided to put a redundant line in each person of the file, this throws it away.
				tempInput = readOutFile.nextLine();
				tempBarCode = Integer.parseInt(tempInput);
				tempName = readOutFile.nextLine();
				doubleCosts = Double.parseDouble(readOutFile.nextLine());
				tempTotalCostRunning = (long)(doubleCosts*100);
				doubleCosts = Double.parseDouble(readOutFile.nextLine());
				tempTotalCostWeek = (long)(doubleCosts*100);
				tempInput = readOutFile.nextLine();
				tempCanBuy = Boolean.parseBoolean(tempInput);
				count += this.setDatabasePerson(z, tempName, tempTotalCostRunning, tempTotalCostWeek, tempBarCode, tempCanBuy); // send the big pile of lines that we just read to the person constructor. 
			}
			readOutFile.close(); // clean up by closing the file
			return z - count; // tell the program how many persons we just got. If it's more than a thousand, I hope the sort doesn't take too long. 
		} catch (FileNotFoundException e) {
			readOutFile.close(); // Well, if something goes wrong, someone should find out. 
			return -1; // this is what we use to tell them that something we didn't expect happened. Like the user assuring me that the file exists.
		}
	}

	public final void sortBy(int sort) {
		switch (sort) { // Rather than place this switch every time the sort is used, Call this.
			case (1):
				this.quickSortByName(0, logicalSize - 1); // when sort is one: call sort by name
				break;
			case (2):
				this.quickSortByPrice(0, logicalSize - 1); // when sort is Three: call sort by size
				break;
			case (3):
				this.quickSortByBarCode(0, logicalSize - 1); // when sort is Four: call sort by duration
				break;
			default:
				this.quickSortByName(0, logicalSize - 1); // when the user imputs the wrong value or there is another issue, default to sort by name.
				break;
		}
	}

	public final int findPerson(long barCode) {
		if (7000000 == barCode) {
			return -2;
		}
		for (int i = logicalSize -1; i > 0; i--) {
			if (allPersons[i].getBarCode() == barCode) {
				return i;
			}
		}
		return -1;
	}

	public final void addCost(int personNo, long cost) {
		allPersons[personNo].addPrice(cost);
	}

	public final void resetBills() {
		for (int i = logicalSize -1; i > 0; i--) {
			allPersons[i].resetWeekCost();
		}
	}
	public final void setAdminPassword(String extPassword) {
		admin.setName(extPassword);
	}
}