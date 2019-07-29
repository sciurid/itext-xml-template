package me.chenqiang.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.TransparentColor;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import me.chenqiang.pdf.xml.context.ColorMap;

public class WatermarkMaker implements IEventHandler {
	@FunctionalInterface
	public static interface Renderer {
		public void render(PdfDocument pdfDoc, PdfPage page, PdfCanvas canvas);
	}

	protected List<Renderer> renderers = new ArrayList<>();

	public void addRenderer(Renderer renderer) {
		this.renderers.add(renderer);
	}

	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		PdfDocument pdfDoc = docEvent.getDocument();
		PdfPage page = docEvent.getPage();

		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
		for (Renderer renderer : this.renderers) {
			renderer.render(pdfDoc, page, pdfCanvas);
		}
		pdfCanvas.release();
	}

	public static class ImageWatermarkSetting {
		protected float width = 0f;
		protected float opacity = 1f;
		protected float offsetX = 0f;
		protected float offsetY = 0f;

		public void setWidth(float width) {
			this.width = width;
		}

		public void setOpacity(float opacity) {
			this.opacity = opacity;
		}

		public void setOffsetX(float offsetX) {
			this.offsetX = offsetX;
		}

		public void setOffsetY(float offsetY) {
			this.offsetY = offsetY;
		}
	}

	public static void renderImage(PdfPage page, PdfCanvas pdfCanvas, ImageData image,
			ImageWatermarkSetting setting) {
		pdfCanvas.saveState();
		Rectangle pageSize = page.getPageSize();
		pdfCanvas.concatMatrix(1, 0, 0, 1, pageSize.getWidth() / 2, pageSize.getHeight() / 2);
		if (setting != null) {
			PdfExtGState pegs = new PdfExtGState();
			pegs.setFillOpacity(setting.opacity);
			pdfCanvas.setExtGState(pegs);

			float posX = setting.offsetX - 1 * setting.width / 2;
			float posY = setting.offsetY - 1 * setting.width * image.getHeight() / (2 * image.getWidth());
			pdfCanvas.addImage(image, posX, posY, setting.width, false);
		} else {
			float posX = -1 * image.getWidth() / 2;
			float posY = -1 * image.getHeight() / 2;
			pdfCanvas.addImage(image, posX, posY, false);
		}
		pdfCanvas.restoreState();
	}

	public static class TextWatermarkSetting {
		protected PdfFont font;
		protected UnitValue fontSize = UnitValue.createPointValue(30f);
		protected float width = 0f;
		protected float offsetX = 0f;
		protected float offsetY = 0f;
		protected Color fontColor = ColorMap.getColor("gray");
		protected float opacity = 1f;
		protected TextAlignment textAlignment = TextAlignment.CENTER;
		protected VerticalAlignment verticalAlignment = VerticalAlignment.MIDDLE;
		protected float rotation = 0f;

		public TextWatermarkSetting() {
			try {
				this.font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD, PdfEncodings.UTF8, true, true);
			} catch (IOException e) {
				this.font = null;
			}
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public void setOffsetX(float offsetX) {
			this.offsetX = offsetX;
		}

		public void setOffsetY(float offsetY) {
			this.offsetY = offsetY;
		}

		public void setFont(PdfFont font) {
			this.font = font;
		}

		public void setFontSize(UnitValue fontSize) {
			this.fontSize = fontSize;
		}

		public void setFontColor(Color color) {
			this.fontColor = color;
		}
		
		public void setOpacity(float opacity) {
			this.opacity = opacity;
		}

		public void setTextAlignment(TextAlignment textAlignment) {
			this.textAlignment = textAlignment;
		}

		public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
		}

		public void setRotation(float rotation) {
			this.rotation = rotation;
		}
	}

	public static void renderText(PdfDocument pdfDoc, PdfPage page, PdfCanvas pdfCanvas, String text,
			TextWatermarkSetting twsetting) {
		pdfCanvas.saveState();
		Rectangle pageSize = page.getPageSize();
		pdfCanvas.concatMatrix(1, 0, 0, 1, pageSize.getWidth() / 2, pageSize.getHeight() / 2);

		Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pageSize);

		TextWatermarkSetting setting = twsetting == null ? new TextWatermarkSetting() : twsetting;

		canvas.setProperty(Property.FONT, setting.font);
		canvas.setProperty(Property.FONT_COLOR, new TransparentColor(setting.fontColor, 1.0f));
		canvas.setProperty(Property.FONT_SIZE, setting.fontSize);

		Paragraph para = new Paragraph(text);
		if (setting.width > 0) {
			para.setWidth(setting.width);
		}

		canvas.showTextAligned(para, setting.offsetX, setting.offsetY, pdfDoc.getPageNumber(page),
				setting.textAlignment, setting.verticalAlignment, setting.rotation);
		canvas.close();

		pdfCanvas.restoreState();
	}

	public static void renderPageColor(PdfDocument pdfDoc, PdfPage page, PdfCanvas pdfCanvas, Color[] colors) {
		if (colors == null || colors.length == 0) {
			return;
		}
		int pageNumber = pdfDoc.getPageNumber(page);
		Rectangle pageSize = page.getPageSize();

		pdfCanvas.saveState().setFillColor(colors[pageNumber % colors.length])
				.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight()).fill()
				.restoreState();
	}
	
	public WatermarkMaker deepCopy() {
		WatermarkMaker wmm = new WatermarkMaker();
		wmm.renderers.addAll(this.renderers);
		return wmm;
	}
}
