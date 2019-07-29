package me.chenqiang.pdf.xml.handler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class ParagraphHandler extends BasicTemplateElementHandler<ParagraphComposer, Paragraph> {
	private ParagraphComposer tplPara;

	public ParagraphHandler(TemplateContext context, ComposerDirectory directory, DocumentComposer tplDoc) {
		super(context, directory, tplDoc::append);
	}

	public ParagraphHandler(TemplateContext context, ComposerDirectory directory, TableCellComposer tplCell) {
		super(context, directory, tplCell::append);
	}

	@Override
	protected ParagraphComposer produce(ElementPath elementPath) {
		this.resumeTextContent(elementPath.getCurrent());
		return this.tplPara;
	}

	protected void resumeTextContent(Element current) {
		int counter = 0;
		for (Node node : current.content()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getName();
				if ("text".equals(nodeName) || "image".equals(nodeName)) {
					counter++;
				}
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				String cleared = node.getText().replaceAll("[\\r\\n]+\\s+", "");
				this.tplPara.insertAt(new StringComposer(cleared), counter++);
			}
		}
	}

	@Override
	public void onStart(ElementPath elementPath) {
		super.onStart(elementPath);
		this.tplPara = new ParagraphComposer();		
		elementPath.addHandler("text", new TextHandler(this.context, this.directory, this.tplPara));
		elementPath.addHandler("image", new ImageHandler(this.context, this.directory, this.tplPara));
		elementPath.addHandler("barcode", new BarcodeHandler(this.context, this.directory, this.tplPara));
	}

	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Paragraph>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getParagraphMap();
	}
	
	
}
