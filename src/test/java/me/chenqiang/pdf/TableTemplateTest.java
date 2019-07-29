package me.chenqiang.pdf;

import java.io.IOException;

import org.junit.Test;

import com.google.zxing.WriterException;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.UnitValue;

import me.chenqiang.pdf.composer.BarcodeComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ImageComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.composer.TableComposer;
import me.chenqiang.pdf.composer.TableRowComposer;

public class TableTemplateTest extends PdfTest {

	@Test
	public void testTableTemplate() throws IOException, WriterException {
		FontProvider fp = new FontProvider();
		fp.addSystemFonts();

		DocumentComposer template = new DocumentComposer();
		template.setAttribute(doc -> doc.setFontProvider(fp)).setAttribute(doc -> doc.setFontSize(12));

		TableComposer tbl = new TableComposer();
		tbl.setColumns(UnitValue.createPercentArray(new float[] { 20f, 30f, 50f }));
		tbl.set(table -> table.setFontFamily("stkaiti"))
				.set(table -> table.setWidth(UnitValue.createPercentValue(100)));

		TableRowComposer header = tbl.getHeader();
		header.set(cell -> cell.setFontFamily("simhei"));
		header.add(new ParagraphComposer().append(new StringComposer("A 甲")));
		header.add(new StringComposer("B 乙"));
		header.add(new StringComposer("C 丙"));

		TableRowComposer content = tbl.getBody();
		content.set(cell -> cell.setFontFamily("stsong"));
		content.add(new TableCellComposer().setRowspan(2).setColspan(2)
				.append(new ParagraphComposer().append(new StringComposer("A1 甲壹"))));
		content.add(new StringComposer("C1 丙壹"));
		content.add(new StringComposer("C3 丙叁"));
		content.add(new StringComposer("A3 甲叁"));

		content.add(new TableCellComposer().setColspan(2)
				.append(new ParagraphComposer().append(new StringComposer("B3 is a very long line. 乙叁-横贰"))));

		;
		;
		content.add(
				new TableCellComposer().setColspan(3)
				.append(new ImageComposer()
						.setImageResource("/books.png")
						.set(image -> image.setWidth(UnitValue.createPercentValue(100f)))
						)
				.append(new BarcodeComposer().setMessage("https://www.tsinghua.edu.cn").setFormat("qrcode"))
				.append(new BarcodeComposer().setMessage("https://www.tsinghua.edu.cn").setFormat("pdf417"))
				);
		template.append(tbl);
		this.render(template, "Table-");
	}
}
