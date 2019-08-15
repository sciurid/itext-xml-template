package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Div;

import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ForEachComposer;
import me.chenqiang.pdf.composer.IfComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.utils.StringEscape;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class DivHandler extends BasicTemplateElementHandler<DivComposer, Div> {
	private DivComposer tplDiv;

	public DivHandler(TemplateContext context, DocumentComposer tplDoc) {
		super(context, tplDoc::append);
	}

	public DivHandler(TemplateContext context,  TableCellComposer tplCell) {
		super(context, tplCell::append);
	}
	
	public DivHandler(TemplateContext context, ParagraphComposer tplPara) {
		super(context, tplPara::append);
	}
	
	public DivHandler(TemplateContext context, ForEachComposer foreach) {
		super(context, foreach::append);
	}
	
	public DivHandler(TemplateContext context, IfComposer foreach) {
		super(context, foreach::append);
	}

	@Override
	protected DivComposer create(ElementPath elementPath) {
		this.resumeTextContent(elementPath.getCurrent());
		return this.tplDiv;
	}

	protected static final Set<String> HANDLED_ELEMENT = new TreeSet<>();
	static {
		HANDLED_ELEMENT.addAll(TextHandler.getElementNames());
		HANDLED_ELEMENT.addAll(ImageHandler.getElementNames());
		HANDLED_ELEMENT.addAll(BarcodeHandler.getElementNames());
		HANDLED_ELEMENT.addAll(ParagraphHandler.getElementNames());
	}
	protected void resumeTextContent(Element current) {
		int counter = 0;
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (HANDLED_ELEMENT.contains(node.getName())) {
					counter++;
				}
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				this.tplDiv.insertAt(new StringComposer(StringEscape.escapeNodeText(node.getText())), counter++);
			}
		}
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplDiv = new DivComposer();
		
		new TextHandler(this.context, this.tplDiv).register(elementPath);
		new ImageHandler(this.context, this.tplDiv).register(elementPath);
		new BarcodeHandler(this.context, this.tplDiv).register(elementPath);
		new ParagraphHandler(this.context, this.tplDiv).register(elementPath);
		
		new ForEachHandler(this.context, this.tplDiv::append).register(elementPath);
		new IfHandler(this.context, this.tplDiv::append).register(elementPath);
	}

	public static List<String> getElementNames() {
		return Arrays.asList("div");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}
}
