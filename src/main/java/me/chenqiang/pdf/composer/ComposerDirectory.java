package me.chenqiang.pdf.composer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.component.DataParameterPlaceholder;
import me.chenqiang.pdf.component.StringParameterPlaceholder;

@Deprecated
public class ComposerDirectory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ComposerDirectory.class);
	protected final Map<String, LinkedList<Object>> mapIdentifiable;
	protected final Map<String, LinkedList<StringParameterPlaceholder>> mapStringPlaceholder;
	protected final Map<String, LinkedList<DataParameterPlaceholder>> mapDataPlaceholder;
	
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
	
	public void registerStringPlaceholder(String id, StringParameterPlaceholder placeholder) {
		this.mapStringPlaceholder.computeIfAbsent(id, k -> new LinkedList<>()).add(placeholder);
	}
	
	public List<StringParameterPlaceholder> getStringPlaceholders(String id) {
		LinkedList<StringParameterPlaceholder> list = this.mapStringPlaceholder.get(id);
		return list == null ? null : Collections.unmodifiableList(list);
	}
	
	public void registerDataPlaceholder(String id, DataParameterPlaceholder placeholder) {
		this.mapDataPlaceholder.computeIfAbsent(id, k -> new LinkedList<>()).add(placeholder);
	}
	
	public List<DataParameterPlaceholder> getDataPlaceholders(String id) {
		LinkedList<DataParameterPlaceholder> list = this.mapDataPlaceholder.get(id);
		return list == null ? null : Collections.unmodifiableList(list);
	}
	
	protected Supplier<Paragraph> defaultParagraph;

	public Supplier<Paragraph> getDefaultParagraph() {
		return defaultParagraph;
	}

	public void setDefaultParagraph(Supplier<Paragraph> defaultParagrah) {
		this.defaultParagraph = defaultParagrah;
	}
	
	
}
