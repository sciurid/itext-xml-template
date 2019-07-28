package me.chenqiang.pdf.xml.handler;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ImageComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.ComposerDirectory;
import me.chenqiang.pdf.xml.TemplateContext;

public class ImageHandler extends TemplateElementHandler<ImageComposer> {

	public ImageHandler(TemplateContext context, DocumentComposer doc) {
		super(context, doc::append);
	}

	public ImageHandler(TemplateContext context, ParagraphComposer para) {
		super(context, para::append);
	}

	public ImageHandler(TemplateContext context, TableCellComposer cell) {
		super(context, cell::append);
	}

	@Override
	protected ImageComposer produce(ElementPath elementPath) {
		ImageComposer tplImg = new ImageComposer();
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			switch (name) {
			case AttributeRegistry.FILE:
				tplImg.setImageFile(value);
				break;
			case AttributeRegistry.RESOURCE:
				tplImg.setImageResource(value);
				break;
			case AttributeRegistry.REF:
				tplImg.setImageData(this.context.getResourceRepository().getImage(value));
				break;
			case AttributeRegistry.ID:
				ComposerDirectory dir = this.context.getComposerDirectory();
				dir.registerIdentifiable(value, tplImg);
				dir.registerDataPlaceholder(value, tplImg);
				break;
			default:
			}
		}
		tplImg.setAllAttributes(getModifiers(current, this.context.getAttributeRegistry().getImageMap()));
		return tplImg;
	}

}
