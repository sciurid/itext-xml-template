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

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.DocumentFactory;
import me.chenqiang.pdf.utils.ResourceContext;
import me.chenqiang.pdf.xml.SimpleLoggingHanlder;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.node.DocumentHandler;
import me.chenqiang.pdf.xml.node.FontDefinitionNode;

public class SAXReaderTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SAXReaderTest.class);
	@Test
	public void doDocDefTest() throws DocumentException {
		ResourceContext ctx = new ResourceContext();
		AttributeRegistry saf = new AttributeRegistry(ctx);
		
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new SimpleLoggingHanlder());
		reader.addHandler("/root/font", new FontDefinitionNode(ctx));
		reader.addHandler("/root/document", new DocumentHandler(saf, tpl -> this.accept(tpl)));
		reader.read(SAXReaderTest.class.getResourceAsStream("/DocDef.xml"));
	}
	
	public void accept(DocumentComposer template) {
		
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
