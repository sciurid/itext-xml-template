package me.chenqiang.pdf;

import java.io.IOException;

import org.junit.Test;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.TextAlignment;

import me.chenqiang.pdf.composer.DocumentComposer;
import me.chenqiang.pdf.composer.ParagraphComposer;
import me.chenqiang.pdf.composer.TextComposer;

public class ComposerTest extends PdfTest{	
	@Test
	public void testEmptyDocument() throws IOException {
		DocumentComposer template = new DocumentComposer();
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
		DocumentComposer template = new DocumentComposer();		
		template
		.setAttribute(doc -> doc.setFont(times))
		.setAttribute(doc -> doc.setFontSize(12))
		;
		
		ParagraphComposer title = new ParagraphComposer();
		title
		.append(new TextComposer("SAMPLE ").setAttribute(para -> para.setBold()))
		.append(new TextComposer("The Gettysburg Address"));
		
		title
		.setAttribute(para -> para.setTextAlignment(TextAlignment.CENTER))
		.setAttribute(para -> para.setFontSize(24));
		
		template.append(title);
		
		ParagraphComposer content = new ParagraphComposer();
		TextComposer strContent = new TextComposer(this.gettysburg);
		content.append(strContent);
		content.setAttribute(para -> para.setFirstLineIndent(24))
		.setAttribute(para -> para.setTextAlignment(TextAlignment.JUSTIFIED));		
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
		
		DocumentComposer template = new DocumentComposer();
		template
		.setAttribute(doc -> doc.setFontProvider(fp))
		.setAttribute(doc -> doc.setFontFamily("kaiti"))
		.setAttribute(doc -> doc.setFontSize(12))
		;
		
		ParagraphComposer title = new ParagraphComposer();
		title
		.append(new TextComposer("蜀道难"))
		.setAttribute(para -> para.setFontFamily("simhei"))
		.setAttribute(para -> para.setTextAlignment(TextAlignment.CENTER))
		.setAttribute(para -> para.setFontSize(24));
		
		template.append(title);
		
		ParagraphComposer content = new ParagraphComposer();
		TextComposer strContent = new TextComposer(this.shudaonan);
		content.append(strContent)
		.setAttribute(para -> para.setFirstLineIndent(24))
		.setAttribute(para -> para.setTextAlignment(TextAlignment.JUSTIFIED));		
		
		template.append(content);
		
		this.render(template, "CN-");
	}	
}