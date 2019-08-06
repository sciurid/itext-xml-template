package me.chenqiang.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import me.chenqiang.pdf.utils.ByteBufferInputStream;
import me.chenqiang.pdf.utils.ByteBufferOutputStream;

public class BufferedStreamTest {

	@Test
	public void testOut() throws IOException {
		InputStream is = this.getClass().getResourceAsStream("/books.png");
		ByteBufferInputStream bis = null;
		try (ByteBufferOutputStream bos = new ByteBufferOutputStream(16)) {
			for (int i = 0; i < 1024; i++) {
				int b = is.read();
				if (b < 0) {
					return;
				}
				bos.write(b);
			}
			bos.write(is.readNBytes(256), 0, 256);
			bos.write(is.readAllBytes());
			
			bis = bos.transfer();
		}
		
		File temp = File.createTempFile("Test", ".png");
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			for (int i = 0; i < 256; i++) {
				int b = bis.read();
				if (b < 0) {
					return;
				}
				fos.write(b);
			}
			fos.write(bis.readNBytes(256), 0, 256);
			fos.write(bis.readAllBytes());
		}
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(temp);
		}
	}
}
