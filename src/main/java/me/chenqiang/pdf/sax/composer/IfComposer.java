package me.chenqiang.pdf.sax.composer;

import java.util.ArrayList;
import java.util.function.Consumer;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.PdfElementComposer;

public class IfComposer extends ConditionalComposer {
	protected String testName;
	
	public IfComposer(String testName) {
		this.components = new ArrayList<>();
		this.testName = testName;
	}

	@SuppressWarnings({ "rawtypes" })
	public void doConditional(DocumentContext context, Consumer<PdfElementComposer> processor) {
		Object item = context.getProperty(testName);
		boolean predicate = false;
		if(item == null) {
			return;
		}
		else if(item instanceof Boolean) {
			predicate = ((Boolean) item).booleanValue();
		}
		else if(item instanceof String) {
			predicate = Boolean.parseBoolean((String)item);
		}
		else {
			predicate = Boolean.parseBoolean(item.toString());
		}
		
		if(predicate) {
			for(PdfElementComposer comp: this.components) {
				processor.accept(comp);
			}
		}
	}	
}
