package me.chenqiang.pdf.composer;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import me.chenqiang.pdf.component.DivComponent;
import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.ParagraphComponent;
import me.chenqiang.pdf.component.TableCellComponent;

public abstract class BasicImageComposer<S extends BasicImageComposer<S>>
		extends BasicElementComposer<Image, S>
		implements DocumentComponent, ParagraphComponent, TableCellComponent, DivComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicImageComposer.class);
	public static final String IMAGE_ERROR = "Error loading image.";
	protected ImageData imageData = null;
	
	protected BasicImageComposer() {
		super(Image.class);
	}
	
	protected BasicImageComposer(BasicImageComposer<S> origin) {
		super(origin);
	}

	public void setImageData(byte[] data) {
		this.imageData = ImageDataFactory.create(data);
	}

	@SuppressWarnings("unchecked")
	public S setImageData(InputStream is) {
		try {
			this.imageData = ImageDataFactory.create(is.readAllBytes());
		} catch (IOException e) {
			LOGGER.error(IMAGE_ERROR, e);
		}
		return (S) this;
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
	public void process(Div div) {
		Image image = this.<Void>produce(null);
		if(image != null) {
			div.add(image);
		}
	}
}
