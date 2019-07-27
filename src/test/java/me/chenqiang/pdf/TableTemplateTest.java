package me.chenqiang.pdf;

import java.io.IOException;

import org.junit.Test;

import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.template.element.ParagraphTemplate;
import me.chenqiang.pdf.template.element.StringTemplate;
import me.chenqiang.pdf.template.table.CellTemplate;
import me.chenqiang.pdf.template.table.TableTemplate;
import me.chenqiang.pdf.template.table.TableTemplate.Row;

public class TableTemplateTest extends PdfTest{

	@Test
	public void testTableTemplate() throws IOException {
		FontProvider fp = new FontProvider();
		fp.addSystemFonts();
		
		DocumentTemplate template = new DocumentTemplate();
		template
		.set(doc -> doc.setFontProvider(fp))
		.set(doc -> doc.setFontSize(12))
		;
		
		
		TableTemplate tbl = new TableTemplate(UnitValue.createPercentArray(new float[] {20f, 30f, 50f}));
		tbl.set(table -> table.setFontFamily("stkaiti"))
		.set(table -> table.setWidth(UnitValue.createPercentValue(100)));
		
		Row header = tbl.getHeader();
		header.set(cell -> cell.setFontFamily("simhei"));
		header.add(new ParagraphTemplate().append(new StringTemplate("A 甲")));
		header.add(new StringTemplate("B 乙"));
		header.add(new StringTemplate("C 丙"));
		
		Row content = tbl.getContent();
		content.set(cell -> cell.setFontFamily("stsong"));
		content.add(
				new CellTemplate().setRowspan(2).setColspan(2).append(
						new ParagraphTemplate().append(new StringTemplate("A1 甲壹"))));
		content.add(new StringTemplate("C1 丙壹"));
		content.add(new StringTemplate("C3 丙叁"));
		content.add(new StringTemplate("A3 甲叁"));

		content.add(
				new CellTemplate().setColspan(2).append(
						new ParagraphTemplate().append(new StringTemplate("B3 is a very long line. 乙叁-横贰"))));
		
		template.append(tbl);
		this.render(template, "Table-");
	}
}
