/*
 * To change this license header, choose License Headers in Project Settings.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TOC19;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

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

/*
 * Author: Jarrah Gosbell
 * Student Number: z5012558
 * Class: PersonDatabase
 * Description: This program will allow for the input and retreval of the person database and will set the limits of the database.
 */

class Settings {
	/**
	 * The properties object which is used to interact with the properties file
	 */
	private final Properties properties = new Properties();
	/**
	 * the path to the properties file which contains the settings
	 */
	private final String propFileName = Compatibility.getFilePath("TOC19.properties");
	/**
	 * an input stream which is used to access the properties file
	 */
	private InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);

	/**
	 * Create an instance of the settings class from which to read settings from.
	 */
	public Settings() {
		if (inputStream != null) return;

		try {
			if (inputStream == null) {
				inputStream = new FileInputStream(String.valueOf(Paths.get(propFileName)));
			}
			if (inputStream == null) {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (FileNotFoundException e) {
			Log.print(e);
		}
	}

	/**
	 * Get the settings for the person database, specifically the location to store the database
	 *
	 * @return The location in which the database is stored. This is checked for compatibility against the running OS
	 * @throws FileNotFoundException if the settings file is not in the location it should be.
	 */
	public final String personSettings() throws FileNotFoundException {
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				Log.print("property file '" + propFileName + "' not found in the classpath");
			}
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		String output;
		output = properties.getProperty("personDatabaseLocation");
		output = Compatibility.getFilePath(output);
		return output;
	}

	/**
	 * Get the settings for the product datasbase, specifically the location to store the database in.
	 *
	 * @return The location in which the database is stored. This is checked for compatibility against the running OS
	 * @throws FileNotFoundException If the settings file is not in the location it should be.
	 */
	public final String productSettings() throws FileNotFoundException {
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				Log.print("property file '" + propFileName + "' not found in the classpath");
			}
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		String output;
		output = properties.getProperty("productDatabaseLocation");
		output = Compatibility.getFilePath(output);
		return output;
	}

	/**
	 * Get the settings for the interface. Specifically the horizontal size, vertical size, (both in pixels) and the text size
	 *
	 * @return A string array with the horizontal size, vertical size and textsize.
	 * @throws FileNotFoundException If the settings file is not in the location it should be.
	 */
	public final String[] interfaceSettings() throws FileNotFoundException {
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				Log.print("property file '" + propFileName + "' not found in the classpath");
			}
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		String[] output = new String[3];
		output[0] = properties.getProperty("horizontalSize");
		output[1] = properties.getProperty("verticalSize");
		output[2] = properties.getProperty("textSize");
		return output;
	}
	/**
	 * Get the settings for the error log. Specifically the location of it's storage
	 * @return A string with the location of the log. This is checked for compatibility against the running OS
	 * @throws FileNotFoundException If the settings file is not in the location it should be.
	 */
	public final String logSettings() throws FileNotFoundException {
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				Log.print("property file '" + propFileName + "' not found in the classpath");
			}
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		String output;
		output = properties.getProperty("logFileLocation");
		output = Compatibility.getFilePath(output);
		return output;
	}
}
