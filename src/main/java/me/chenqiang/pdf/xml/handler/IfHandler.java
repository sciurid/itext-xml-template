package me.chenqiang.pdf.xml.handler;

import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.IfComposer;
import me.chenqiang.pdf.xml.context.AttributeNames;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class IfHandler implements ElementHandler{
	protected TemplateContext context;
	protected Consumer<IfComposer> parentAppender;
	protected IfComposer conditional;
	
	public IfHandler(TemplateContext context, Consumer<IfComposer> parentAppender) {
		this.context = context;
		this.parentAppender = parentAppender;
	}
		
	protected void registerSubHandlers(ElementPath elementPath) {
		new ParagraphHandler(this.context, this.conditional).register(elementPath);
		new DivHandler(this.context, this.conditional).register(elementPath);
		new TextHandler(this.context, this.conditional).register(elementPath);
		new BarcodeHandler(this.context, this.conditional).register(elementPath);
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		String testName = current.attributeValue(AttributeNames.TEST);
		
		this.conditional = new IfComposer(testName);
		this.registerSubHandlers(elementPath);
	}
	
	@Override
	public void onEnd(ElementPath elementPath) {
		this.parentAppender.accept(this.conditional);
	}	
	
	public void register(ElementPath elementPath) {
		elementPath.addHandler("if", this);
	}
}
