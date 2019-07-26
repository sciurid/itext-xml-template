package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.itextpdf.layout.ElementPropertyContainer;

public abstract class StyledTemplate<T extends ElementPropertyContainer<T>, S extends StyledTemplate<T, S>> {
	protected List<Consumer<? super T>> modifiers;	
	
	public StyledTemplate() {
		this.modifiers = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public S set(Consumer<? super T> attribute) {
		this.modifiers.add(attribute);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S setAll(List<? extends Consumer<? super T>> attributes) {
		this.modifiers.addAll(attributes);
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S inherit(List<? extends Consumer<? super T>> attributes) {
		this.modifiers.addAll(0, attributes);
		return (S)this;
	}
	
	public void apply(T element) {
		modifiers.forEach(attribute -> attribute.accept(element));
	}
}
