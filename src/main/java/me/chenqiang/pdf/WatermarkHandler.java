package me.chenqiang.pdf;

import java.io.IOException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

import me.chenqiang.pdf.DocumentFactory.Watermark;
import me.chenqiang.pdf.utils.LengthUnit;

public class WatermarkHandler extends PdfDocumentEventHandler {

	@Override
	protected void handleEvent(PdfDocumentEvent event, PdfDocument pdfDoc, PdfPage page) {
		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

		try {
			ImageData image = ImageDataFactory.create(Watermark.class.getResourceAsStream("/hawk.png").readAllBytes());
			this.renderImage(page, pdfCanvas, image, LengthUnit.cm2pt(8), 0.3f);
		} catch (IOException e) {
		}

		pdfCanvas.release();
	}
	
	protected void renderImage(PdfPage page, PdfCanvas pdfCanvas, ImageData image, float width, float opacity) {
		this.renderImage(page, pdfCanvas, image, width, opacity, 0, 0);
	}
	
	protected void renderImage(PdfPage page, PdfCanvas pdfCanvas, ImageData image, float width, float opacity, float offsetX, float offsetY) {
		PdfExtGState pegs = new PdfExtGState();
		pegs.setFillOpacity(opacity);
		
		Rectangle pageSize = page.getPageSize();		
		pdfCanvas.saveState().setExtGState(pegs)
		.concatMatrix(1, 0, 0, 1, pageSize.getWidth() / 2, pageSize.getHeight() / 2);
		
		float posX = offsetX - 1 * width / 2;
		float posY = offsetY - 1 * width * image.getHeight() / (2 * image.getWidth()); 
		pdfCanvas.addImage(image, posX, posY, width, false);
		pdfCanvas.restoreState();
	}

}
