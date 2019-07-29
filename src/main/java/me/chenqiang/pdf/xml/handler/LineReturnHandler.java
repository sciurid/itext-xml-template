package me.chenqiang.pdf.xml.handler;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.StringComposer;

public class LineReturnHandler implements ElementHandler{
	protected ParagraphComposer tplPara;
	public LineReturnHandler(ParagraphComposer tplPara) {
		this.tplPara = tplPara;
	}

	@Override
	public void onStart(ElementPath elementPath) {		
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		this.tplPara.append(new StringComposer("\r\n"));
	}

	public void register(ElementPath path) {
		path.addHandler("br", this);
	}
}
