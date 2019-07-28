package me.chenqiang.pdf.xml;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.node.DocumentHandler;
import me.chenqiang.pdf.xml.node.FontDefinitionHandler;
import me.chenqiang.pdf.xml.node.ImageDefinitionHandler;
import me.chenqiang.pdf.xml.node.SimpleLoggingHanlder;

public class XMLTemplateLoader {
	protected TemplateContext context;
	protected Map<String, DocumentComposer> templates;
	protected org.dom4j.Document xml;
	
	public XMLTemplateLoader() {
		this.context = new TemplateContext();
		this.templates = new TreeMap<>();
	}
	
	protected SAXReader init() {
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new SimpleLoggingHanlder());
		reader.addHandler("/root/font", new FontDefinitionHandler(this.context.getResourceRepository()));
		reader.addHandler("/root/image-resource", new ImageDefinitionHandler(this.context.getResourceRepository()));
		reader.addHandler("/root/document", new DocumentHandler(this.context, this::doTemplatePostProcess));
		return reader;
	}

	public void read(File file) throws DocumentException {
		this.xml = this.init().read(file);
	}
	
	public void read(InputStream is) throws DocumentException {
		this.xml = this.init().read(is);
	}
		
	protected void doTemplatePostProcess(String docId, DocumentComposer tplDoc) {
		if(docId == null) {
			return;
		}
		
		this.templates.put(docId, tplDoc);
	}
	
	public DocumentComposer getComposer(String id) {
		return this.templates.get(id);
	}
}
