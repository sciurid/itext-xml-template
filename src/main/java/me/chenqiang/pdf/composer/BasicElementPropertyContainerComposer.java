package me.chenqiang.pdf.composer;

import com.itextpdf.layout.ElementPropertyContainer;

import me.chenqiang.pdf.attribute.BackgroundColorAttribute;
import me.chenqiang.pdf.attribute.FontColorAttribute;

public abstract class BasicElementPropertyContainerComposer<T extends ElementPropertyContainer<T>, S extends BasicElementPropertyContainerComposer<T, S>> 
extends BasicElementComposer<T, S> 
implements FontColorAttribute.Acceptor, BackgroundColorAttribute.Acceptor{

	@Override
	public void accept(FontColorAttribute fontColorAttr) {
		if(fontColorAttr != null) {
			this.attributes.add(fontColorAttr::apply);
		}
	}

	@Override
	public void accept(BackgroundColorAttribute backgroundColorAttr) {
		if(backgroundColorAttr != null) {
			this.attributes.add(backgroundColorAttr::apply);
		}
	}	
	
}
