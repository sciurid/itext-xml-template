package me.chenqiang.pdf.sax.composer;

import java.util.Collection;
import java.util.function.Consumer;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.DocumentComponent;
import me.chenqiang.pdf.sax.composer.component.PdfElementComposer;

public abstract class AreaBreakComposer implements PdfElementComposer<AreaBreak, AreaBreakComposer>, DocumentComponent {
	protected String id;
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer, DocumentContext context) {
		doc.add(this.produce(context));
	}

	@Override
	public Class<AreaBreak> getElementClass() {
		return AreaBreak.class;
	}

	@Override
	public void setAttribute(Consumer<? super AreaBreak> attribute) {
	}

	@Override
	public void setAllAttributes(Collection<? extends Consumer<? super AreaBreak>> attributes) {
		
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static AreaBreakComposer getNextPage() {
		return new AreaBreakComposer() {
			@Override
			public AreaBreak produce(DocumentContext context) {
				return new AreaBreak(AreaBreakType.NEXT_PAGE);
			}
		};
	}

	public static AreaBreakComposer getNextPage(PageSize ps) {
		return new AreaBreakComposer() {
			@Override
			public AreaBreak produce(DocumentContext context) {
				return new AreaBreak(ps);
			}
		};
	}
}
