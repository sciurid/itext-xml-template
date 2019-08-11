package me.chenqiang.pdf.xml.context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateContext.class);
	protected ResourceRepository resourceRepository;
	protected AttributeRegistry attributeRegistry;
	protected LinkedList<Scope> scopeStack;
	
	public TemplateContext() {
		this.resourceRepository = new ResourceRepository();
		this.attributeRegistry = new AttributeRegistry(this.resourceRepository);
		this.scopeStack = new LinkedList<>();
		this.beginScope();
	}

	public ResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public AttributeRegistry getAttributeRegistry() {
		return attributeRegistry;
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
		List<String []> res = null;
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
