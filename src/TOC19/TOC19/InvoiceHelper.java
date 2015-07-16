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
import java.nio.file.Files;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
	 * @param transactionsIn
	 * @param outputDir
	 * @return
	 */
	public static final int generateInvoiceForUser(Person user, ArrayList<Transaction> transactionsIn, String
			outputDir, boolean shouldCreatePdf) {


		// Auto sorting ftw
		TreeMap<Long, Transaction> transactionMap = new TreeMap<>();
		for(Transaction trans : transactionsIn){
			transactionMap.put(trans.getTimestamp().toEpochSecond(ZoneOffset.UTC),trans);
		}

		Set entries = transactionMap.entrySet();
		Iterator it = entries.iterator();
		double total = 0D;

		ArrayList transactions = new ArrayList();
		while (it.hasNext()){
			Map transaction = new HashMap<>();
			//So much unchecked casting however I only just created the array so..... It should be fine.
			Transaction trans = (Transaction)((Map.Entry) it.next()).getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			transaction.put("date", trans.getTimestamp().format(formatter));
			transaction.put("total", trans.getTotalCost());
			ArrayList products = new ArrayList();
			for (int i = 0; i < trans.getProducts().size(); i++){
				Map product = new HashMap<>();
				product.put("item", trans.getProducts().get(i).getName());
				product.put("quantity", trans.getQuantities()[i]);
				product.put("price", "\\$" + trans.getProducts().get(i).getDataPrice());
				product.put("total", trans.getProducts().get(i).getDataPrice() * trans.getQuantities()[i]);
				total += trans.getProducts().get(i).getDataPrice() * trans.getQuantities()[i];
				products.add(product);
			}
			transaction.put("products", products);
			transactions.add(transaction);
		}

		VelocityContext context = new VelocityContext();
		context.put("name", user.getName());
		context.put("pmkeys", user.getBarCode());
		context.put("startDate", "startingDate");
		context.put("endDate", "endingDate");
		context.put("transactions", transactions);
		context.put("total", total);

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();


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
			File texFile = new File("invoice/" + user.getName() + "-" + user.getBarCode() + ".tex");
			if(texFile.exists()){
				texFile.renameTo(new File("invoice/" + user.getName() + "-" + user.getBarCode() + "_old.tex"));
			}

			BufferedWriter writeFile = new BufferedWriter(new FileWriter(texFile));
			writeFile.write(writer.toString());
			writeFile.close();

			if (shouldCreatePdf) {
				File pdfFile = createPdfFromLatex(texFile);
				System.out.println("pdfFile = " + pdfFile);
				Files.move(new File("invoice/" + pdfFile.getName()).toPath(), new File(outputDir + "\\" + pdfFile.getName()).toPath());
			} else {
				Files.copy(texFile.toPath(), new File(outputDir + "\\" + texFile.getName()
				).toPath());
			}

			// now clean up after ourselves
			File[] remains = new File("invoice").listFiles();
			for (File rawFile : remains) {
				rawFile.delete();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}

		return 0;
	}

	public static File createPdfFromLatex(File latexFile) {
		File output = null;
		String command = "pdflatex " + "\"" + latexFile.getPath() + "\"";
		try {

			ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "\"" + latexFile.getName() + "\"");
			processBuilder.directory(new File("invoice"));
			System.out.println(processBuilder.directory());
			Process process = processBuilder.start();

			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");

			errorGobbler.start();
			outputGobbler.start();

			int exitVal = process.waitFor();

			output = new File(latexFile.getName().replace(".tex", ".pdf"));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static boolean canCreatePDF(){
		String command = "pdflatex -v";
		StringBuilder out = new StringBuilder();
		try {
			Process child = Runtime.getRuntime().exec(command);
			InputStream in = child.getInputStream();
			 int ret;
			while ((ret = in.read()) != -1){
				//System.out.print((char) ret);
				out.append((char)ret);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString().contains("Copyright");
	}

}

class StreamGobbler extends Thread {

	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) { System.out.println(type + ">" + line); }
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}