package me.chenqiang.pdf.xml.handler;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.BarcodeComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.TemplateContext;

public class BarcodeHandler extends BasicTemplateElementHandler<BarcodeComposer>{
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

	@Override
	protected BarcodeComposer produce(ElementPath elementPath) {
		
		Element current = elementPath.getCurrent();
		String format = current.attributeValue(AttributeRegistry.FORMAT);
		if(format == null) {
			LOGGER.warn("No barcode format is specified: QRCode is assumed.");
		}
		if(current.getText().isEmpty()) {
			LOGGER.error("No barcode message is specified: ignored.");
			return null;
		}
		
		BarcodeComposer barcode = new BarcodeComposer()
				.setFormat(format).setMessage(current.getText());
		AttributeRegistry attrreg = this.context.getAttributeRegistry();
		barcode.accept(attrreg.getFontColorAttribute(listAttributes(current)));
		barcode.accept(attrreg.getBackgroundColorAttribute(listAttributes(current)));
		barcode.setAllAttributes(getModifiers(current, attrreg.getImageMap()));
		return barcode;
	}

}
