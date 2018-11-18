package org.grobidExample;

import org.grobid.core.data.BiblioItem;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * An example of usage of Grobid.
 *
 * @author Patrice Lopez
 */
public class MyGrobid {
    private static Engine engine;

    public static String runGrobid(String pdfPath) {
        String tei = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("grobid-example.properties"));
            String pGrobidHome = prop.getProperty("grobid_example.pGrobidHome");

            GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));

            GrobidProperties.getInstance(grobidHomeFinder);

            System.out.println(">>>>>>>> GROBID_HOME=" + GrobidProperties.get_GROBID_HOME_PATH());

            engine = GrobidFactory.getInstance().createEngine();

            // Biblio object for the result
            BiblioItem resHeader = new BiblioItem();
            tei = engine.processHeader(pdfPath, 1, resHeader);
        } catch (Exception e) {
            // If an exception is generated, print a stack trace
            e.printStackTrace();
        }
        return tei;
    }

}