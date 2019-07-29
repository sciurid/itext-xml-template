package me.chenqiang.pdf.composer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import me.chenqiang.pdf.configurability.StringStub;

public abstract class BasicElementComposer<T, S extends BasicElementComposer<T, S>>
implements ElementComposer<T>, StringStub, AttributedComposer<T> {
	protected String id;
	protected LinkedList<Consumer<? super T>> attributes;	
	
	protected BasicElementComposer() {
		this.attributes = new LinkedList<>();
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
	public S set(Consumer<? super T> attribute) {
		this.setAttribute(attribute);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S setAll(Collection<? extends Consumer<? super T>> attributes) {
		this.setAllAttributes(attributes);
		return (S)this;
	}
	
	protected abstract T create();

	@Override
	public <C> T produce(C context) {
		T instance = this.create();
		this.attributes.forEach(attribute -> attribute.accept(instance));
		return instance;
	}
	
	public static <T, S extends BasicElementComposer<T, S>> 
	void deepCopy(BasicElementComposer<T, S> origin, BasicElementComposer<T, S> target) {
		target.attributes.clear();
		target.attributes.addAll(origin.attributes);
	}
}
