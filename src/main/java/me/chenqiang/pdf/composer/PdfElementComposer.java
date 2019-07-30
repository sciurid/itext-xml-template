package me.chenqiang.pdf.composer;

import java.util.Collection;
import java.util.function.Consumer;

public interface PdfElementComposer<T> {
	public <C> T produce(C context);
	public Class<T> getElementClass();
	public void setAttribute(Consumer<? super T> attribute);
	public void setAllAttributes(Collection<? extends Consumer<? super T>> attributes);
}
