package me.chenqiang.pdf.composer;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.borders.Border;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.BorderAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;
import me.chenqiang.pdf.xml.context.CompositeAttribute;

public abstract class BasicElementPropertyContainerComposer<T extends ElementPropertyContainer<T>, S extends BasicElementPropertyContainerComposer<T, S>>
		extends BasicElementComposer<T, S>
		implements FontColorAttribute.Acceptor, BackgroundColorAttribute.Acceptor, BorderAttribute.Acceptor {

	@Override
	public void accept(FontColorAttribute fontColorAttr) {
		if (fontColorAttr != null) {
			this.attributes.add(fontColorAttr::apply);
		}
	}

	@Override
	public void accept(BackgroundColorAttribute backgroundColorAttr) {
		if (backgroundColorAttr != null) {
			this.attributes.add(backgroundColorAttr::apply);
		}
	}

	@Override
	public void accept(BorderAttribute common, 
			BorderAttribute top, BorderAttribute right, BorderAttribute bottom,	BorderAttribute left) {
		List<BiConsumer<T, Border>> setters = Arrays.<BiConsumer<T, Border>>asList(
				ElementPropertyContainer::setBorder,
				ElementPropertyContainer::setBorderTop, ElementPropertyContainer::setBorderRight,
				ElementPropertyContainer::setBorderBottom, ElementPropertyContainer::setBorderLeft);
		int index = 0;
		for(BorderAttribute attr : new BorderAttribute [] {common, top, right, bottom, left}) {
			if(attr != null) {
				BiConsumer<T, Border> setter = setters.get(index++);
				Consumer<T> borderConsumer = CompositeAttribute.createBorderAttribute(attr, setter);
				if(borderConsumer != null) {
					this.attributes.add(borderConsumer);
				}
			}			
			
		}
	}

	protected void setBorderAttribute(BorderAttribute borderAttr, BiConsumer<T, Border> setter) {
		if (borderAttr != null) {
			Consumer<T> attr = borderAttr.createAttribute(setter);
			if (attr != null) {
				this.attributes.add(attr);
			}
		}
	}
}
