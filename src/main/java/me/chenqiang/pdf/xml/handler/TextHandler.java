package me.chenqiang.pdf.xml.handler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Text;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TextComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class TextHandler extends BasicTemplateElementHandler<TextComposer, Text> {

	private TextComposer tplText;

	public TextHandler(TemplateContext context, ComposerDirectory directory, ParagraphComposer tplPara) {
		super(context, directory, tplPara::append);
	}

	public TextHandler(TemplateContext context, ComposerDirectory directory, TableCellComposer tplCell) {
		super(context, directory, tplCell::append);
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
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Text>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getTextMap();
	}

	@Override
	public void register(ElementPath path) {
		path.addHandler("text", this);
		path.addHandler("span", this);
	}
}
