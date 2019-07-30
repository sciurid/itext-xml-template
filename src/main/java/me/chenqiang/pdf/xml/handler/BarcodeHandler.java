package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.composer.BarcodeComposer;
import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class BarcodeHandler extends BasicTemplateElementHandler<BarcodeComposer, Image>{
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeHandler.class);
	public BarcodeHandler(TemplateContext context, ComposerDirectory directory, DocumentComposer doc) {
		super(context, directory, doc::append);
	}
	public BarcodeHandler(TemplateContext context, ComposerDirectory directory, ParagraphComposer para) {
		super(context, directory, para::append);
	}
	public BarcodeHandler(TemplateContext context, ComposerDirectory directory, TableCellComposer cell) {
		super(context, directory, cell::append);
	}
	public BarcodeHandler(TemplateContext context, ComposerDirectory directory, DivComposer tplDiv) {
		super(context, directory, tplDiv::append);
	}

	@Override
	protected BarcodeComposer create(ElementPath elementPath) {		
		Element current = elementPath.getCurrent();
		String format = current.attributeValue(AttributeRegistry.FORMAT);
		if(format == null) {
			LOGGER.warn("No barcode format is specified: QRCode is assumed.");
		}
//		if(current.getText().isEmpty()) {
//			LOGGER.error("No barcode message is specified: ignored.");
//			return null;
//		}
		
		return new BarcodeComposer().setFormat(format).setText(current.getText());
	}
	
	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Image>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getImageMap();
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
