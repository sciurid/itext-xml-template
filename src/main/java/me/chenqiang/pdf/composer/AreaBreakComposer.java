package me.chenqiang.pdf.composer;

import java.util.Collection;
import java.util.function.Consumer;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;

import me.chenqiang.pdf.component.DocumentComponent;
import me.chenqiang.pdf.component.PdfElementComposer;

public abstract class AreaBreakComposer implements PdfElementComposer<AreaBreak, AreaBreakComposer>, DocumentComponent {
	protected String id;
	@Override
	public void process(Document doc, PdfDocument pdf, PdfWriter writer) {
		doc.add(this.<Void>produce(null));
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

	@Override
	public AreaBreakComposer copy() {
		return this;
	}
	
	public static AreaBreakComposer getNextPage() {
		return new AreaBreakComposer() {
			@Override
			public <C> AreaBreak produce(C context) {
				return new AreaBreak(AreaBreakType.NEXT_PAGE);
			}
		};
	}

	public static AreaBreakComposer getNextPage(PageSize ps) {
		return new AreaBreakComposer() {
			@Override
			public <C> AreaBreak produce(C context) {
				return new AreaBreak(ps);
			}
		};
	}
}
