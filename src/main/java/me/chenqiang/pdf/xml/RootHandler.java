package me.chenqiang.pdf.xml;

import java.util.List;
import java.util.Map;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;
import me.chenqiang.pdf.xml.handler.DocumentHandler;
import me.chenqiang.pdf.xml.handler.resource.DefaultParagraphHandler;
import me.chenqiang.pdf.xml.handler.resource.FontResourceHandler;
import me.chenqiang.pdf.xml.handler.resource.ImageResourceHandler;
import me.chenqiang.pdf.xml.handler.resource.PredefinedStyleHandler;

public class RootHandler implements ElementHandler{
	protected TemplateContext context;
	protected Map<String, DocumentComposer> composerMap;
	protected List<DocumentComposer> composers;
	
	public RootHandler(TemplateContext context, Map<String, DocumentComposer> composerMap,
			List<DocumentComposer> composers) {
		super();
		this.context = context;
		this.composerMap = composerMap;
		this.composers = composers;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		new FontResourceHandler(this.context.getFontreg()).register(elementPath);
		new ImageResourceHandler(this.context::registerImage).register(elementPath);
		new DocumentHandler(this.context, this::doTemplatePostProcess).register(elementPath);
		new DefaultParagraphHandler(this.context).register(elementPath);
		new PredefinedStyleHandler(this.context).register(elementPath);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// Do Nothing.
	}

	protected void doTemplatePostProcess(DocumentComposer tplDoc) {
		this.composers.add(tplDoc);
		if (tplDoc.getId() != null) {
			this.composerMap.put(tplDoc.getId(), tplDoc);
		}
	}
}
