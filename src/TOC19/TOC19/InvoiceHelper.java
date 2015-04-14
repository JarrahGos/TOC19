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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * filename axample: "Michael Brock-8610097.tex"
	 * @param user
	 * @param itemLines
	 * @param outputDir
	 * @return
	 */
	public static final int generateInvoiceForUser(Person user, ArrayList<ItemLine> itemLines, String outputDir) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		VelocityContext context = new VelocityContext();
		context.put("CustomerName", user.getName());
		context.put("Date", LocalDate.now());
		context.put("transactions", itemLines);

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
			File texFile = new File(outputDir + user.getName() + "-" + user.getBarCode() + ".tex");
			if(texFile.exists()){
				texFile.renameTo(new File(texFile.getAbsolutePath() + user.getName() + "-" + user.getBarCode() + "_old.tex"));
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

	public static final ArrayList<ItemLine> getItemLinesFromTransactions(ArrayList<Transaction> transactions){

		//First group all transactions by time as they
		HashMap<Long, List<Transaction>> map = new HashMap<Long, List<Transaction>>();
		for(Transaction trans : transactions){
			long key = trans.getTimestamp().toEpochSecond(ZoneOffset.UTC);
			if(map.containsKey(key)){
				map.get(key).add(trans);
			}else {
				List<Transaction> list = new ArrayList<Transaction>();
				list.add(trans);
				map.put(key,list);
			}
		}

		for (Transaction transaction : transactions){
			HashMap<Product, Integer> productMap = new HashMap<>();
			for (int i = 0; i < transaction.getProducts().size(); i++) {
				Product product = transaction.getProducts().get(i);
			}
		}

		ArrayList<ItemLine> lines = new ArrayList<>();



		return lines;
	}
}

/**
 * Used to have a more accessible method of getting data into Velocity
 */
class ItemLine{
	/** Quantiy of items purchased at that time */
	private Integer quantity;
	/** Product purchased */
	private Product product;
	/** Date and Time of purchase */
	private LocalDateTime dateTime;
	/** How the purchase will appear on the invoice */
	private String name;

	public ItemLine(Integer quantity, Product product, LocalDateTime dateTime) {
		this.quantity = quantity;
		this.product = product;
		this.dateTime = dateTime;
		name = dateTime.toString() + " - " + this.product.getName();
	}
}