package me.chenqiang.pdf.xml.handler;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.WatermarkMaker;
import me.chenqiang.pdf.WatermarkMaker.TextWatermarkSetting;
import me.chenqiang.pdf.xml.AttributeValueParser;
import me.chenqiang.pdf.xml.TemplateContext;

public class WatermarkTextHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkTextHandler.class);
	protected TemplateContext context;
	protected WatermarkMaker watermark;
	
	public WatermarkTextHandler(TemplateContext context, WatermarkMaker watermark) {
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
		TextWatermarkSetting setting = new TextWatermarkSetting();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			AttributeValueParser parser = new AttributeValueParser(name, value);
			try {
				switch (name) {
				case "offset-x":
					parser.setLengthInPoints(setting::setOffsetX);
					break;
				case "offset-y":
					parser.setLengthInPoints(setting::setOffsetY);
					break;
				case "width":
					parser.setLengthInPoints(setting::setWidth);
					break;
				case "font-size":
					parser.setLengthInPoints(setting::setFontSize);
					break;
				default:
				}
			} catch (RuntimeException e) {
				LOGGER.error("Error in watermark text loading.", e);
			}
		}
		
		String text = current.getTextTrim();
		if (text != null && text.length() > 0) {
			this.watermark.addRenderer((pdf, page, canvas) -> {
				WatermarkMaker.renderText(pdf, page, canvas, text, setting);
			});
		} else {
			LOGGER.error("Watermark text is not specified.");
		}
	}

}
