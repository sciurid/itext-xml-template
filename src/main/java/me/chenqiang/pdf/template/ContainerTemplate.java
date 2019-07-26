package me.chenqiang.pdf.template;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.layout.element.AbstractElement;

public abstract class ContainerTemplate<T extends AbstractElement<T>, S extends ContainerTemplate<T, S>> 
extends StyledTemplate<T, ContainerTemplate<T, S>> {	
	protected List<ComponentInContainer<T>> components;

	public ContainerTemplate() {
		super();
		this.components = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public S append(ComponentInContainer<T> component) {
		this.components.add(component);
		return (S)this;
	}
	
}
