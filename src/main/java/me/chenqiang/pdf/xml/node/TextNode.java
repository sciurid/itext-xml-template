package me.chenqiang.pdf.xml.node;

import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.template.element.ParagraphTemplate;
import me.chenqiang.pdf.template.element.TextTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class TextNode extends TemplateElementNode<TextTemplate>{
	private static final Logger LOGGER = LoggerFactory.getLogger(TextNode.class);
	
	private TextTemplate tplText;
	
	public TextNode(StyleAttributeFactory attrFactory, ParagraphTemplate tplPara) {
		super(attrFactory, tplPara::append);
	}
	
	@Override
	protected TextTemplate produce(ElementPath elementPath) {
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
		return this.tplText;
	}


	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplText = new TextTemplate();
	}
}
