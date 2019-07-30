package me.chenqiang.pdf.xml.handler;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoggingHanlder implements ElementHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLoggingHanlder.class);

	@Override
	public void onStart(ElementPath elementPath) {
		LOGGER.warn("[START] Unhandled element: {}, {}", elementPath.getPath(), elementPath.getCurrent());
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		LOGGER.debug("[END] Unhandled element: {}, {}", elementPath.getPath(), elementPath.getCurrent());
	}	
}

