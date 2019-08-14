package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ImageComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class ImageHandler extends BasicTemplateElementHandler<ImageComposer, Image> {

	public ImageHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}

	public ImageHandler(TemplateContext context, ParagraphComposer para) {
		super(context, para::append);
	}

	public ImageHandler(TemplateContext context, TableCellComposer cell) {
		super(context, cell::append);
	}
	
	public ImageHandler(TemplateContext context, DivComposer tplDiv) {
		super(context, tplDiv::append);
	}

	@Override
	protected ImageComposer create(ElementPath elementPath) {
		ImageComposer tplImg = new ImageComposer();
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			switch (name) {
			case AttributeRegistry.FILE:
				tplImg.loadFromFile(value);
				break;
			case AttributeRegistry.RESOURCE:
				tplImg.loadFromResource(value);
				break;
			case AttributeRegistry.REF:
				tplImg.loadFromBytes(this.context.getImage(value));
				break;
			case AttributeRegistry.VALUE:
				tplImg.setValue(value);
				break;
			default:
			}
		}
		return tplImg;
	}
	
	public static final List<String> getElementNames() {
		return Arrays.asList("image", "img");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
