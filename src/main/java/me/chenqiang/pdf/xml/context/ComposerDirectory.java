package me.chenqiang.pdf.xml.context;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.composer.ParameterPlaceholder;

public class ComposerDirectory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ComposerDirectory.class);
	protected final Map<String, LinkedList<Object>> mapIdentifiable;
	protected final Map<String, LinkedList<ParameterPlaceholder<String>>> mapStringPlaceholder;
	protected final Map<String, LinkedList<ParameterPlaceholder<byte []>>> mapDataPlaceholder;
	
	public ComposerDirectory() {
		this.mapIdentifiable = new TreeMap<>();
		this.mapStringPlaceholder = new TreeMap<>();
		this.mapDataPlaceholder = new TreeMap<>();
	}

	public void registerIdentifiable(String id, Object composer) {
		this.mapIdentifiable.computeIfAbsent(id, k -> new LinkedList<>()).add(composer);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getIdentifiable(String id) {
		if(this.mapIdentifiable.containsKey(id)) {
			LinkedList<Object> list = this.mapIdentifiable.get(id);
			if(list.isEmpty()) {
				return null;
			}
			else if(list.size() == 1) {
				return (T)list.getFirst();
			}
			else {
				LOGGER.warn("Multiple ({}) values exist. ONLY the first one is returned.", list.size());
				return (T)list.getFirst();
			}
		}
		else {
			return null;
		}
	}
	
	public List<Object> getIdentifiableList(String id) {
		return Collections.unmodifiableList(this.mapIdentifiable.get(id));
	}
	
	public void registerStringPlaceholder(String id, ParameterPlaceholder<String> placeholder) {
		this.mapStringPlaceholder.computeIfAbsent(id, k -> new LinkedList<>()).add(placeholder);
	}
	
	public List<ParameterPlaceholder<String>> getStringPlaceholders(String id) {
		LinkedList<ParameterPlaceholder<String>> list = this.mapStringPlaceholder.get(id);
		return list == null ? null : Collections.unmodifiableList(list);
	}
	
	public void registerDataPlaceholder(String id, ParameterPlaceholder<byte []> placeholder) {
		this.mapDataPlaceholder.computeIfAbsent(id, k -> new LinkedList<>()).add(placeholder);
	}
	
	public List<ParameterPlaceholder<byte []>> getDataPlaceholders(String id) {
		LinkedList<ParameterPlaceholder<byte[]>> list = this.mapDataPlaceholder.get(id);
		return list == null ? null : Collections.unmodifiableList(list);
	}
	
}
