package me.chenqiang.pdf.attribute;

import com.itextpdf.kernel.geom.PageSize;

public class PaperLayout {
	protected PageSize pageSize;
	protected float marginLeft;
	protected float marginRight;
	protected float marginTop;
	protected float marginBottom;

	public PaperLayout() {
		this(PageSize.A4, 72, 72, 72, 72);
	}

	public PaperLayout(PageSize ps, float marginLeft, float marginRight, float marginTop, float marginBottom) {
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
}
