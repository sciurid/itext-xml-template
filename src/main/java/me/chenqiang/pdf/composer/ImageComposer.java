package me.chenqiang.pdf.composer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

public class ImageComposer extends BasicElementComposer<Image, ImageComposer>
implements DocumentComponent, ParagraphComponent, CellComponent{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageComposer.class);
	public static final String IMAGE_ERROR = "Error loading image.";
	protected ImageData imageData = null;

	public void setImageData(byte [] data) {
		this.imageData = ImageDataFactory.create(data);
	}
	
	public ImageComposer setImageData(InputStream is) {
		try {
			this.imageData = ImageDataFactory.create(is.readAllBytes());
		} catch (IOException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
		return this;
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
	
	public ImageComposer setImageDataBase64(String base64) {
		this.setImageData(Base64.decodeBase64(base64));
		return this;
	}
	
	public ImageComposer setImageDataHex(String base64) {
		try {
			this.setImageData(Hex.decodeHex(base64.toCharArray()));
		} catch (DecoderException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
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

	@Override
	public void process(Cell cell) {
		cell.add(this.<Void>produce(null));
	}

	@Override
	public void process(Paragraph para) {
		para.add(this.<Void>produce(null));
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		doc.add(this.<Void>produce(null));
	}
}
