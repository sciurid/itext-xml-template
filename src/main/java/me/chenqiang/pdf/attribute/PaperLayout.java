package me.chenqiang.pdf.attribute;

import com.itextpdf.kernel.geom.PageSize;

import me.chenqiang.pdf.component.Copyable;

public class PaperLayout implements Copyable<PaperLayout>{
	protected PageSize pageSize;
	protected float marginLeft;
	protected float marginRight;
	protected float marginTop;
	protected float marginBottom;

	public PaperLayout() {
		this(PageSize.A4, 72, 72, 72, 72);
	}
	
	protected PaperLayout(PaperLayout origin) {
		this(origin.pageSize, origin.marginTop, origin.marginRight, origin.marginBottom, origin.marginLeft);
	}

	public PaperLayout(PageSize ps, float marginTop, float marginRight, float marginBottom, float marginLeft) {
		super();
		this.pageSize = ps == null ? PageSize.A4 : ps;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
	}

	public PageSize getPageSize() {
		return pageSize;
	}

	public void setPs(PageSize ps) {
		this.pageSize = ps;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}
	
	public void setMargin(float margin) {
		this.marginLeft = margin;
		this.marginRight = margin;
		this.marginTop = margin;
		this.marginBottom = margin;
	}

	@Override
	public PaperLayout copy() {
		return new PaperLayout(this);
	}
}
