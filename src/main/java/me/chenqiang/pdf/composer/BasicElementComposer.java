package me.chenqiang.pdf.composer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import me.chenqiang.pdf.component.PdfElementComposer;

public abstract class BasicElementComposer<T, S extends BasicElementComposer<T, S>>
implements PdfElementComposer<T, S> {
	protected String id;
	protected final LinkedList<Consumer<? super T>> attributes;
	protected final Class<T> elementClass;
	
	protected BasicElementComposer(Class<T> elementClass) {
		this.attributes = new LinkedList<>();
		this.elementClass = elementClass;
	}
	
	protected BasicElementComposer(BasicElementComposer<T, S> origin) {
		this(origin.elementClass);
		this.attributes.addAll(origin.attributes);
		this.id = origin.id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	protected abstract T create();

	@Override
	public <C> T produce(C context) {
		T instance = this.create();
		if(instance == null) {
			return null;
		}
		this.attributes.forEach(attribute -> attribute.accept(instance));
		return instance;
	}

}
