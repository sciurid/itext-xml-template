package me.chenqiang.pdf.xml.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementPath;

import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.composer.ComposerDirectory;
import me.chenqiang.pdf.composer.DivComposer;
import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ImageComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TableCellComposer;
import me.chenqiang.pdf.xml.context.AttributeRegistry;
import me.chenqiang.pdf.xml.context.TemplateContext;

public class ImageHandler extends BasicTemplateElementHandler<ImageComposer, Image> {

	public ImageHandler(TemplateContext context, ComposerDirectory directory, DocumentComposer doc) {
		super(context, directory, doc::append);
	}

	public ImageHandler(TemplateContext context, ComposerDirectory directory, ParagraphComposer para) {
		super(context, directory, para::append);
	}

	public ImageHandler(TemplateContext context, ComposerDirectory directory, TableCellComposer cell) {
		super(context, directory, cell::append);
	}
	
	public ImageHandler(TemplateContext context, ComposerDirectory directory, DivComposer tplDiv) {
		super(context, directory, tplDiv::append);
	}

	@Override
	protected ImageComposer create(ElementPath elementPath) {
		ImageComposer tplImg = new ImageComposer();
		Element current = elementPath.getCurrent();
		for (Attribute attr : current.attributes()) {
			String name = attr.getName();
			String value = attr.getValue();
			switch (name) {
			case AttributeRegistry.FILE:
				tplImg.setImageFile(value);
				break;
			case AttributeRegistry.RESOURCE:
				tplImg.setImageResource(value);
				break;
			case AttributeRegistry.REF:
				tplImg.setImageData(this.context.getResourceRepository().getImage(value));
				break;
			case AttributeRegistry.ID:
				this.directory.registerIdentifiable(value, tplImg);
				this.directory.registerDataPlaceholder(value, tplImg);
				break;
			default:
			}
		}
		return tplImg;
	}
	
	@Override
	protected Map<String, BiFunction<String, String, ? extends Consumer<? super Image>>> getAttributeMap() {
		return this.context.getAttributeRegistry().getImageMap();
	}

	public static final List<String> getElementNames() {
		return Arrays.asList("image", "img");
	}
	@Override
	public void register(ElementPath path) {
		for(String name : getElementNames()) {
			path.addHandler(name, this);
		}
	}	
}
