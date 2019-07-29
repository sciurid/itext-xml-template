package me.chenqiang.pdf.xml.handler;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.WatermarkMaker;
import me.chenqiang.pdf.WatermarkMaker.TextWatermarkSetting;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeValueParser;
import me.chenqiang.pdf.xml.context.TemplateContext;

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
				case AttributeRegistry.FONT_FAMILY:
					PdfFont font = this.context.getResourceRepository().getFont(parser.getString());
					if(font != null) {
						setting.setFont(font);
					}
					else {
						LOGGER.error("Font-family name '{}' not registered. Watermark may fail.", value);
					}
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
				case "font-size":
					parser.setUnitValue(setting::setFontSize);
					break;
				case "font-color":
					parser.setDeviceRgb(setting::setFontColor);
					break;
				case "opacity":
					parser.setFloat(setting::setOpacity);
					break;
				case "rotation":
					parser.setFloat(setting::setRotation);
					break;
				case AttributeRegistry.VERTICAL_ALIGNMENT:
					VerticalAlignment verticalAlignment = parser.getVerticalAlignment();
					if(verticalAlignment != null) {
						setting.setVerticalAlignment(verticalAlignment);
					}
					break;
				case AttributeRegistry.TEXT_ALIGN:
					TextAlignment textAlign = parser.getTextAlign();
					if(textAlign != null) {
						setting.setTextAlignment(textAlign);
					}
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
