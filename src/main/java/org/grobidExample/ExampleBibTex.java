package org.grobidExample;

import org.grobid.core.utilities.GrobidPropertyKeys;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.data.BibDataSet;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;

import java.util.Properties;
import java.util.List;
import java.io.FileInputStream;
import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * An example of usage of Grobid. Extract and normalize the header of a PDF file and export it in BibTex. 
 * 
 * @author Patrice Lopez
 *
 */
public class ExampleBibTex {
	private static Engine engine = null;

	public String runGrobid(String pdfPath, String process) {
		String bibtex = "";
		try {
			// context variable are read from the project property file grobid-example.properties
			Properties prop = new Properties();
			prop.load(new FileInputStream("grobid-example.properties"));
			String pGrobidHome = prop.getProperty("grobid_example.pGrobidHome");
			String pGrobidProperties = prop.getProperty("grobid_example.pGrobidProperties");

			MockContext.setInitialContext(pGrobidHome, pGrobidProperties);		
			GrobidProperties.getInstance();

			System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());
		
			engine = GrobidFactory.getInstance().createEngine();
		
			if (process.equals("header")) {
				// Biblio object for the result
				BiblioItem resHeader = new BiblioItem();
				engine.processHeader(pdfPath, false, resHeader);
				bibtex = resHeader.toBibTeX();
			}
			else if (process.equals("citation")) {
				List<BibDataSet> citations = engine.processReferences(new File(pdfPath), false);
				for(BibDataSet bib : citations) {
					if (bib.getResBib() != null)
						bibtex += bib.getResBib().toBibTeX();
				} 
			}
			else {
				System.err.println("Unknown selected process: " + process);
				System.err.println("Usage: command process[header,citation] path_to_pdf");	
			}
		} 
		catch (Exception e) {
			// If an exception is generated, print a stack trace
			e.printStackTrace();
		} 
		finally {
			try {
				MockContext.destroyInitialContext();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bibtex;
	}
	
	/**
     *	
     */
    public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("usage: command process[header|citation] path-to-pdf-file path-to-bib-file");
			return;
		}
	
		String process = args[0];
		
		if (!process.equals("citation") && !process.equals("header")) {
			System.err.println("unknown process: " + process); 
			System.err.println("usage: command process[header|citation] path-to-pdf-file path-to-bib-file");
			return;
		}
		
		String pdfPath = args[1];
		String bibPath = args[2];
		
		File bibFile = new File(bibPath);
		System.out.println(process + " " + pdfPath + " " + bibPath);
		try {
			ExampleBibTex example = new ExampleBibTex();
			String result = example.runGrobid(pdfPath, process);
			FileUtils.writeStringToFile(bibFile, result, "UTF-8");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}