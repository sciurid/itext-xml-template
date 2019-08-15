package me.chenqiang.pdf.sax.handler;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.sax.TemplateContext;
import me.chenqiang.pdf.sax.composer.DocumentComposer;
import me.chenqiang.pdf.sax.composer.WatermarkMaker;

public class WatermarkHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkHandler.class);
	protected TemplateContext context;
	protected DocumentComposer tplDoc;

	public WatermarkHandler(TemplateContext context, DocumentComposer tplDoc) {
		this.context = context;
		this.tplDoc = tplDoc;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.debug("[START] {}", elementPath.getPath());
		WatermarkMaker wmm = this.tplDoc.getWatermarkMaker();
		new WatermarkImageHandler(this.context, wmm).register(elementPath);
		new WatermarkTextHandler(this.context, wmm).register(elementPath);
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// DO NOTHING
	}

	public void register(ElementPath elementPath) {
		elementPath.addHandler("watermark", this);
	}
}
