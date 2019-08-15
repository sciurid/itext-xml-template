package me.chenqiang.pdf.sax.composer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.DocumentContext;

public final class ImageComposer extends BasicImageComposer<ImageComposer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageComposer.class);
	public static final String IMAGE_ERROR = "IMAGE DATA LOADING ERROR.";
	protected ImageData imageData = null;
	protected String value = null;

	
	
	public void setValue(String value) {
		this.value = value;
	}

	public void loadFromBytes(byte[] data) {
		this.imageData = ImageDataFactory.create(data);
	}

	public void loadFromInputStream(InputStream is) {
		try {
			this.imageData = ImageDataFactory.create(is.readAllBytes());
		} catch (IOException e) {
			LOGGER.error(IMAGE_ERROR, e);
			this.setImageData((ImageData)null);
		}
	}

	public void loadFromFile(String filepath) {
		try {
			this.imageData = ImageDataFactory.create(filepath);
		} catch (MalformedURLException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
	}
	
	public void loadFromResource(String resource) {
		this.loadFromInputStream(ImageComposer.class.getResourceAsStream(resource));
	}
	
	public void setImageData(ImageData imageData) {
		if(this.imageData != null) {
			if(imageData == null) {
				LOGGER.warn("Existing image is to be removed.");
			}
			else {
				LOGGER.warn("Existing image is to be replaced.");
			}
		}		
		this.imageData = imageData;
	}

	@Override
	protected Image create(DocumentContext context) {
		if(context != null && this.value != null) {
			byte [] data = (byte [])context.getProperty(this.value);
			if(data == null) {
				return null;
			}
			else {
				return new Image(ImageDataFactory.create(data));
			}			
		}
		else {
			if(this.imageData == null) {
				return null;
			}
			else {
				return new Image(this.imageData);
			}
		}
	}
	
	
}
