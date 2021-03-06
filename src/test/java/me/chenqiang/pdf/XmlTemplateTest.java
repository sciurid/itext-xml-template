package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chenqiang.pdf.sax.SAXDocumentEngine;

public class XmlTemplateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlTemplateTest.class);
	@Test
	public void doStandardSampleTest() throws DocumentException, IOException {
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		File file = File.createTempFile("Sample", ".pdf");
		
		SAXDocumentEngine engine = new SAXDocumentEngine();
		engine.load(stream);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {
			engine.produce("test", null, null, fos);
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
		
		SAXDocumentEngine engine = new SAXDocumentEngine();
		InputStream stream = XmlTemplateTest.class.getResourceAsStream("/standard-sample.xml");
		engine.load(stream);
		
		Map<String, Object> params = Map.of(
				"文本替换", "https://www.tsinghua.edu.cn", 
				"元素替换", "https://www.tsinghua.edu.cn", 
				"数据替换", sampleImage,
				"names", List.of("李白", "杜甫", "白居易", "杜牧", "李商隐"));
		List<BiConsumer<InputStream, OutputStream>> modifiers = List.of(SAXDocumentEngine.CHINESE_PAGE_NUMBER, SAXDocumentEngine.PRINTING_ONLY);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {
			engine.produce("test", params, modifiers, fos);
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
		
		SAXDocumentEngine engine = new SAXDocumentEngine();
		engine.load(stream);
		
		byte [] sampleImage = XmlTemplateTest.class.getResourceAsStream("/books.png").readAllBytes();
		
		LinkedBlockingQueue<File> files = new LinkedBlockingQueue<>();
		
		Map<String, Object> params = Map.of(
				"文本替换", "https://www.tsinghua.edu.cn", 
				"元素替换", "https://www.tsinghua.edu.cn", 
				"数据替换", sampleImage,
				"names", List.of("李白", "杜甫", "白居易", "杜牧", "李商隐"));
		List<BiConsumer<InputStream, OutputStream>> modifiers = List.of(SAXDocumentEngine.PAGE_NUMBER, SAXDocumentEngine.PRINTING_ONLY);
		
		Runnable task = () ->  {
			try {
				File file = File.createTempFile("Sample", ".pdf");

				try (FileOutputStream fos = new FileOutputStream(file)) {
					engine.produce("test", params, modifiers, fos);
				}
				files.offer(file);
			}
			catch (IOException e) {
				LOGGER.error("Template failed.", e);
			}
		};
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
		Thread display = new Thread(() -> {
			while(true) {
				try {
					File file = files.take();				
					if(Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							LOGGER.error("File open failed.", e);
						}
					}
					if(Thread.currentThread().isInterrupted() && files.isEmpty()) {
						return;
					}
				} catch (InterruptedException e) {
					if(files.isEmpty()) {
						return;
					}
				}
			}			
		});
		display.setName("Display");
		display.start();
		
		for(int i = 0; i < 10; i++) {
			executor.submit(task);
		}
		
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		
		display.interrupt();
		display.join(1000 * 60);
	}
}
