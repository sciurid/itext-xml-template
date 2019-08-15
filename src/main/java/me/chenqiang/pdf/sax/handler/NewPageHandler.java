package me.chenqiang.pdf.sax.handler;

import org.dom4j.ElementPath;

import com.itextpdf.layout.element.AreaBreak;

import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.AreaBreakComposer;
import me.chenqiang.pdf.sax.composer.DocumentComposer;

public class NewPageHandler extends BasicTemplateElementHandler<AreaBreakComposer, AreaBreak>{
	
	public NewPageHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}

	@Override
	protected AreaBreakComposer create(ElementPath elementPath) {
		return AreaBreakComposer.getNextPage();
	}
	
	@Override
	public void register(ElementPath path) {
		path.addHandler("newpage", this);
	}

}
