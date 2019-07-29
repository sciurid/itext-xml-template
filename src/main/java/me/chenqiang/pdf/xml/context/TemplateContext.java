package me.chenqiang.pdf.xml.context;

public class TemplateContext {
	protected ResourceRepository resourceRepository;
	protected AttributeRegistry attributeRegistry;
	protected ComposerDirectory composerDirectory;
	
	public TemplateContext() {
		this.resourceRepository = new ResourceRepository();
		this.attributeRegistry = new AttributeRegistry(this.resourceRepository);
		this.composerDirectory = new ComposerDirectory();
	}

	public ResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public AttributeRegistry getAttributeRegistry() {
		return attributeRegistry;
	}

	public ComposerDirectory getComposerDirectory() {
		return composerDirectory;
	}
	
	
}
