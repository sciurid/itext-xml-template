package me.chenqiang.pdf.sax.handler;

import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.common.attribute.AttributeNames;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.ForEachComposer;

public class ForEachHandler implements ElementHandler{
	protected TemplateContext context;
	protected Consumer<ForEachComposer> parentAppender;
	protected ForEachComposer foreach;
	
	public ForEachHandler(TemplateContext context, Consumer<ForEachComposer> parentAppender) {
		this.context = context;
		this.parentAppender = parentAppender;
	}
		
	protected void registerSubHandlers(ElementPath elementPath) {
		new ParagraphHandler(this.context, this.foreach).register(elementPath);
		new DivHandler(this.context, this.foreach).register(elementPath);
		new TextHandler(this.context, this.foreach).register(elementPath);
		new BarcodeHandler(this.context, this.foreach).register(elementPath);
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		String varName = current.attributeValue(AttributeNames.VAR);
		String itemsName = current.attributeValue(AttributeNames.ITEMS);
		
		this.foreach = new ForEachComposer(itemsName, varName);
		this.registerSubHandlers(elementPath);
	}
	
	@Override
	public void onEnd(ElementPath elementPath) {
		this.parentAppender.accept(this.foreach);
	}	
	
	public void register(ElementPath elementPath) {
		elementPath.addHandler("foreach", this);
	}
}
