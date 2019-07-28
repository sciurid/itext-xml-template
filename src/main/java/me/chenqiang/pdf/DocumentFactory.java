package me.chenqiang.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.pdfa.PdfADocument;

import me.chenqiang.pdf.composer.DocumentComposer;

public class DocumentFactory {
	private DocumentFactory() {
	}

	public static float mm2pt(float mm) {
		return 72 * mm / 25.4f;
	}

	public static class PaperLayout {
		protected PageSize ps;
		protected float marginLeft;
		protected float marginRight;
		protected float marginTop;
		protected float marginBottom;

		public PaperLayout() {
			this(PageSize.A4, 72, 72, 72, 72);
		}

		public PaperLayout(PageSize ps, float marginLeft, float marginRight, float marginTop, float marginBottom) {
			super();
			this.ps = ps == null ? PageSize.A4 : ps;
			this.marginLeft = marginLeft;
			this.marginRight = marginRight;
			this.marginTop = marginTop;
			this.marginBottom = marginBottom;
		}
	}

	public static void produce(DocumentComposer template, OutputStream os, PaperLayout paper) throws IOException {
		Map<String, String> params = Map.ofEntries(
				Map.entry("占位", "替换结果"),
				Map.entry("B1", "变量B1")
				);
		template.substitute(params);
		
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A, 
				new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", 
						DocumentFactory.class.getResourceAsStream("/sRGB_CS_profile.icm")));
		pdf.setTagged();
//		PdfDocument pdf = new PdfDocument(writer);
		
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new Watermark());
		Document document = new Document(pdf, paper.ps);
		document.setMargins(paper.marginTop, paper.marginRight, paper.marginBottom, paper.marginLeft);
		template.process(document, pdf, writer);
		document.close();
		writer.close();
	}

	public static void produce(DocumentComposer template, OutputStream os) throws IOException {
		produce(template, os, new PaperLayout());
	}
	
	
	public static class Watermark implements IEventHandler {

		@Override
		public void handleEvent(Event event) {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
	        PdfDocument pdfDoc = docEvent.getDocument();
	        PdfPage page = docEvent.getPage();
	        int pageNumber = pdfDoc.getPageNumber(page);
	        Rectangle pageSize = page.getPageSize();
	        PdfCanvas pdfCanvas = new PdfCanvas(
	            page.newContentStreamBefore(), page.getResources(), pdfDoc);
	 
	        //Set background
//	        Color limeColor = new DeviceCmyk(0.208f, 0, 0.584f, 0);
//	        Color blueColor = new DeviceCmyk(0.445f, 0.0546f, 0, 0.0667f);
//	        pdfCanvas.saveState()
//	                .setFillColor(pageNumber % 2 == 1 ? limeColor : blueColor)
//	                .rectangle(pageSize.getLeft(), pageSize.getBottom(),
//	                    pageSize.getWidth(), pageSize.getHeight())
//	                .fill().restoreState();
	        
//	        Color limeColor = DeviceRgb.GREEN;
//	        Color blueColor = DeviceRgb.BLUE;
//	        pdfCanvas.saveState()
//	                .setFillColor(pageNumber % 2 == 1 ? limeColor : blueColor)
//	                .rectangle(pageSize.getLeft(), pageSize.getBottom(),
//	                    pageSize.getWidth(), pageSize.getHeight())
//	                .fill().restoreState();
//	        
//	        //Add header and footer
//	        pdfCanvas.beginText()
//	                .setFontAndSize(helvetica, 9)
//	                .moveText(pageSize.getWidth() / 2 - 60, pageSize.getTop() - 20)
//	                .showText("THE TRUTH IS OUT THERE")
//	                .moveText(60, -pageSize.getTop() + 30)
//	                .showText(String.valueOf(pageNumber))
//	                .endText();
//	        //Add watermark
//	        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
//	        canvas.setProperty(Property.FONT_COLOR, Color.WHITE);
//	        canvas.setProperty(Property.FONT_SIZE, 60);
//	        canvas.setProperty(Property.FONT, helveticaBold);
//	        canvas.showTextAligned(new Paragraph("CONFIDENTIAL"),
//	            298, 421, pdfDoc.getPageNumber(page),
//	            TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);
	        
			try {
				ImageData image = ImageDataFactory.create(Watermark.class.getResourceAsStream("/hawk.png").readAllBytes());
				float ratio = image.getHeight() / image.getWidth();
				PdfExtGState pegs = new PdfExtGState();
				pegs.setFillOpacity(0.2f);
		        pdfCanvas.saveState()
		        .setExtGState(pegs)
		        .concatMatrix(1, 0, 0, 1, pageSize.getWidth() / 2, pageSize.getHeight() / 2);
		        pdfCanvas.addImage(image, mm2pt(-50), mm2pt(-50) * ratio, mm2pt(100), false);
		        pdfCanvas.restoreState();
			} catch (IOException e) {
			}	        
	 
	        pdfCanvas.release();
		}
		
	}
}
