package org.grobidTest;

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
	public void testHeaderHeader() throws Exception {
		String pdfPath = "./src/test/resources/Wang paperAVE2008.pdf";
		String result = MyGrobid.runGrobid(pdfPath);
		assertNotNull(result);
		
		System.out.println(result);
	}
}