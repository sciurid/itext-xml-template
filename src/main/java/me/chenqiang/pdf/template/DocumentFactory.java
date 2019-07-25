package me.chenqiang.pdf.template;


import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

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
	
	public static void produce(DocumentTemplate template, OutputStream os, PaperLayout paper) throws IOException {
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, paper.ps);
		document.setMargins(paper.marginTop, paper.marginRight, paper.marginBottom, paper.marginLeft);
		template.process(document, pdf, writer);
		document.close();
		writer.close();
	}
	
	public static void produce(DocumentTemplate template, OutputStream os) throws IOException {
		produce(template, os, new PaperLayout());
	}
}
