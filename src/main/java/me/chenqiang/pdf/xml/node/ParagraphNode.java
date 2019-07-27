package me.chenqiang.pdf.xml.node;

import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.template.element.ParagraphTemplate;
import me.chenqiang.pdf.template.element.StringTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class ParagraphNode extends TemplateElementNode<ParagraphTemplate>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphNode.class);	
	private ParagraphTemplate tplPara;
	
	public ParagraphNode(StyleAttributeFactory attrFactory, DocumentTemplate tplDoc) {
		super(attrFactory, tplDoc::append);
	}

	@Override
	protected ParagraphTemplate produce(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		for(Attribute attr : current.attributes()) {
			Consumer<Paragraph> modifier = 
					this.attrFactory.<Paragraph>getElementPropertyContainerAttribute(
							attr.getName(), attr.getValue());
			if(modifier == null) {
				modifier = this.attrFactory.getParagraphAttribute(attr.getName(), attr.getValue());
			}
			if(modifier != null) {
				this.tplPara.set(modifier);
			}
			else {
				LOGGER.error("Not recognized attribute: {}", attr.getName());
			}
		}
		
		int counter = 0;
		for(Node node : current.content()) {
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getName();
				if("text".equals(nodeName) || "image".equals(nodeName)) {
					counter ++;
				}
			}
			else if(node.getNodeType() == Node.TEXT_NODE) {
				String cleared = node.getText().replaceAll("[\\r\\n]+\\s+", "");
				this.tplPara.insertAt(new StringTemplate(cleared), counter++);
			}
		}
		return this.tplPara;
	}



	@Override
	public void onStart(ElementPath elementPath) {	
		super.onStart(elementPath);	
		this.tplPara = new ParagraphTemplate();
		elementPath.addHandler("text", new TextNode(this.attrFactory, this.tplPara));		
	}
}
