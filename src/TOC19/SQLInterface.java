/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TOC19;

/**
 *
 * @author jarrah
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


public class SQLInterface {
	private String URL = "jdbc:mysql//localhost:3306/toc";
	private String user = "jarrah";
	private String password = "password";
	private Connection db = null;  
	public SQLInterface(String extuser, String extpassword) 
	{
		if(extuser != null) {
			user = extuser;
		}
		if(extpassword != null) {
			password = extpassword;
		}
		try {
			db = DriverManager.getConnection(URL, user, password);
		}
		catch (java.sql.SQLException e){
			System.out.println("error connecting to DB, check the settings" + e.toString());
		}
	}
	public final String SQLRead(String table,String columnName, String where) 
	{
		String result = "";
		ResultSet rs = null;
		try {
			Statement request = db.createStatement();
			rs = request.executeQuery("SELECT " + columnName + " FROM " + table + " WHERE " + where);
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
	public final String[] SQLReadSet(String table, String columnName, String where)
	{
		String[] result = null;
		ResultSet rs = null;
		try {
			Statement request = db.createStatement();
			rs = request.executeQuery("SELECT " + columnName + " FROM " + table + " WHERE " + where);
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
	public final void SQLSet(String table, String set, String where)
	{
		try {
			Statement request = db.createStatement();
			request.executeQuery("UPDATE " + table + " SET " + set + " WHERE " + where);
		}
		catch (java.sql.SQLException e) {
			System.out.println("Unable to write to database.\n" + e.toString());
		}
	}
	public final void SQLDelete(String table, String where)
	{
		try {
			Statement request = db.createStatement();
			request.executeQuery("UPDATE " + table + " DELETE " + " WHERE " + where);
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
}
