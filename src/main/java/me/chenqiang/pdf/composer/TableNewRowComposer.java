package me.chenqiang.pdf.composer;

import com.itextpdf.layout.element.Table;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.TableComponent;

// 并未实际使用
public class TableNewRowComposer implements TableComponent {

	@Override
	public void process(Table tbl, DocumentContext context) {
		tbl.startNewRow();
	}

}
