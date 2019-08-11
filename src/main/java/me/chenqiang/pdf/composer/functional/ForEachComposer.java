package me.chenqiang.pdf.composer.functional;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.PdfElementComposer;

@SuppressWarnings("rawtypes")
public class ForEachComposer implements DocumentComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(ForEachComposer.class);
	protected final String itemsExpr;
	protected final String var;
	protected final List<PdfElementComposer> subordinates;
	
	public ForEachComposer(String itemsExpr, String var) {
		super();
		this.itemsExpr = itemsExpr;
		this.var = var;
		this.subordinates = new ArrayList<>();
	}
	
	public void append(PdfElementComposer composer) {
		this.subordinates.add(composer);
	}
	
	protected void process(DocumentContext context, ConditionalProducer producer) {
		Object items = context.getProperty(itemsExpr);
		if(!(items instanceof Iterable)) {
			LOGGER.warn("Expression {} is not Iterable.", itemsExpr);
			return;
		}
		try {
			DocumentContext.Scope scope = context.beginScope();
			int index = 0;
			for(Object item : ((Iterable)items)) {
				scope.setParameter(var, item);
				scope.setParameter("_index", index);
				for(PdfElementComposer sub : this.subordinates) {
					producer.accept(sub, context);
				}
			}
		}
		finally {
			context.endScope();
		}
	}
	
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		this.process(context, (composer, ctx) -> {
			if(composer instanceof DocumentComponent) {
				((DocumentComponent)composer).process(doc, pdf, writer, context);
			}
		});
	}
}
