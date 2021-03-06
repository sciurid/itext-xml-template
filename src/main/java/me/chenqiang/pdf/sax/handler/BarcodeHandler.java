package me.chenqiang.pdf.sax.handler;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.common.attribute.AttributeNames;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.BarcodeComposer;
import me.chenqiang.pdf.sax.composer.DivComposer;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.composer.ForEachComposer;
import me.chenqiang.pdf.sax.composer.IfComposer;
import me.chenqiang.pdf.sax.composer.ParagraphComposer;
import me.chenqiang.pdf.sax.composer.TableCellComposer;

public class BarcodeHandler extends BasicTemplateElementHandler<BarcodeComposer, Image>{
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeHandler.class);
	public BarcodeHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}
	public BarcodeHandler(TemplateContext context, ParagraphComposer para) {
		super(context, para::append);
	}
	public BarcodeHandler(TemplateContext context, TableCellComposer cell) {
		super(context, cell::append);
	}
	public BarcodeHandler(TemplateContext context, DivComposer tplDiv) {
		super(context, tplDiv::append);
	}
	public BarcodeHandler(TemplateContext context, ForEachComposer foreach) {
		super(context, foreach::append);
	}
	public BarcodeHandler(TemplateContext context, IfComposer foreach) {
		super(context, foreach::append);
	}

	@Override
	protected List<String> listIgnoredAttributes() {
		 List<String> list = super.listIgnoredAttributes();
		 list.add(AttributeNames.FORMAT);
		 return list;
	}
	
	@Override
	protected BarcodeComposer create(ElementPath elementPath) {		
		Element current = elementPath.getCurrent();
		String format = current.attributeValue(AttributeNames.FORMAT);
		if(format == null) {
			LOGGER.warn("No barcode format is specified: QRCode is assumed.");
		}
		String code = current.getTextTrim();
		return new BarcodeComposer(code, format);
	}
	
	public static final List<String> getElementNames() {
		return Arrays.asList("barcode");
	}	
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
