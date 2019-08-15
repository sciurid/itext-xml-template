package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import me.chenqiang.pdf.xml.preprocessing.EvaluationHandler;

public class PreprocessingTest {
	@Test
	public void preprocess() throws DocumentException, IOException {
		DocumentContext ctx = new DocumentContext(null);
		ctx.setParameter("name", "David");
		ctx.setParameter("title", "Professor");
		ctx.setParameter("children", new String[] {"Alice", "Bob"});
		
		byte [] hawk = this.getClass().getResourceAsStream("/hawk.png").readAllBytes();
		ctx.setParameter("hawk", hawk);
		
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new EvaluationHandler(ctx));
		Document doc = reader.read(this.getClass().getResourceAsStream("/functional-test.xml"));
		
		File file = File.createTempFile("xml-", ".xml");
		System.out.println(file.getAbsolutePath());
		try(FileOutputStream fos = new FileOutputStream(file)) {
			OutputFormat pretty = OutputFormat.createPrettyPrint();
			pretty.setEncoding("utf-8");
			pretty.setOmitEncoding(false);
			pretty.setSuppressDeclaration(false);
			XMLWriter writer = new XMLWriter(fos, pretty);
			writer.write(doc);
			writer.close();
		}
		
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}		
	}
}
