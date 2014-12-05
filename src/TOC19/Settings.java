/*
 * To change this license header, choose License Headers in Project Settings.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TOC19;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Properties;

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

public class Settings {
	private Properties properties = new Properties();
	private String propFileName = "TOC19.properties";
	private InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	
	public final String[] SQLInterfaceSettings() throws FileNotFoundException
	{
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			}
			catch(IOException e) {
				System.out.print("property file '" + propFileName + "' not found in the classpath");
			}
		} 
		else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		String[] output = new String[3];
		output[0] = properties.getProperty("URL");
		output[1] = properties.getProperty("user");
		output[2] = properties.getProperty("password");
		return output;
	}
	public final String[] adminSettings() throws FileNotFoundException
	{
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			}
			catch(IOException e) {
				System.out.print("property file '" + propFileName + "' not found in the classpath");
			}
		} 
		else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		String[] output = new String[2];
		output[0] = properties.getProperty("adminBarcode");
		output[1] = properties.getProperty("adminPassword");
		return output;
	}
	public final void adminSetPassword(String passwd) throws FileNotFoundException
	{	
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			}
			catch(IOException e) {
				System.out.print("property file '" + propFileName + "' not found in the classpath");
			}
		} 
		else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		FileOutputStream output = null;
		try {
			properties.setProperty("adminPassword", passwd);
			output = new FileOutputStream(propFileName);
			properties.store(output, null);
		}
		catch (IOException e) {
			System.out.print(e);
		}
		if(output != null) {
			try { 
				output.close();
			}
			catch (IOException e) {
				System.out.print(e);
			}
		}
	}
	public final String[] InterfaceSettings() throws FileNotFoundException
	{
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			}
			catch(IOException e) {
				System.out.print("property file '" + propFileName + "' not found in the classpath");
			}
		} 
		else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		String[] output = new String[3];
		output[0] = properties.getProperty("horizontalSize");
		output[1] = properties.getProperty("verticalSize");
		output[2] = properties.getProperty("textSize");
		return output;
	}
}
