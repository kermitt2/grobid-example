package org.grobidExample;

import org.grobid.core.utilities.GrobidPropertyKeys;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.data.BibDataSet;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;

import java.util.*;
import java.io.*;

import org.apache.commons.io.FileUtils;

/**
 * An example of usage of Grobid. Extract and normalize the header of a PDF file and export it in BibTex. 
 * 
 * @author Patrice Lopez
 *
 */
public class ExampleBibTex {
	private static Engine engine = null;

	public ExampleBibTex() {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String runGrobid(File pdfFile, String process) {
		StringBuilder bibtex = new StringBuilder();
		try {
			if (process.equals("header")) {
				// Biblio object for the result
				BiblioItem resHeader = new BiblioItem();
				engine.processHeader(pdfFile.getPath(), false, resHeader);
				bibtex.append(resHeader.toBibTeX());
			}
			else if (process.equals("citation")) {
				List<BibDataSet> citations = engine.processReferences(pdfFile, false);
				for(BibDataSet bib : citations) {
					if (bib.getResBib() != null)
						bibtex.append(bib.getResBib().toBibTeX());
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
		return bibtex.toString();
	}

	public void close() {
		try {
			MockContext.destroyInitialContext();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
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
			System.err.println("usage: command process[header|citation] path-to-pdf-file(s) path-to-bib-file(s)");
			return;
		}

		String pdfPath = args[1];
		String bibPath = args[2];

		System.out.println(process + " " + pdfPath + " " + bibPath);

		File pdfFile = new File(pdfPath);
		File bibFile = new File(bibPath);

		if (!pdfFile.exists()) {
			System.err.println("Path does not exist: " + pdfPath);	
			System.exit(0);
		}

		List<File> filesToProcess = new ArrayList<File>();
		if (pdfFile.isFile()) {
			filesToProcess.add(pdfFile);
		} else if (pdfFile.isDirectory()) {
			if (!bibFile.exists()) {
				System.err.println("Path does not exist: " + bibPath);	
				System.exit(0);
			}

			if (!bibFile.isDirectory()) {
				System.err.println("BibTex path is not a directory: " + bibPath);	
				System.exit(0);
			}

			File[] refFiles = pdfFile.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".pdf") || name.endsWith(".PDF");
                }
            });

            if (refFiles == null) {
                System.err.println("No PDF file to be processed under directory: " + pdfPath);	
				System.exit(0);
            }

            for(int i=0; i<refFiles.length; i++) {
            	filesToProcess.add(refFiles[i]);
            }
		}

		ExampleBibTex example = new ExampleBibTex();
		try {
			for(File fileToProcess : filesToProcess) {
				String result = example.runGrobid(fileToProcess, process);
				if (!bibFile.exists() || bibFile.isFile())
					FileUtils.writeStringToFile(bibFile, result, "UTF-8");
				else {
					File theBibFile = new File(bibFile.getPath() + "/" + 
						fileToProcess.getName().replace(".pdf",".bib").replace(".PDF",".bib"));
					FileUtils.writeStringToFile(theBibFile, result, "UTF-8");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}