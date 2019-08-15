package me.chenqiang.pdf.sax.handler;

import java.util.List;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.handler.resource.DefaultParagraphHandler;
import me.chenqiang.pdf.sax.handler.resource.FontResourceHandler;
import me.chenqiang.pdf.sax.handler.resource.ImageResourceHandler;
import me.chenqiang.pdf.sax.handler.resource.PredefinedStyleHandler;

public class RootHandler implements ElementHandler{
	protected TemplateContext context;
	protected List<DocumentComposer> composers;
	
	public RootHandler(TemplateContext context,  List<DocumentComposer> composers) {
		this.context = context;
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
	}
}
