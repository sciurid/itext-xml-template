package me.chenqiang.pdf;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

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
	protected final ParameterEvaluator evaluator;
	
	public DocumentContext(Scope scope, ParameterEvaluator evaluator) {
		this.scopes = new LinkedList<>();
		this.scopes.addFirst(scope);
		this.evaluator = evaluator;
	}
	public DocumentContext(Map<String, Object> parameters, ParameterEvaluator evaluator) {
		this(new Scope(parameters), evaluator);
	}

	public DocumentContext(Map<String, Object> parameters) {
		this(parameters, new BeanUtilEvaluator());
	}
	
	public Scope beginScope() {
		this.scopes.addFirst(new Scope());
		return this.scopes.getFirst();
	}
	
	public void endScope() {
		this.scopes.removeFirst();
	}

	public String eval(String text) {		
		if(this.evaluator != null) {
			String res = null;
			for(Scope scope : this.scopes) {
				 res = this.evaluator.evaluate(scope.getParameters(), text);
				 if(res != null) {
					 return res;
				 }
			}
			return res;
		}
		else {
			return text;
		}
		
	}

	public Object getProperty(String text) {
		if(this.evaluator != null) {
			Object res = null;
			for(Scope scope : this.scopes) {
				 res = this.evaluator.getProperty(scope.getParameters(), text);
				 if(res != null) {
					 return res;
				 }
			}
			return res;
		}
		else {
			return null;
		}
	}	
}
