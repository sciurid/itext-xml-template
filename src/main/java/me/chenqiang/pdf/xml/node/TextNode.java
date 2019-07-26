package me.chenqiang.pdf.xml.node;

import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.template.ParagraphTemplate;
import me.chenqiang.pdf.template.TextTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class TextNode implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TextNode.class);
	private StyleAttributeFactory attrFactory;	
	private ParagraphTemplate tplPara;
	private int count;
	
	private TextTemplate tplText;
	
	public TextNode(ParagraphTemplate tplPara, StyleAttributeFactory attrFactory) {
		this.attrFactory = attrFactory;
		this.tplPara = tplPara;
		this.count = 0;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);
		this.tplText = new TextTemplate();
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		
		for(Attribute attr : current.attributes()) {
			Consumer<Text> modifier = 
					this.attrFactory.<Text>getElementPropertyContainerAttribute(
							attr.getName(), attr.getValue());
			if(modifier == null) {
				modifier = this.attrFactory.getTextAttribute(attr.getName(), attr.getValue());
			}
			if(modifier != null) {
				tplText.set(modifier);
			}
			else {
				LOGGER.error("Not recognized attribute: {}", attr.getName());
			}
		}
		
		for(Node node : current.content()) {
			if(node.getNodeType() == Node.TEXT_NODE) {
				tplText.append(node.getText());
			}
		}
		this.tplPara.append(tplText);
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}
}
