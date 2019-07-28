package me.chenqiang.pdf.xml.handler;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TextComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;
import me.chenqiang.pdf.xml.ComposerDirectory;
import me.chenqiang.pdf.xml.TemplateContext;

public class TextHandler extends BasicTemplateElementHandler<TextComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(TextNode.class);

	private TextComposer tplText;

	public TextHandler(TemplateContext context, ParagraphComposer tplPara) {
		super(context, tplPara::append);
	}

	public TextHandler(TemplateContext context, TableCellComposer tplCell) {
		super(context, tplCell::append);
	}

	@Override
	protected TextComposer produce(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.TEXT_NODE) {
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
		String id = current.attributeValue(AttributeRegistry.ID);
		if (id != null) {
			ComposerDirectory dir = this.context.getComposerDirectory();
			dir.registerIdentifiable(id, this.tplText);
			dir.registerStringPlaceholder(id, this.tplText);
		}
		AttributeRegistry attrreg = this.context.getAttributeRegistry();
		this.tplText.accept(attrreg.getFontColorAttribute(listAttributes(current)));
		this.tplText.accept(attrreg.getBackgroundColorAttribute(listAttributes(current)));
		this.tplText.setAllAttributes(getModifiers(current, attrreg.getTextMap()));
	}
}
