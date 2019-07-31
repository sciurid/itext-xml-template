package me.chenqiang.pdf.component;

import java.util.Collection;
import java.util.function.Consumer;

public interface PdfElementComposer<T, S extends PdfElementComposer<T, S>> extends Copyable<S>{
	public String getId();
	public void setId(String id);
	public <C> T produce(C context);
	public Class<T> getElementClass();
	public void setAttribute(Consumer<? super T> attribute);
	public void setAllAttributes(Collection<? extends Consumer<? super T>> attributes);
}
