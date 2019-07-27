package me.chenqiang.pdf.composer;

public interface ElementComposer<T> {
	public <C> T produce(C context);
}
