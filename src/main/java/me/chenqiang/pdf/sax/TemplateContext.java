package me.chenqiang.pdf.sax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.Style;

import me.chenqiang.pdf.common.attribute.AttributeRegistry;
import me.chenqiang.pdf.font.FontRegistry;
import me.chenqiang.pdf.font.FontRegistryEntry;

public class TemplateContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateContext.class);
	protected Map<String, Style> styles;
	protected Map<String, byte []> images;
	protected FontRegistry fontreg;
	protected AttributeRegistry attributes;
	protected LinkedList<Scope> scopeStack;
	
	public TemplateContext() {
		this.fontreg = new FontRegistry();	
		this.fontreg.initialize();
		this.styles = new TreeMap<>();
		this.images = new TreeMap<>();
		this.attributes = new AttributeRegistry(this.fontreg);
		this.scopeStack = new LinkedList<>();
		this.beginScope();
	}

	public AttributeRegistry getAttributeRegistry() {
		return attributes;
	}
	
	public byte [] getImage(String name) {
		return this.images.get(name);
	}
	
	public void registerImage(String name, byte [] image) {
		this.images.put(name, image);
	}

	public FontRegistry getFontreg() {
		return fontreg;
	}

	public FontRegistryEntry getFont(String name) {
		return this.fontreg.apply(name);
	}
	
	public Scope beginScope() {
		Scope scope = new Scope();
		this.scopeStack.addFirst(scope);
		LOGGER.debug("Enter new scope, stack depth: {}", this.scopeStack.size());
		return scope;
	}
	
	public Scope endScope() {
		Scope scope = this.scopeStack.removeFirst();
		LOGGER.debug("Leave scope, stack depth: {}", this.scopeStack.size());
		return scope;
	}
	
	public Map<String, List<String []>> getStyleMap() {
		Map<String, List<String []>> res = new TreeMap<>();
		this.scopeStack.forEach(scope -> {
			scope.styleMap.forEach(res::putIfAbsent);
		});
		return res;
	}
	
	public void registerStyle(String id, List<String []> style) {
		this.scopeStack.getFirst().registerStyle(id, style);
	}
	
	public List<String []> getPredefinedStyle(String id) {
		List<String []> res = new ArrayList<>();
		for(Scope scope : this.scopeStack) {
			res = scope.getStyle(id);
			if(res != null) {
				break;
			}
		}
		return res;
	}
	
	public void setDefaultParagraphStyle(List<String []> defaultParagraphStyle) {
		this.scopeStack.getFirst().setDefaultParagraphStyle(defaultParagraphStyle);
	}
	
	public List<String []> getDefaultParagraphStyle() {
		for(Scope scope : this.scopeStack) {
			if(scope.defaultParagraphStyle != null) {
				return scope.defaultParagraphStyle;
			}
		}
		return null;
	}
	
		
	public static class Scope {
		protected List<String []> defaultParagraphStyle = null;
		protected Map<String, List<String []>> styleMap = new TreeMap<>();
		protected List<String[]> getDefaultParagraphStyle() {
			return this.defaultParagraphStyle;
		}
		protected void setDefaultParagraphStyle(List<String[]> defaultParagraphStyle) {
			this.defaultParagraphStyle = defaultParagraphStyle;
		}
		protected void registerStyle(String id, List<String []> style) {
			this.styleMap.putIfAbsent(id, style);
		}
		protected List<String []> getStyle(String id) {
			return this.styleMap.get(id);
		}
	}
}
