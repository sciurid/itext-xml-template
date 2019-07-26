package me.chenqiang.pdf.template;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

public class CellTemplate extends ContainerTemplate<Cell, CellTemplate>
implements ComponentInContainer<Table>{
	protected boolean newRow;

	public CellTemplate(boolean newRow) {
		this.newRow = newRow;
	}

	public CellTemplate() {
		this(false);
	}
	
	public boolean isNewRow() {
		return this.newRow;
	}

	@Override
	public void process(Table container) {
		// TODO Auto-generated method stub
		
	}
	
}
