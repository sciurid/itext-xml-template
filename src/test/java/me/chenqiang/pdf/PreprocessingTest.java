package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

public class PreprocessingTest {
	public static class EvaluationHandler implements ElementHandler {
		protected DocumentContext context;

		public EvaluationHandler(DocumentContext context) {
			super();
			this.context = context;
		}

		@Override
		public void onStart(ElementPath elementPath) {
			Element current = elementPath.getCurrent();
			for(Attribute attr : current.attributes()) {
				String originalValue = attr.getValue();
				attr.setValue(context.eval(originalValue));
			}
		}

		@Override
		public void onEnd(ElementPath elementPath) {
			Element current = elementPath.getCurrent();
			Iterator<Node> nodeIterator = current.nodeIterator();
			while(nodeIterator.hasNext()) {
				Node node = nodeIterator.next();
				if(node.getNodeType() == Node.TEXT_NODE) {
					String text = node.getText();
					node.setText(context.eval(text));
				}
			}
		}		
	}
	
	@Test
	public void preprocess() throws DocumentException, IOException {
		DocumentContext ctx = new DocumentContext(null);
		ctx.setParameter("name", "David");
		ctx.setParameter("title", "Professor");
//		ctx.setParameter("children", null);
		
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
