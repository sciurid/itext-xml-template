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

import me.chenqiang.pdf.xml.XMLTemplateLoader;

public class XmlTemplateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlTemplateTest.class);
	@Test
	public void doDocDefTest() throws DocumentException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/DocDef.xml");
		XMLTemplateLoader engine = new XMLTemplateLoader();
		engine.read(stream);
		
		try {
			File file = File.createTempFile("TEST", ".pdf");
			FileOutputStream fos = new FileOutputStream(file);
			DocumentFactory.produce(engine.getComposer("test1"), fos);
			fos.close();
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
	}
	
}
