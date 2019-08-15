package me.chenqiang.pdf.xml.context;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface interface StyleAttributeFactory<T> {
	public Consumer<T> create(String name, String value);
	public default <S> Consumer<T> create(S obj, Function<? super S, String> nameGetter, Function<? super S, String> valueGetter) {
		return this.create(nameGetter.apply(obj), valueGetter.apply(obj));
	}
}