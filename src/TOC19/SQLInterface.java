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
	public final String SQLread(String columnName, String table, String where) 
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
}
