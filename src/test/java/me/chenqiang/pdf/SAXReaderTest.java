package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.template.DocumentFactory;
import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.utils.ResourceContext;
import me.chenqiang.pdf.xml.FontDefinition;
import me.chenqiang.pdf.xml.SimpleLoggingHanlder;
import me.chenqiang.pdf.xml.StyleAttributeFactory;
import me.chenqiang.pdf.xml.node.DocumentNode;

public class SAXReaderTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SAXReaderTest.class);
	@Test
	public void doDocDefTest() throws DocumentException {
		ResourceContext ctx = new ResourceContext();
		StyleAttributeFactory saf = new StyleAttributeFactory(ctx);
		
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new SimpleLoggingHanlder());
		reader.addHandler("/root/font", new FontDefinition(ctx));
		reader.addHandler("/root/document", new DocumentNode(saf, tpl -> this.accept(tpl)));
		reader.read(SAXReaderTest.class.getResourceAsStream("/DocDef.xml"));
	}
	
	public void accept(DocumentTemplate template) {
		
		try {
			File file = File.createTempFile("TEST", ".pdf");
			FileOutputStream fos = new FileOutputStream(file);
			DocumentFactory.produce(template, fos);
			fos.close();
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
	}
}
