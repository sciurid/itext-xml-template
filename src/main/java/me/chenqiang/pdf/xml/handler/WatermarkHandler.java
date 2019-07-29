package me.chenqiang.pdf.xml.handler;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.WatermarkMaker;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.xml.context.TemplateContext;

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
		elementPath.addHandler("image", new WatermarkImageHandler(this.context, wmm));
		elementPath.addHandler("text", new WatermarkTextHandler(this.context, wmm));
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		// DO NOTHING
	}

}
