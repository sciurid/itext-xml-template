package me.chenqiang.pdf.xml.node;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ImageComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class ImageHandler extends TemplateElementHandler<ImageComposer> {
	protected ImageComposer tplImg;

	public ImageHandler(AttributeRegistry attrFactory, DocumentComposer doc) {
		super(attrFactory, doc::append);
	}
	
	public ImageHandler(AttributeRegistry attrFactory, ParagraphComposer para) {
		super(attrFactory, para::append);
	}
	
	public ImageHandler(AttributeRegistry attrFactory, TableCellComposer cell) {
		super(attrFactory, cell::append);
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplImg = new ImageComposer();
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			switch (name) {
			case "file":
				this.tplImg.setImageFile(value);
				break;
			case "resource":
				this.tplImg.setImageResource(value);
				break;
			case "base64":
				this.tplImg.setImageDataBase64(value);
				break;
			case "hex":
				this.tplImg.setImageDataHex(value);
				break;
			default:
			}
		}
		this.tplImg.setAll(getModifiers(current, this.attrFactory.getImageMap()));
	}

	@Override
	protected ImageComposer produce(ElementPath elementPath) {
		return this.tplImg;
	}

}
