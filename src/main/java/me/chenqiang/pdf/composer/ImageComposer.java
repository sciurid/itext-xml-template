package me.chenqiang.pdf.composer;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

public class ImageComposer extends BasicImageComposer<ImageComposer>
implements ParameterPlaceholder<byte []>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageComposer.class);
	
	@Override
	public void setParameter(byte [] parameter) {
		this.setImageData(parameter);
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

	public ImageComposer setImageFile(String filepath) {
		try {
			this.imageData = ImageDataFactory.create(filepath);
		} catch (MalformedURLException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
		return this;
	}

	public ImageComposer setImageResource(String resource) {
		this.setImageData(ImageComposer.class.getResourceAsStream(resource));
		return this;
	}
	
	@Override
	protected Image create() {
		if(this.imageData == null) {
			return null;
		}
		else {
			return new Image(this.imageData);
		}
	}
}
