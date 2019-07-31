package me.chenqiang.pdf.xml.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Cell;

import me.chenqiang.pdf.composer.TableRowComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.AttributeUtils;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TableRowHander implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableRowHander.class);
	protected TemplateContext context;
	protected TableRowComposer row;

	public TableRowHander(TemplateContext context, TableRowComposer row) {
		this.context = context;
		this.row = row;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		new TableCellHandler(this.context, this.row).register(elementPath);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		
		Map<String, BiFunction<String, String, ? extends Consumer<? super Cell>>> cellMap = this.context.getAttributeRegistry().getCellMap();
		String styleId = current.attributeValue(AttributeRegistry.STYLE);
		
		List<String []> attributes = new ArrayList<>();
		if(styleId != null){
			List<String []> styleAttributes = this.context.getPredefinedStyle(styleId);
			if(styleAttributes != null) {
				attributes.addAll(styleAttributes);
//				List<String[]> filtered = styleAttributes.stream().filter(attr -> attributeMap.containsKey(attr[0])).collect(Collectors.toList());
//				AttributeUtils.setComposerAttributes(styleAttributes, cellMap, this.row);
//				AttributeUtils.getCompositeAttribute(styleAttributes).setComposerAttribute(this.row);
			}
		}
		current.attributes().forEach(attr -> attributes.add(new String[] {attr.getName(), attr.getValue()}));
		
		AttributeUtils.setComposerAttributes(attributes, cellMap, this.row);
		AttributeUtils.getCompositeAttribute(attributes).setComposerAttribute(this.row);
		
		LOGGER.debug("[END] {}", elementPath.getPath());
	}
}
