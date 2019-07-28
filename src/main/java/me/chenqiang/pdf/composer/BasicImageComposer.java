package me.chenqiang.pdf.composer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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

import me.chenqiang.pdf.composer.DocumentComposer.DocumentComponent;
import me.chenqiang.pdf.composer.ParagraphComposer.ParagraphComponent;
import me.chenqiang.pdf.composer.TableCellComposer.TableCellComponent;

public abstract class BasicImageComposer<T extends BasicImageComposer<T>>
		extends BasicElementComposer<Image, BasicImageComposer<T>>
		implements DocumentComponent, ParagraphComponent, TableCellComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicImageComposer.class);
	public static final String IMAGE_ERROR = "Error loading image.";
	protected ImageData imageData = null;

	public void setImageData(byte[] data) {
		this.imageData = ImageDataFactory.create(data);
	}

	@SuppressWarnings("unchecked")
	public T setImageData(InputStream is) {
		try {
			this.imageData = ImageDataFactory.create(is.readAllBytes());
		} catch (IOException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setImageDataBase64(String base64) {
		this.setImageData(Base64.decodeBase64(base64));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setImageDataHex(String base64) {
		try {
			this.setImageData(Hex.decodeHex(base64.toCharArray()));
		} catch (DecoderException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
		return (T) this;
	}

	@Override
	protected Image create() {
		if (this.imageData == null) {
			return null;
		} else {
			return new Image(this.imageData);
		}
	}

	@Override
	public void process(Cell cell) {
		Image image = this.<Void>produce(null);
		if(image != null) {
			cell.add(image);
		}
	}

	@Override
	public void process(Paragraph para) {
		Image image = this.<Void>produce(null);
		if(image != null) {
			para.add(image);
		}
	}

	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		Image image = this.<Void>produce(null);
		if(image != null) {
			doc.add(image);
		}
	}

	@Override
	public void substitute(Map<String, String> params) {
		// Do nothing.
	}
}
