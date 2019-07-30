package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;

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

	public static final List<String> getElementNames() {
		return Arrays.asList("br");
	}	
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}
}
