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

/*
* Author: Michael Brock
* Student Number: z5056704
* Class: Compatibility
* Description: This class enables compatibility with Windows based operating systems and will handle all external file access.
*/

public class Compatibility {

	/**
	 * Get whether the host os is windows based
	 * @return Whether the system is based on Windows
	 */
	public static boolean isWindows(){
		return (System.getProperty("os.name").startsWith("Windows"));
	}

	public static String getFilePath(String file){
		return isWindows()? System.getProperty("user.dir") + "\\" + file : file;
	}

}
