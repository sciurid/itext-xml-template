package me.chenqiang.pdf.xml.context;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.property.UnitValue;

class StyleAttributeRegistryEntry<T> {
	protected Class<T> clazz;
	protected Map<String, StyleAttributeFactory<T>> factories;
	
	public StyleAttributeRegistryEntry(Class<T> clazz) {
		this.clazz = clazz;
		this.factories = new TreeMap<>();
	}
	
	public StyleAttributeRegistryEntry(Class<T> clazz, Map<String, StyleAttributeFactory<T>> factories) {
		this.clazz = clazz;
		this.factories = new TreeMap<>(factories);
	}
	
	public StyleAttributeRegistryEntry(StyleAttributeRegistryEntry<T> origin) {
		this(origin.clazz, origin.factories);
	}
	
	public void register(String name, StyleAttributeFactory<T> factory) {
		this.factories.put(name, factory);
	}
	
	public boolean isAcceptable(Class<?> elementClass) {
		return this.clazz.isAssignableFrom(elementClass);
	}
	
	public boolean isAcceptable(Object element) {
		return clazz.isInstance(element);
	}
	
	public Consumer<T> getAttribute(String name, String value) {
		if(this.factories.containsKey(name)) {
			StyleAttributeFactory<T> factory = this.factories.get(name);
			if(factory != null) {
				return factory.create(name, value);				
			}
		}
		return null;
	}
	
	public void registerBoolean(String name, BooleanFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				return element -> function.apply(element, new AttributeValueParser(name, value).getBoolean());
			}			
		});
	}
	
	public void registerFloat(String name, FloatFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				Float fval = new AttributeValueParser(name, value).getLength();
				return fval == null ? null : element -> function.apply(element, fval.floatValue());
			}			
		});
	}
	
	public void registerBiFloat(String name, BiFloatFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				float [] fvals = new AttributeValueParser(name, value).getLengthArray(2);
				return fvals == null ? null : element -> function.apply(element, fvals[0], fvals[1]);
			}			
		});
	}
	
	public void registerTriFloat(String name, TriFloatFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				float [] fvals = new AttributeValueParser(name, value).getLengthArray(3);
				return fvals == null ? null : element -> function.apply(element, fvals[0], fvals[1], fvals[2]);
			}			
		});
	}
	
	public void registerQuadFloat(String name, QuadFloatFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				float [] fvals = new AttributeValueParser(name, value).getLengthArray(4);
				return fvals == null ? null : element -> function.apply(element, fvals[0], fvals[1], fvals[2], fvals[3]);
			}			
		});
	}
	
	public void registerColor(String name, ColorFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				Color color = new AttributeValueParser(name, value).getDeviceRgb();
				return color == null ? null : element -> function.apply(element, color);
			}			
		});
	}
	
	public void registerUnitValue(String name, UnitValueFunction<T> function) {
		this.register(name, new StyleAttributeFactory<T>() {
			@Override
			public Consumer<T> create(String name, String value) {
				UnitValue uvval = new AttributeValueParser(name, value).getUnitValue();
				return uvval == null ? null : element -> function.apply(element, uvval);
			}			
		});
	}
}
