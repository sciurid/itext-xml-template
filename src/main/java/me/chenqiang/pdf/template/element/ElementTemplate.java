package me.chenqiang.pdf.template.element;

public interface ElementTemplate<T> {
	public <C> T produce(C context);
}
