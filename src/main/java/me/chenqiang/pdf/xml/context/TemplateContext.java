package me.chenqiang.pdf.xml.context;

public class TemplateContext {
	protected ResourceRepository resourceRepository;
	protected AttributeRegistry attributeRegistry;
	
	public TemplateContext() {
		this.resourceRepository = new ResourceRepository();
		this.attributeRegistry = new AttributeRegistry(this.resourceRepository);
	}

	public ResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public AttributeRegistry getAttributeRegistry() {
		return attributeRegistry;
	}
}
