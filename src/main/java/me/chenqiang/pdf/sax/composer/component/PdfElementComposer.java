package me.chenqiang.pdf.sax.composer.component;

import java.util.Collection;
import java.util.function.Consumer;

import me.chenqiang.pdf.DocumentContext;

public interface PdfElementComposer<T, S extends PdfElementComposer<T, S>>{
	public T produce(DocumentContext context);
	public Class<T> getElementClass();
	public void setAttribute(Consumer<? super T> attribute);
	public void setAllAttributes(Collection<? extends Consumer<? super T>> attributes);
}
