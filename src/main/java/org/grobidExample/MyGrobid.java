package org.grobidExample;

import org.grobid.core.utilities.GrobidPropertyKeys;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;

import java.util.Properties;
import java.io.FileInputStream;

/**
 * An example of usage of Grobid.
 * 
 * @author Patrice Lopez
 *
 */
public class MyGrobid {
	private static Engine engine;

	public static String runGrobid(String pdfPath) {
		String tei = null;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("grobid-example.properties"));
			String pGrobidHome = prop.getProperty("grobid_example.pGrobidHome");
			String pGrobidProperties = prop.getProperty("grobid_example.pGrobidProperties");

			MockContext.setInitialContext(pGrobidHome, pGrobidProperties);		
			GrobidProperties.getInstance();

			System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());

			engine = GrobidFactory.getInstance().createEngine();

			// Biblio object for the result
			BiblioItem resHeader = new BiblioItem();
			tei = engine.processHeader(pdfPath, false, resHeader);
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
		return tei;
	}
	
}