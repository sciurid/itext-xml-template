package me.chenqiang.pdf;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import me.chenqiang.pdf.common.utils.BeanUtilEvaluator;

public class DocumentContext {
	public static class Scope {
		protected final Map<String, Object> parameters;
		protected Scope() {
			this.parameters = new TreeMap<>();
		}
		protected Scope(Map<String, Object> parameters) {
			if(parameters != null) {
				this.parameters = new TreeMap<>(parameters);
			}
			else {
				this.parameters = new TreeMap<>();
			}
		}
		
		public void setParameter(String key, Object value) {
			this.parameters.put(key, value);
		}
		
		public Map<String, Object> getParameters() {
			return parameters;
		}		
	}
	
	protected final LinkedList<Scope> scopes;
	
	protected DocumentContext(Scope scope) {
		this.scopes = new LinkedList<>();
		this.scopes.addFirst(scope);
	}
	public DocumentContext(Map<String, Object> parameters) {
		this(new Scope(parameters));
	}
	
	public Scope beginScope() {
		this.scopes.addFirst(new Scope());
		return this.scopes.getFirst();
	}
	
	public void endScope() {
		this.scopes.removeFirst();
	}
	
	public Scope getCurrentScope() {
		return this.scopes.getFirst();
	}
	
	public void setParameter(String key, Object value) {
		this.getCurrentScope().setParameter(key, value);
	}

	public String eval(String text) {		
		String res = null;
		for(Scope scope : this.scopes) {
			 res = BeanUtilEvaluator.evaluate(scope.getParameters(), text);
			 if(res != null) {
				 return res;
			 }
		}
		return res;		
	}

	public Object getProperty(String text) {
		Object res = null;
		for(Scope scope : this.scopes) {
			 res = BeanUtilEvaluator.getProperty(scope.getParameters(), text);
			 if(res != null) {
				 return res;
			 }
		}
		return res;
	}
}
