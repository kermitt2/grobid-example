package org.grobidExample;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
		String result = example.runGrobid(pdfPath, "header");
		assertNotNull(result);
		
		System.out.println(result);
	}
	
	@Test
	public void testCitationBibTeX() throws Exception {
		String pdfPath = "./src/test/resources/Wang_paperAVE2008.pdf";
		ExampleBibTex example = new ExampleBibTex();
		String result = example.runGrobid(pdfPath, "citation");
		assertNotNull(result);
		
		System.out.println(result);
	}	
}