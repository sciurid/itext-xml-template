package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlTemplateTest.class);
//	@Test
	public void doStandardSampleTest() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		File file = File.createTempFile("Sample", ".pdf");
		
		DocumentEngine engine = new DocumentEngine();
		engine.load(stream);
			
		byte [] pdfData = engine.produce("test", null, null, null);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {			
			fos.write(pdfData);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
				
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}
	}
	
//	@Test
	public void doStandardSampleTestSub() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		File file = File.createTempFile("Sample", ".pdf");
		
		DocumentEngine engine = new DocumentEngine();
		engine.load(stream);
		
		byte [] sampleImage = XmlTemplateTest.class.getResourceAsStream("/books.png").readAllBytes();		
		byte [] pdfData = engine.produce("test", Map.of("文本替换", "https://www.tsinghua.edu.cn"), Map.of("元素替换", "https://www.tsinghua.edu.cn"), 
					Map.of("数据替换", sampleImage));
		
		try (FileOutputStream fos = new FileOutputStream(file)) {			
			fos.write(pdfData);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}
	}
	
	@Test
	public void parallelTest() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		
		DocumentEngine engine = new DocumentEngine();
		engine.load(stream);
		
		byte [] sampleImage = XmlTemplateTest.class.getResourceAsStream("/books.png").readAllBytes();	
		
		List<Thread> list = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			Thread thread = new Thread(() ->  {
				try {
					File file = File.createTempFile("Sample", ".pdf");
					byte [] pdfData = engine.produce("test", 
							Map.of("文本替换", "https://www.tsinghua.edu.cn", 
									"时间", LocalDateTime.now().toString()), 
							Map.of("元素替换", "https://www.tsinghua.edu.cn"), 
								Map.of("数据替换", sampleImage));
					
					try (FileOutputStream fos = new FileOutputStream(file)) {			
						fos.write(pdfData);
					} 
					if(Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(file);
					}
				}
				catch (IOException e) {
					LOGGER.error("Template failed.", e);
				}
			});
			thread.start();
			list.add(thread);
		}
		list.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		});
		
	}
}
