package me.chenqiang.pdf.attribute;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.ElementPropertyContainer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.RoundDotsBorder;
import com.itextpdf.layout.borders.SolidBorder;

public class BorderAttribute {
	private static final Logger LOGGER = LoggerFactory.getLogger(BorderAttribute.class);
	public static final String TYPE_NONE = "none";
	public static final String TYPE_SOLID = "solid";
	public static final String TYPE_DASHED = "dashed";
	public static final String TYPE_DOTTED = "dotted";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_ROUNDDOTS = "rounddots";
	protected String type = TYPE_SOLID;
	protected Float width = 0.5f;
	protected Color color = null;
	protected Float opacity = null;
	
	public void setType(String type) {
		this.type = type;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	@FunctionalInterface
	static interface TriFunction<T, U, S, R> {
		R apply(T t, U u, S s);
	}
	
	protected static final Map<String, Function<Float, Border>> CREATORS_1 = Map.ofEntries(
			Map.entry(TYPE_SOLID, SolidBorder::new),
			Map.entry(TYPE_DASHED, DashedBorder::new),
			Map.entry(TYPE_DOTTED, DottedBorder::new),
			Map.entry(TYPE_DOUBLE, DoubleBorder::new),
			Map.entry(TYPE_ROUNDDOTS, RoundDotsBorder::new)
			);
	protected static final Map<String, BiFunction<Color, Float, Border>> CREATORS_2 = Map.ofEntries(
			Map.entry(TYPE_SOLID, SolidBorder::new),
			Map.entry(TYPE_DASHED, DashedBorder::new),
			Map.entry(TYPE_DOTTED, DottedBorder::new),
			Map.entry(TYPE_DOUBLE, DoubleBorder::new),
			Map.entry(TYPE_ROUNDDOTS, RoundDotsBorder::new)
			);
	protected static final Map<String, TriFunction<Color, Float, Float, Border>> CREATORS_3 = Map.ofEntries(
			Map.entry(TYPE_SOLID, SolidBorder::new),
			Map.entry(TYPE_DASHED, DashedBorder::new),
			Map.entry(TYPE_DOTTED, DottedBorder::new),
			Map.entry(TYPE_DOUBLE, DoubleBorder::new),
			Map.entry(TYPE_ROUNDDOTS, RoundDotsBorder::new)
			);
	
	public <T extends ElementPropertyContainer<T>> Consumer<T> createAttribute(BiConsumer<T, Border> setter) {
		final Border border;
		if(TYPE_NONE.equals(this.type)) {
			border = Border.NO_BORDER;
		}
		else {
			if(!CREATORS_1.containsKey(this.type) || !CREATORS_2.containsKey(this.type) || !CREATORS_3.containsKey(this.type)) {
				LOGGER.error("Border type '{}' is not accepted", this.type);
				border = null;
			}
			else if(this.width != null) {
				if(this.color != null) {
					if(this.opacity != null) {
						border = CREATORS_3.get(this.type).apply(color, width, opacity);
					}
					else {
						border = CREATORS_2.get(this.type).apply(color, width);
					}
				}
				else {
					border = CREATORS_1.get(this.type).apply(width);
				}
			}
			else {
				border = null;
			}
		}
		if(border != null) {
			return element -> setter.accept(element, border);
		}
		else {
			return null;
		}
	}
	
	public static interface Acceptor {
		public void accept(BorderAttribute common, BorderAttribute top, BorderAttribute right, BorderAttribute bottom, BorderAttribute left);
	}
}
