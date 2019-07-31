package me.chenqiang.pdf.xml.handler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.ElementPath;

import com.itextpdf.layout.element.AreaBreak;

import me.chenqiang.pdf.composer.AreaBreakComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class NewPageHandler extends BasicTemplateElementHandler<AreaBreakComposer, AreaBreak>{
	
	public NewPageHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}

	@Override
	protected AreaBreakComposer create(ElementPath elementPath) {
		return AreaBreakComposer.getNextPage();
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super AreaBreak>>> getAttributeMap() {
		return null;
	}
	
	@Override
	public void register(ElementPath path) {
		path.addHandler("newpage", this);
	}

}
