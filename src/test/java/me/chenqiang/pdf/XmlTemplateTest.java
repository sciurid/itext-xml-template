package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlTemplateTest.class);
	@Test
	public void doStandardSampleTest() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		File file = File.createTempFile("Sample", ".pdf");
		
		DocumentEngine engine = new DocumentEngine();
		engine.load(stream);
			
		byte [] pdfData = engine.produce("test", null);
		
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
	public void doStandardSampleTestSub() throws DocumentException, IOException {
		File file = File.createTempFile("Sample", ".pdf");
		byte [] sampleImage = XmlTemplateTest.class.getResourceAsStream("/books.png").readAllBytes();		
		
		DocumentEngine engine = new DocumentEngine();
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		engine.load(stream);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {
			engine.add(DocumentEngine.PAGE_NUMBER);
			engine.add(DocumentEngine.PRINTING_ONLY);
			engine.produce("test", 
					Map.of(
							"文本替换", "https://www.tsinghua.edu.cn", 
							"元素替换", "https://www.tsinghua.edu.cn", 
							"数据替换", sampleImage), 
					fos);
		} catch (IOException e) {
			LOGGER.error("Template failed.", e);
		}
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(file);
		}
	}
	
//	@Test
	public void parallelTest() throws DocumentException, IOException, InterruptedException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		
		DocumentEngine engine = new DocumentEngine();
		engine.load(stream);
		
		byte [] sampleImage = XmlTemplateTest.class.getResourceAsStream("/books.png").readAllBytes();	
		
		
		Set<Thread> threads = new LinkedHashSet<>();
		List<File> files = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			Thread thread = new Thread(() ->  {
				try {
					File file = File.createTempFile("Sample", ".pdf");					
					try (FileOutputStream fos = new FileOutputStream(file)) {
						engine.produce("test", 
								Map.of(
										"文本替换", "https://www.tsinghua.edu.cn", 
										"元素替换", "https://www.tsinghua.edu.cn", 
										"数据替换", sampleImage),
								fos);
					}
					files.add(file);
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e) {
						LOGGER.error("File open failed.", e);
					}
				}
				catch (IOException e) {
					LOGGER.error("Template failed.", e);
				}
			});
			thread.start();
			threads.add(thread);
		}
		synchronized(threads) {
			for(Thread t : threads) {
				t.join();
			}
		}
		files.forEach(file -> {
			if(Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					LOGGER.error("File open failed.", e);
				}
			}
		});
		
	}
}
