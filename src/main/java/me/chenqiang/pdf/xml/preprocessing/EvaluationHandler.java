package me.chenqiang.pdf.xml.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.xml.handler.BarcodeHandler;
import me.chenqiang.pdf.xml.handler.ImageHandler;

public class EvaluationHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationHandler.class);
	protected DocumentContext context;

	public EvaluationHandler(DocumentContext context) {
		super();
		this.context = context;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
//		for(Attribute attr : current.attributes()) {
//			String originalValue = attr.getValue();
//			attr.setValue(context.eval(originalValue));
//		}
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		switch(current.getName()) {
		case "foreach":
			this.doForEach(current);
			break;
		default:
			this.evaluate(current);
		}
	}		
	
	protected void evaluate(Element element) {
		boolean image = ImageHandler.getElementNames().contains(element.getName()) 
				|| BarcodeHandler.getElementNames().contains(element.getName());
		Iterator<Node> nodeIterator = element.nodeIterator();
		if(image) {
			Object data = context.getProperty(element.getTextTrim());
			if(data.getClass() == byte[].class) {
				element.setText(Base64.getUrlEncoder().encodeToString((byte[])data));
			}
			else {
				element.setText(null);
			}
		}
		else {
			while(nodeIterator.hasNext()) {
				Node node = nodeIterator.next();
				if(node.getNodeType() == Node.TEXT_NODE) {
					String text = node.getText();
					node.setText(context.eval(text));
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doForEach(Element current) {
		String itemsName = current.attributeValue("items");
		String varName = current.attributeValue("var");
		if(varName == null || itemsName == null) {
			LOGGER.error("Attribute var or item does not exist.");
			return;
		}
		
		Object underlyingItems = this.context.getProperty(itemsName);
		Iterable<Object> items;
		if(underlyingItems == null) {
			return;
		}
		if(underlyingItems.getClass().isArray()) {
			items = Arrays.asList((Object [])underlyingItems);
		} 
		else if(underlyingItems instanceof Iterable){
			items = (Iterable<Object>)underlyingItems;
		}
		else {
			LOGGER.error("Attribute 'items' is not array or iterable.");
			return;
		}
		
		Iterable<Node> iter = () -> current.nodeIterator();
		List<Node> nodes = StreamSupport.stream(iter.spliterator(), false).collect(Collectors.toList());		
		nodes.forEach(Node::detach);
		Element parent = current.getParent();
		current.detach();
		try {
			DocumentContext.Scope scope = context.beginScope();
			int index = 0;
			for(Object item: items) {
				scope.setParameter(varName, item);
				scope.setParameter("_index", index++);
				for(Node node : nodes) {
					Node repeated = (Node)node.clone();
					switch(repeated.getNodeType()) {
					case Node.ELEMENT_NODE:
						this.evaluate((Element)repeated);
						break;
					case Node.TEXT_NODE:
						repeated.setText(context.eval(repeated.getText()));
						break;
					default:
					}
					parent.add(repeated);
				}
			}
		}
		finally {
			context.endScope();
		}
	}
}