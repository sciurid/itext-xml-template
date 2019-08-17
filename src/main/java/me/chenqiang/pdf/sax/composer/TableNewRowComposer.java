package me.chenqiang.pdf.sax.composer;

import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.sax.composer.component.TableComponent;

// 并未实际使用
public class TableNewRowComposer implements TableComponent {

	@Override
	public void process(Table tbl, DocumentContext context) {
		tbl.startNewRow();
	}

}
