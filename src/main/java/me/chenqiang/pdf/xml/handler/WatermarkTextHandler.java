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

import me.chenqiang.pdf.composer.WatermarkMaker;
import me.chenqiang.pdf.composer.WatermarkMaker.TextWatermarkSetting;
import me.chenqiang.pdf.utils.SerializableCloning;
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
					byte [] font = this.context.getResourceRepository().getFont(parser.getString());
					if(font != null) {
						setting.setFont(font);
					}
					else {
						LOGGER.error("Font-family name '{}' not registered. Watermark may fail.", value);
					}
					break;
				case AttributeRegistry.OFFSET_X:
					parser.setLength(setting::setOffsetX);
					break;
				case AttributeRegistry.OFFSET_Y:
					parser.setLength(setting::setOffsetY);
					break;
				case AttributeRegistry.WIDTH:
					parser.setLength(setting::setWidth);
					break;
				case AttributeRegistry.FONT_SIZE:
					parser.setUnitValue(setting::setFontSize);
					break;
				case AttributeRegistry.FONT_COLOR:
					parser.setDeviceRgb(setting::setFontColor);
					break;
				case AttributeRegistry.OPACITY:
					parser.setFloat(setting::setOpacity);
					break;
				case AttributeRegistry.ROTATION:
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
		
		String text = current.getTextTrim().replaceAll("(?:\\\\r)?\\\\n", "\n");
		if (text != null && text.length() > 0) {
			this.watermark.addRenderer((pdf, page, canvas) -> 
				WatermarkMaker.renderText(pdf, page, canvas, text, setting)
			);
		} else {
			LOGGER.error("Watermark text is not specified.");
		}
	}

	public void register(ElementPath path) {
		path.addHandler("text", this);
		path.addHandler("span", this);
	}
}
