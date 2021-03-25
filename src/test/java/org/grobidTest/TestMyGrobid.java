package org.grobidExample;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

/**
 *  @author Patrice Lopez
 */
public class TestMyGrobid {

	private String testPath = null;

	@Test
	public void testHeader() throws Exception {
		
		String pdfPath = "./src/test/resources/Wang_paperAVE2008.pdf";
		String result = MyGrobid.runGrobid(pdfPath);
		assertNotNull(result);
		
		System.out.println(result);
	}
	
	@Test
	public void testHeaderBibTeX() throws Exception {
		String pdfPath = "./src/test/resources/Wang_paperAVE2008.pdf";
		ExampleBibTex example = new ExampleBibTex();
		String result = example.runGrobid(new File(pdfPath), "header", 0);
		assertNotNull(result);
		
		System.out.println(result);
		example.close();
	}
	
	@Test
	public void testCitationBibTeX() throws Exception {
		String pdfPath = "./src/test/resources/Wang_paperAVE2008.pdf";
		File local = new File(pdfPath);
		ExampleBibTex example = new ExampleBibTex();
		String result = example.runGrobid(new File(local.getAbsolutePath()), "citation", 0);
		assertNotNull(result);
		
		System.out.println(result);
		example.close();
	}	
}