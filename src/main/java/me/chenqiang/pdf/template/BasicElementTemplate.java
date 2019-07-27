package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import me.chenqiang.pdf.template.element.ElementTemplate;

public abstract class BasicElementTemplate<T, S extends BasicElementTemplate<T, S>>
implements ElementTemplate<T>{
	protected List<Consumer<? super T>> attributes;	
	
	protected BasicElementTemplate() {
		this.attributes = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public S set(Consumer<? super T> attribute) {
		this.attributes.add(attribute);
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
