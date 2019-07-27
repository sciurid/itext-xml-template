package me.chenqiang.pdf.xml.node;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TextComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class TextHandler extends TemplateElementHandler<TextComposer>{
//	private static final Logger LOGGER = LoggerFactory.getLogger(TextNode.class);
	
	private TextComposer tplText;
	
	public TextHandler(AttributeRegistry attrFactory, ParagraphComposer tplPara) {
		super(attrFactory, tplPara::append);
	}
	public TextHandler(AttributeRegistry attrFactory, TableCellComposer tplCell) {
		super(attrFactory, tplCell::append);
	}
	
	@Override
	protected TextComposer produce(ElementPath elementPath) {
		Element current = elementPath.getCurrent();	
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
		this.tplText = new TextComposer();
		Element current = elementPath.getCurrent();	
		this.tplText.setAll(getModifiers(current, this.attrFactory.getTextMap()));
	}
}
