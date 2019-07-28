package me.chenqiang.pdf.xml.handler;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageDataFactory;

import me.chenqiang.pdf.xml.ResourceRepository;

public class ImageDefinitionHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDefinitionHandler.class);
	protected ResourceRepository repo;
	protected int count;

	public ImageDefinitionHandler(ResourceRepository repo) {
		this.repo = repo;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		// Do nothing.
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();

		String id = current.attributeValue("id");
		if (id == null) {
			LOGGER.error("Image resource without id: {} - {}", elementPath.getPath(), this.count);
			return;
		}
		
		String file = current.attributeValue("file");
		if(file != null) {
			try {
				this.repo.registerImage(id, ImageDataFactory.create(file));
			}
			catch(IOException ioe) {
				LOGGER.error("Image resource (file) error: {} - {} with file {}", elementPath.getPath(), this.count, file);
				LOGGER.error("Image resource (file) error:", ioe);
				return;
			}
		}
		
		String resource = current.attributeValue("resource");
		if(resource != null) {
			try {
				InputStream is = ImageDefinitionHandler.class.getResourceAsStream(resource);
				if (is != null) {
					this.repo.registerImage(id, ImageDataFactory.create(is.readAllBytes()));
				} else {
					LOGGER.error("Image resource (resource) not found: {} - {}", elementPath.getPath(), this.count);
					return;
				}
			} catch (IOException ioe) {
				LOGGER.error("Image resource (resource) error: {} - {}", elementPath.getPath(), this.count);
				LOGGER.error("Image resource (resource) error:", ioe);
				return;
			}
		}
		
		this.count++;
	}

}
