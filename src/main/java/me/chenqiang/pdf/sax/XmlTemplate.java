package me.chenqiang.pdf.sax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.handler.RootHandler;
import me.chenqiang.pdf.sax.handler.UnhandledLoggingHanlder;

public class XmlTemplate {
	protected TemplateContext context;
	protected List<DocumentComposer> list;
	protected Map<String, DocumentComposer> map;
	protected org.dom4j.Document xml;

	public XmlTemplate() {
		this.list = new ArrayList<>();
	}

	public XmlTemplate(InputStream is) throws DocumentException {
		this.context = new TemplateContext();
		
		List<DocumentComposer> composers = new ArrayList<>();		
		
		SAXReader reader = new SAXReader();
		reader.setDefaultHandler(new UnhandledLoggingHanlder());
		reader.addHandler("/root", new RootHandler(this.context, composers));
		this.xml = reader.read(is);
		
		this.map = Collections.unmodifiableMap(
				composers.stream().filter(comp -> comp.getId() != null)
				.collect(Collectors.toMap(DocumentComposer::getId, Function.identity())));
		this.list = Collections.unmodifiableList(composers);
	}

	public DocumentComposer getComposer(String id) {
		return this.map.get(id);
	}

	public List<DocumentComposer> getComposers() {
		return this.list;
	}

	public Map<String, DocumentComposer> getComposerMap() {
		return this.map;
	}
}
