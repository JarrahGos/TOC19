/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import TOC19.Settings;
import java.io.FileNotFoundException;


public class SQLInterface {
	private String URL = "jdbc:mysql//localhost:3306/toc"; // these will be initialised from the file. 
	private String user = "jarrah"; // when sure it works, remove these. 
	private String password = "password";
	private Connection db = null;  
	private Settings config;
	public SQLInterface(String extuser, String extpassword) throws FileNotFoundException
	{
		String[] settings = config.SQLInterfaceSettings();
		URL = settings[0];
		user = settings[1];
		password = settings[2];
		try {
			db = DriverManager.getConnection(URL, user, password);
		}
		catch (java.sql.SQLException e){
			System.out.println("error connecting to DB, check the settings\n" + e.toString());
		}
	}
	public final String SQLRead(String table,String columnName, String where, String equals) 
	{
		String result = "";
		ResultSet rs = null;
		try {
			PreparedStatement request = db.prepareStatement("SELECT ? FROM ? WHERE ?=?");
			request.setString(1, columnName);
			request.setString(2, table);
			request.setString(3, where);
			request.setString(4, equals);
			rs = request.executeQuery();
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to read database.\n" + e.toString());
		}
		try {
			if(rs != null)
			result = rs.getString(1);
		}
		catch (java.sql.SQLException e) {
			System.out.println("could not read string from result" + e.toString());
		}
		return result;
	}
	public final String[] SQLReadSet(String table, String columnName, String where, String equals)
	{
		String[] result = null;
		ResultSet rs = null;
		try {
			PreparedStatement request = db.prepareStatement("SELECT ? FROM ? WHERE ?=?");
			request.setString(1, columnName);
			request.setString(2, table);
			request.setString(3, where);
			request.setString(4, equals);
			rs = request.executeQuery();
			rs = request.executeQuery();
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to read database.\n" + e.toString());
		}
		try {
			if(rs != null){
				for(int i = 0; rs.next(); i++) {
					result[i] = rs.getString(1);
				}
			}
		}
		catch (java.sql.SQLException e) {
			System.out.println("could not read string from result" + e.toString());
		}
		return result;
	}
	public final void SQLSet(String table, String setWhat, String setTo, String where, String equals)
	{
		try {
			PreparedStatement request = db.prepareStatement("UPDATE ? SET ?=? WHERE ?=?");
			request.setString(1, table);
			request.setString(2, setWhat);
			request.setString(3, setTo);
			request.setString(4, where);
			request.setString(5, equals);
			request.executeQuery();
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to write to database.\n" + e.toString());
		}
	}
	public final void SQLDelete(String table, String where, String equals)
	{
		try {
			PreparedStatement request = db.prepareStatement("UPDATE ? DELETE ? WHERE ?=?");
			request.setString(1, table);
			request.setString(2, where);
			request.setString(3, equals);
			request.executeQuery();
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to write to database.\n" + e.toString());
		}
	}
	public final boolean SQLOutputCSV(String table, String file)
	{
		try {
			Statement request = db.createStatement();
			request.executeQuery("SELECT a,b,a+b INTO OUTFILE '" + file + "'" +   
									"FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"'" +
									"LINES TERMINATED BY '\n'" +
									"FROM " + table);
			return true;
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to write out table  " + table + " to csv.\n" + e.toString());
			return false;
		}
	}
	public final void SQLInsert(String table, String values)
	{
		try {
			PreparedStatement request = db.prepareStatement("INSERT INTO ? VALUES ? ");
			request.setString(1, table);
			request.setString(2, values);
			request.executeQuery();
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to write out your new values to table  " + table + "\n" + e.toString());
		}
	}
	
}
