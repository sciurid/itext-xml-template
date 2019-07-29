package me.chenqiang.pdf.xml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;
import me.chenqiang.pdf.xml.handler.DocumentHandler;
import me.chenqiang.pdf.xml.handler.SimpleLoggingHanlder;
import me.chenqiang.pdf.xml.handler.resource.FontResourceHandler;
import me.chenqiang.pdf.xml.handler.resource.ImageResourceHandler;

public class XmlTemplateLoader {
	protected TemplateContext context;
	protected Map<String, DocumentComposer> composerMap;
	protected List<DocumentComposer> composers;
	protected org.dom4j.Document xml;

	public XmlTemplateLoader() {
		this.context = new TemplateContext();
		this.composerMap = new TreeMap<>();
		this.composers = new ArrayList<>();
	}

	protected SAXReader init() {
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new SimpleLoggingHanlder());
		reader.addHandler("/root/font", new FontResourceHandler(this.context.getResourceRepository()));
		reader.addHandler("/root/image-resource", new ImageResourceHandler(this.context.getResourceRepository()));
		
		reader.addHandler("/root/document", new DocumentHandler(this.context, this::doTemplatePostProcess));
		return reader;
	}

	public void load(File file) throws DocumentException {
		this.xml = this.init().read(file);
	}

	public void load(InputStream is) throws DocumentException {
		this.xml = this.init().read(is);
	}

	protected void doTemplatePostProcess(String docId, DocumentComposer tplDoc) {
		this.composers.add(tplDoc);
		if (docId != null) {
			this.composerMap.put(docId, tplDoc);
		}
	}

	public DocumentComposer getDocumentComposer(String id) {
		return this.composerMap.get(id);
	}

	public List<DocumentComposer> getDocumentComposers() {
		return this.composers;
	}
}
