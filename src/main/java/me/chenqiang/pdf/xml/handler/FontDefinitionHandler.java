package me.chenqiang.pdf.xml.handler;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.xml.ResourceRepository;

public class FontDefinitionHandler implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(FontDefinitionHandler.class);
	protected ResourceRepository fontCollection;	

	public FontDefinitionHandler(ResourceRepository fontCollection) {
		super();
		this.fontCollection = fontCollection;
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
		 Element current = elementPath.getCurrent();
		 String path = null;
		 String type = null;
		 String [] names = null;
		 int ttcIndex = -1;
		 
		 Iterator<Attribute> iter = current.attributeIterator();
		 while(iter.hasNext()) {
			 Attribute attr = iter.next();
			 String attrName = attr.getName();
			 String attrValue = attr.getValue();
			 switch(attrName) {
			 case "type":
				 type = attrValue;
				 break;
			 case "path":
				 path = attrValue;
				 break;
			 case "ttc-index":
				 try {
					 ttcIndex = Integer.parseInt(attrValue);
				 }
				 catch(NumberFormatException nfe) {
					 LOGGER.error("NaN ttc-index: {}", attrValue);
					 return;
				 }
				 break;
			 case "name":
				 names = attrValue.split("\\s*,\\s*");
				 break;
			 default:
				 LOGGER.warn("Attribute not recognized: {}", attrName);
			 }
		 }
		 if(names == null || names.length == 0) {
			 LOGGER.error("Names for font not defined.");
			 return;
		 }
		 if(type == null) {
			 LOGGER.error("Type for font not defined.");
			 return;
		 }
		 try {
			 if("file".equals(type)) {
				 if(ttcIndex < 0) {
					 this.fontCollection.loadFontFile(path, names);
					 LOGGER.info("Font file '{}' with names '{}' loaded.", path, (Object)names);
				 }
				 else {
					 this.fontCollection.loadTtcFontFile(path, ttcIndex, names);
					 LOGGER.info("Font file '{}' with names '{}' loaded.", path, (Object)names);
				 }
			 }
			 else if("resource".equals(type)) {
				 if(ttcIndex < 0) {
					 this.fontCollection.loadFontResource(path, names);
					 LOGGER.info("Font resource '{}' with names '{}' loaded.", path, (Object)names);
				 }
				 else {
					 this.fontCollection.loadTtcFontResource(path, ttcIndex, names);
					 LOGGER.info("Font resource '{}' with names '{}' loaded.", path, (Object)names);
				 }				
			 }
			 else {
				 LOGGER.error("Unrecognized type: '{}'.", type);
			 }
		 }
		 catch (Exception e) {
			 LOGGER.error("Error in loading font", e);
		 } 
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// No content of the <font/> element is needed.
	}

}
