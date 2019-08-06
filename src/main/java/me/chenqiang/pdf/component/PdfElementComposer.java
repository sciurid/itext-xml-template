package me.chenqiang.pdf.component;

import java.util.Collection;
import java.util.function.Consumer;

import me.chenqiang.pdf.DocumentContext;

public interface PdfElementComposer<T, S extends PdfElementComposer<T, S>>{
	public String getId();
	public void setId(String id);
	public T produce(DocumentContext context);
	public Class<T> getElementClass();
	public void setAttribute(Consumer<? super T> attribute);
	public void setAllAttributes(Collection<? extends Consumer<? super T>> attributes);
}
