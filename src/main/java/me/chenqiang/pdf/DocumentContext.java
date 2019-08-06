package me.chenqiang.pdf;

import java.util.Map;
import java.util.TreeMap;

public class DocumentContext {
	protected final Map<String, Object> parameters;
	protected final ParameterEvaluator evaluator;

	public DocumentContext(Map<String, Object> parameters, ParameterEvaluator evaluator) {
		this.parameters = parameters;
		this.evaluator = evaluator;
	}

	public DocumentContext(Map<String, Object> parameters) {
		this(parameters, new BeanUtilEvaluator());
	}

	public DocumentContext(ParameterEvaluator evaluator) {
		this(new TreeMap<>(), evaluator);
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public String eval(String text) {
		if(this.evaluator != null && this.parameters != null) {
			return this.evaluator.evaluate(this.parameters, text);
		}
		else {
			return text;
		}
		
	}

	public Object getProperty(String text) {
		if(this.evaluator != null && this.parameters != null) {
			return this.evaluator.getProperty(this.parameters, text);
		}
		else {
			return null;
		}
	}

}
