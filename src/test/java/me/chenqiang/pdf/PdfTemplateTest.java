package me.chenqiang.pdf;

import java.io.IOException;

import org.junit.Test;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.TextAlignment;

import me.chenqiang.pdf.template.DocumentTemplate;
import me.chenqiang.pdf.template.element.ParagraphTemplate;
import me.chenqiang.pdf.template.element.TextTemplate;

public class PdfTemplateTest extends PdfTest{	
	@Test
	public void testEmptyDocument() throws IOException {
		DocumentTemplate template = new DocumentTemplate();
		this.render(template, "Empty-");
	}
	
	protected String gettysburg = "Four score and seven years ago our fathers brought forth on this continent, "
			+ "a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal."
			+ "Now we are engaged in a great civil war, testing whether that nation, "
			+ "or any nation so conceived and so dedicated, can long endure. "
			+ "We are met on a great battle-field of that war. "
			+ "We have come to dedicate a portion of that field, "
			+ "as a final resting place for those who here gave their lives that that nation might live. "
			+ "It is altogether fitting and proper that we should do this.";
	
	@Test
	public void testSimpleEnglishDocument() throws IOException {
		PdfFont times = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		DocumentTemplate template = new DocumentTemplate();		
		template
		.set(doc -> doc.setFont(times))
		.set(doc -> doc.setFontSize(12))
		;
		
		ParagraphTemplate title = new ParagraphTemplate();
		title
		.append(new TextTemplate("SAMPLE ").set(para -> para.setBold()))
		.append(new TextTemplate("The Gettysburg Address"));
		
		title
		.set(para -> para.setTextAlignment(TextAlignment.CENTER))
		.set(para -> para.setFontSize(24));
		
		template.append(title);
		
		ParagraphTemplate content = new ParagraphTemplate();
		TextTemplate strContent = new TextTemplate(this.gettysburg);
		content.append(strContent);
		content.set(para -> para.setFirstLineIndent(24))
		.set(para -> para.setTextAlignment(TextAlignment.JUSTIFIED));		
		template.append(content);
		
		this.render(template, "SimpleEnglish-");
	}
	
	protected String shudaonan =  
			"噫吁嚱，危乎高哉！\r\n" + 
			"蜀道之难，难于上青天！\r\n" + 
			"蚕丛及鱼凫，开国何茫然。\r\n" + 
			"尔来四万八千岁，不与秦塞通人烟。\r\n" + 
			"西当太白有鸟道，可以横绝峨眉巅。\r\n" + 
			"地崩山摧壮士死，然后天梯石栈相钩连。\r\n" + 
			"上有六龙回日之高标，下有冲波逆折之回川。\r\n" + 
			"黄鹤之飞尚不得过，猿猱欲度愁攀援。\r\n" + 
			"青泥何盘盘，百步九折萦岩峦。\r\n" + 
			"扪参历井仰胁息，以手抚膺坐长叹。\r\n" + 
			"问君西游何时还，畏途躔岩不可攀。\r\n" + 
			"但见悲鸟号古木，雄飞雌从绕林间。\r\n" + 
			"又闻子规啼夜月，愁空山，蜀道之难，难于上青天！\r\n" + 
			"使人听此凋朱颜。\r\n" + 
			"连峰去天不盈尺，枯松倒挂倚绝壁。\r\n" + 
			"飞湍瀑流争喧虺，砰崖转石万壑雷。\r\n" + 
			"其险也如此，嗟尔远道之人胡为乎哉！\r\n" + 
			"剑阁峥嵘而崔嵬，一夫当关，万夫莫开。\r\n" + 
			"所守或匪亲，化为狼与豺。\r\n" + 
			"朝避猛虎，夕避长蛇，磨牙吮血，杀人如麻。\r\n" + 
			"锦城虽云乐，不如早还家。\r\n" + 
			"蜀道之难，难于上青天！侧身西望长咨嗟。";
	
	@Test
	public void testChineseDocument() throws IOException {
		FontProvider fp = new FontProvider();
		fp.addSystemFonts();
		
		DocumentTemplate template = new DocumentTemplate();
		template
		.set(doc -> doc.setFontProvider(fp))
		.set(doc -> doc.setFontFamily("kaiti"))
		.set(doc -> doc.setFontSize(12))
		;
		
		ParagraphTemplate title = new ParagraphTemplate();
		title
		.append(new TextTemplate("蜀道难"))
		.set(para -> para.setFontFamily("simhei"))
		.set(para -> para.setTextAlignment(TextAlignment.CENTER))
		.set(para -> para.setFontSize(24));
		
		template.append(title);
		
		ParagraphTemplate content = new ParagraphTemplate();
		TextTemplate strContent = new TextTemplate(this.shudaonan);
		content.append(strContent)
		.set(para -> para.setFirstLineIndent(24))
		.set(para -> para.setTextAlignment(TextAlignment.JUSTIFIED));		
		
		template.append(content);
		
		this.render(template, "CN-");
	}	
}
