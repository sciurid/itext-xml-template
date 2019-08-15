package me.chenqiang.pdf.composer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.DocumentContext.Scope;
import me.chenqiang.pdf.component.PdfElementComposer;

public class ForEachComposer extends ConditionalComposer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ForEachComposer.class);
	protected String itemsName;
	protected String varName;
	
	public ForEachComposer(String itemsName, String varName) {
		this.itemsName = itemsName;
		this.varName = varName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doConditional(DocumentContext context, Consumer<PdfElementComposer> processor) {
		Object items = context.getProperty(itemsName);
		if(items == null) {
			return;
		}
		Iterator<Object> iter;
		if(items instanceof Object []) {
			iter = Arrays.asList((Object [])items).iterator();
		}
		else if(items instanceof Iterable) {
			iter = ((Iterable)items).iterator();
		}
		else if(items.getClass().isArray()) {
			int count = 0;
			List<Object> objs = new ArrayList<>();
			boolean end = false;
			while(!end) {
				try {
					objs.add(Array.get(objs, count++));
				}
				catch(IndexOutOfBoundsException e) {
					end = true;
				}
			}
			iter = objs.iterator();
		}
		else {
			LOGGER.warn("Attribute [items={}] is not iterable or array.", itemsName);
			return;
		}
		
		try {
			Scope scope = context.beginScope();
			int count = 0;
			while(iter.hasNext()) {
				scope.setParameter(this.varName, iter.next());
				scope.setParameter("_index", ++count);
				for(PdfElementComposer comp: this.components) {
					processor.accept(comp);
				}
			}			
		}
		finally {
			context.endScope();
		}
	}
}
