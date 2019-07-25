package me.chenqiang.pdf.xml.node;

import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.template.ParagraphTemplate;
import me.chenqiang.pdf.template.StringTemplate;
import me.chenqiang.pdf.xml.StyleAttributeFactory;

public class ParagraphNode implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphNode.class);
	
	private StyleAttributeFactory attrFactory;
	private DocumentTemplate tplDoc;
	private int count;
	
	private ParagraphTemplate tplPara;
	
	public ParagraphNode(DocumentTemplate tplDoc, StyleAttributeFactory attrFactory) {
		this.tplDoc = tplDoc;
		this.attrFactory = attrFactory;
		this.count = 0;
	}

	@Override
	public void onStart(ElementPath elementPath) {	
		LOGGER.debug("[START] {} - {}", elementPath.getPath(), this.count);	
		this.tplPara = new ParagraphTemplate();
		elementPath.addHandler("text", new TextNode(this.tplPara, this.attrFactory));		
	}
	
	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		for(Attribute attr : current.attributes()) {
			Consumer<Paragraph> modifier = 
					this.attrFactory.<Paragraph>getElementPropertyContainerAttribute(
							attr.getName(), attr.getValue());
			if(modifier != null) {
				this.tplPara.set(modifier);
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
		
		this.tplDoc.append(this.tplPara);
		LOGGER.debug("[END] {} - {}", elementPath.getPath(), this.count++);
	}
}
