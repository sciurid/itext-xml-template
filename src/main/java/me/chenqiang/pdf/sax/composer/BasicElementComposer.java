package me.chenqiang.pdf.sax.composer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.PdfElementComposer;

public abstract class BasicElementComposer<T, S extends BasicElementComposer<T, S>>
implements PdfElementComposer<T, S> {
	protected final LinkedList<Consumer<? super T>> attributes;
	protected final Class<T> elementClass;
	
	protected BasicElementComposer(Class<T> elementClass) {
		this.attributes = new LinkedList<>();
		this.elementClass = elementClass;
	}

	@Override
	public Class<T> getElementClass() {
		return this.elementClass;
	}

	@Override
	public void setAttribute(Consumer<? super T> attribute) {
		this.attributes.addFirst(attribute);
	}
	
	@Override
	public void setAllAttributes(Collection<? extends Consumer<? super T>> attributes) {
		this.attributes.addAll(0, attributes);
	}
	
	@SuppressWarnings("unchecked")
	public S chainSetAttribute(Consumer<? super T> attribute) {
		this.setAttribute(attribute);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S chainSetAllAttributes(Collection<? extends Consumer<? super T>> attributes) {
		this.setAllAttributes(attributes);
		return (S)this;
	}
	
	protected abstract T create(DocumentContext context);

	@Override
	public T produce(DocumentContext context) {
		T instance = this.create(context);
		if(instance == null) {
			return null;
		}
		this.attributes.forEach(attribute -> attribute.accept(instance));
		return instance;
	}

}
