package me.chenqiang.pdf.composer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class BasicElementComposer<T, S extends BasicElementComposer<T, S>>
implements ElementComposer<T>, StringStub {
	protected String id;
	protected LinkedList<Consumer<? super T>> attributes;	
	
	protected BasicElementComposer() {
		this.attributes = new LinkedList<>();
	}
	
	@SuppressWarnings("unchecked")
	public S setAttribute(Consumer<? super T> attribute) {
		this.attributes.addFirst(attribute);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S setAllAttributes(Collection<? extends Consumer<? super T>> attributes) {
		this.attributes.addAll(0, attributes);
		return (S)this;
	}
	
	protected abstract T create();

	@Override
	public <C> T produce(C context) {
		T instance = this.create();
		this.attributes.forEach(attribute -> attribute.accept(instance));
		return instance;
	}
}
