package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.utils.StringEscape;
import me.chenqiang.pdf.xml.context.AttributeUtils;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class ParagraphHandler extends BasicTemplateElementHandler<ParagraphComposer, Paragraph> {
	private ParagraphComposer tplPara;

	public ParagraphHandler(TemplateContext context, ComposerDirectory directory, DocumentComposer tplDoc) {
		super(context, directory, tplDoc::append);
	}

	public ParagraphHandler(TemplateContext context, ComposerDirectory directory, TableCellComposer tplCell) {
		super(context, directory, tplCell::append);
	}
	
	public ParagraphHandler(TemplateContext context, ComposerDirectory directory, DivComposer tplDiv) {
		super(context, directory, tplDiv::append);
	}

	@Override
	protected ParagraphComposer create(ElementPath elementPath) {
		this.resumeTextContent(elementPath.getCurrent());
		return this.tplPara;
	}

	protected static final Set<String> HANDLED_ELEMENT = new TreeSet<>();
	static {
		HANDLED_ELEMENT.addAll(TextHandler.getElementNames());
		HANDLED_ELEMENT.addAll(ImageHandler.getElementNames());
		HANDLED_ELEMENT.addAll(BarcodeHandler.getElementNames());
		HANDLED_ELEMENT.addAll(LineReturnHandler.getElementNames());
		HANDLED_ELEMENT.addAll(DivHandler.getElementNames());
	}
	protected void resumeTextContent(Element current) {
		int counter = 0;
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (HANDLED_ELEMENT.contains(node.getName())) {
					counter++;
				}
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				this.tplPara.insertAt(new StringComposer(StringEscape.escapeNodeText(node.getText())), counter++);
			}
		}
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplPara = new ParagraphComposer();
		
		new TextHandler(this.context, this.directory, this.tplPara).register(elementPath);
		new ImageHandler(this.context, this.directory, this.tplPara).register(elementPath);
		new BarcodeHandler(this.context, this.directory, this.tplPara).register(elementPath);
		new DivHandler(this.context, this.directory, this.tplPara).register(elementPath);
		new LineReturnHandler(this.tplPara).register(elementPath);
	}
	
	@Override
	public void onEnd(ElementPath elementPath) {
		super.onEnd(elementPath);
		List<String []> defaultParagraphStyle = this.context.getDefaultParagraphStyle();
		if(defaultParagraphStyle != null) {
			AttributeUtils.setComposerAttributes(defaultParagraphStyle, this.context.getAttributeRegistry().getParagraphMap(), this.tplPara);
		}
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getParagraphMap();
	}
	
	public static List<String> getElementNames() {
		return Arrays.asList("paragraph", "para", "p");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
