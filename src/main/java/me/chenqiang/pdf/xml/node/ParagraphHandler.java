package me.chenqiang.pdf.xml.node;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.Node;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.AttributeRegistry;

public class ParagraphHandler extends TemplateElementHandler<ParagraphComposer> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphNode.class);
	private ParagraphComposer tplPara;

	public ParagraphHandler(AttributeRegistry attrFactory, DocumentComposer tplDoc) {
		super(attrFactory, tplDoc::append);
	}

	public ParagraphHandler(AttributeRegistry attrFactory, TableCellComposer tplCell) {
		super(attrFactory, tplCell::append);
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
		
		this.tplPara.setAll(getModifiers(elementPath.getCurrent(), this.attrFactory.getParagraphMap()));
		
		elementPath.addHandler("text", new TextHandler(this.attrFactory, this.tplPara));
		elementPath.addHandler("image", new ImageHandler(this.attrFactory, this.tplPara));
	}
}
