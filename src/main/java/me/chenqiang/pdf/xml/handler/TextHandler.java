package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TextComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TextHandler extends BasicTemplateElementHandler<TextComposer, Text> {

	private TextComposer tplText;

	public TextHandler(TemplateContext context, ParagraphComposer tplPara) {
		super(context, tplPara::append);
	}

	public TextHandler(TemplateContext context, TableCellComposer tplCell) {
		super(context, tplCell::append);
	}
	
	public TextHandler(TemplateContext context, DivComposer tplDiv) {
		super(context, tplDiv::append);
	}

	@Override
	protected TextComposer create(ElementPath elementPath) {
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
	}

	public static final List<String> getElementNames() {
		return Arrays.asList("text", "span");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
