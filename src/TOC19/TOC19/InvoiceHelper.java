package TOC19;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/*
*    TOC19 is a simple program to run TOC payments within a small group.
*    Copyright (C) 2015 Michael Brock
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
 * @author Michael Brock
 * This class enables static creation of invoice tex files for a specified user and list of transactions.
 * It also gives means to check if LaTeX is installed and if so compile the LaTeX document to a PDF.
 */

public class InvoiceHelper {

	/**
	 * Creates a LaTeX file in the specified directory which is an invoice for the specified person.
	 * filename example: "Michael Brock-8610097.tex"
	 * @param user
	 * @param transactions
	 * @param outputDir
	 * @return
	 */
	public static final int generateInvoiceForUser(Person user, ArrayList<Transaction> transactions, String outputDir) {

		// Auto sorting ftw
		TreeMap<Long, String[]> transactionMap = new TreeMap<>();
		for(Transaction trans : transactions){
			transactionMap.put(trans.getTimestamp().toEpochSecond(ZoneOffset.UTC),trans.toInvoiceString());
		}

		ArrayList transactionLines = new ArrayList<>();
		Set entries = transactionMap.entrySet();
		Iterator it = entries.iterator();

		while (it.hasNext()){
			Map.Entry<Long,String[]> read = (Map.Entry) it.next();

			Map map = new HashMap<>();
			map.put("name", read.getValue()[0]);
			map.put("cost", read.getValue()[1]);
			transactionLines.add(map);
		}


		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		VelocityContext context = new VelocityContext();
		context.put("CustomerName", user.getName());
		context.put("Date", LocalDate.now());
		context.put("transactions", transactionLines);

		Template template = null;

		try {
			template = ve.getTemplate("TOC19/resources/invoiceTemplate.tex");
		} catch (ResourceNotFoundException rnfe) {
			Log.print("Following error is caused by: " + user.getName());
			Log.print(rnfe);
			return 1;
		} catch (ParseErrorException pee) {
			Log.print("Following error is caused by: " + user.getName());
			Log.print(pee);
			return 1;
		} catch (MethodInvocationException mie) {
			Log.print("Following error is caused by: " + user.getName());
			Log.print(mie);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringWriter writer = new StringWriter();

		template.merge(context, writer);

		try{
			File texFile = new File(outputDir + "/" + user.getName() + "-" + user.getBarCode() + ".tex");
			if(texFile.exists()){
				texFile.renameTo(new File(texFile.getAbsolutePath() + "/" + user.getName() + "-" + user.getBarCode() +
						                          "_old.tex"));
			}

			BufferedWriter writeFile = new BufferedWriter(new FileWriter(texFile));
			writeFile.write(writer.toString());
			writeFile.close();

		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}

		return 0;
	}

}