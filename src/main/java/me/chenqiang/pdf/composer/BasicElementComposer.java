package me.chenqiang.pdf.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class BasicElementComposer<T, S extends BasicElementComposer<T, S>>
implements ElementComposer<T>{
	protected List<Consumer<? super T>> attributes;	
	
	protected BasicElementComposer() {
		this.attributes = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public S set(Consumer<? super T> attribute) {
		this.attributes.add(attribute);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S setAll(Collection<? extends Consumer<? super T>> attributes) {
		this.attributes.addAll(attributes);
		return (S)this;
	}
	
	protected abstract T create();

	@Override
	public <C> T produce(C context) {
		T instance = this.create();
		this.attributes.forEach(attribuate -> attribuate.accept(instance));
		return instance;
	}
}
