package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlTemplateTest.class);
	@Test
	public void doDocDefTest() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/DocDef.xml");
		File file = File.createTempFile("TEST", ".pdf");
		
		try (FileOutputStream fos = new FileOutputStream(file)) {			
			DocumentEngine.produce(stream, "test", null, null, fos);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
		
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}
	}
	
}
