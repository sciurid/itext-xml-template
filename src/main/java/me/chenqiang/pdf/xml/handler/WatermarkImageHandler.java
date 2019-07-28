package me.chenqiang.pdf.xml.handler;

import java.io.IOException;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import me.chenqiang.pdf.WatermarkMaker;
import me.chenqiang.pdf.WatermarkMaker.ImageWatermarkSetting;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.AttributeValueParser;
import me.chenqiang.pdf.xml.TemplateContext;

public class WatermarkImageHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkImageHandler.class);
	protected TemplateContext context;
	protected WatermarkMaker watermark;
	
	public WatermarkImageHandler(TemplateContext context, WatermarkMaker watermark) {
		this.context = context;
		this.watermark = watermark;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		// DO NOTHING.
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		ImageData imageData = null;
		ImageWatermarkSetting setting = new ImageWatermarkSetting();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			AttributeValueParser parser = new AttributeValueParser(name, value);
			try {
				switch (name) {
				case AttributeRegistry.FILE:
					imageData = ImageDataFactory.create(parser.getString());
					break;
				case AttributeRegistry.RESOURCE:
					imageData = ImageDataFactory
							.create(WatermarkImageHandler.class.getResourceAsStream(parser.getString()).readAllBytes());
					break;
				case AttributeRegistry.REF:
					imageData = this.context.getResourceRepository().getImage(parser.getString());
					break;
				case "offset-x":
					parser.setLengthInPoints(setting::setOffsetX);
					break;
				case "offset-y":
					parser.setLengthInPoints(setting::setOffsetY);
					break;
				case "width":
					parser.setLengthInPoints(setting::setWidth);
					break;
				case "opacity":
					parser.setLengthInPoints(setting::setOpacity);
					break;
				default:
				}
			} catch (RuntimeException | IOException e) {
				LOGGER.error("Error in watermark image loading.", e);
			}
		}
		if (imageData != null) {
			final ImageData image = imageData;
			this.watermark.addRenderer((pdf, page, canvas) -> {
				WatermarkMaker.renderImage(pdf, page, canvas, image, setting);
			});
		} else {
			LOGGER.error("Watermark image is not specified.");
		}
	}

}
