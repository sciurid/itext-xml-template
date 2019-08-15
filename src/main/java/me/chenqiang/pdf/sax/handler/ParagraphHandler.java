package me.chenqiang.pdf.sax.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.common.attribute.AttributeRegistry;
import me.chenqiang.pdf.common.utils.StringEscape;
import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.DivComposer;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.composer.ForEachComposer;
import me.chenqiang.pdf.sax.composer.IfComposer;
import me.chenqiang.pdf.sax.composer.ParagraphComposer;
import me.chenqiang.pdf.sax.composer.StringComposer;
import me.chenqiang.pdf.sax.composer.TableCellComposer;

public class ParagraphHandler extends BasicTemplateElementHandler<ParagraphComposer, Paragraph> {
	private ParagraphComposer tplPara;

	public ParagraphHandler(TemplateContext context, DocumentComposer tplDoc) {
		super(context, tplDoc::append);
	}

	public ParagraphHandler(TemplateContext context, TableCellComposer tplCell) {
		super(context, tplCell::append);
	}
	
	public ParagraphHandler(TemplateContext context, DivComposer tplDiv) {
		super(context, tplDiv::append);
	}
	
	public ParagraphHandler(TemplateContext context, ForEachComposer foreach) {
		super(context, foreach::append);
	}
	
	public ParagraphHandler(TemplateContext context, IfComposer foreach) {
		super(context, foreach::append);
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
		
		new TextHandler(this.context, this.tplPara).register(elementPath);
		new ImageHandler(this.context, this.tplPara).register(elementPath);
		new BarcodeHandler(this.context, this.tplPara).register(elementPath);
		new DivHandler(this.context, this.tplPara).register(elementPath);
		new LineReturnHandler(this.tplPara).register(elementPath);
		
		new ForEachHandler(this.context, this.tplPara::append).register(elementPath);	
		new IfHandler(this.context, this.tplPara::append).register(elementPath);
	}
	
	@Override
	public void onEnd(ElementPath elementPath) {
		super.onEnd(elementPath);
		List<String []> dps = this.context.getDefaultParagraphStyle();
		if(dps != null) {
			AttributeRegistry ar = this.context.getAttributeRegistry();
			dps.forEach(item -> this.tplPara.setAttribute(ar.get(Paragraph.class, item[0], item[1])));
		}
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
