package me.chenqiang.pdf.sax.composer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.oned.UPCEWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.DocumentContext;

public final class BarcodeComposer extends BasicImageComposer<BarcodeComposer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeComposer.class);
	protected final String text;
	protected final String output;
	protected final BarcodeFormat format;
	public static final int BASE_SIZE = 300;

	protected static final Map<String, BarcodeFormat> FORMATS = Map.ofEntries(
			Map.entry("qrcode", BarcodeFormat.QR_CODE), Map.entry("pdf417", BarcodeFormat.PDF_417),
			Map.entry("ean13", BarcodeFormat.EAN_13), Map.entry("ean8", BarcodeFormat.EAN_8),
			Map.entry("codebar", BarcodeFormat.CODABAR), Map.entry("code39", BarcodeFormat.CODE_39),
			Map.entry("code93", BarcodeFormat.CODE_93), Map.entry("code128", BarcodeFormat.CODE_128),
			Map.entry("upca", BarcodeFormat.UPC_A), Map.entry("upce", BarcodeFormat.UPC_E));

	@FunctionalInterface
	private static interface Creator {
		BitMatrix create(String text, BarcodeFormat format) throws WriterException;
	}

	protected static final Map<BarcodeFormat, Creator> CREATORS;
	protected static final Map<EncodeHintType, ?> DEFAULT_HINTS = Map.of(EncodeHintType.MARGIN, 1);
	static {
		CREATORS = Map.<BarcodeFormat, Creator>ofEntries(
				Map.entry(BarcodeFormat.QR_CODE,
						(str, format) -> new QRCodeWriter().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.PDF_417,
						(str, format) -> new PDF417Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.EAN_13,
						(str, format) -> new EAN13Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.EAN_8,
						(str, format) -> new EAN8Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.CODABAR,
						(str, format) -> new CodaBarWriter().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.CODE_39,
						(str, format) -> new Code39Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.CODE_93,
						(str, format) -> new Code93Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.CODE_128,
						(str, format) -> new Code128Writer().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.UPC_A,
						(str, format) -> new UPCAWriter().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)),
				Map.entry(BarcodeFormat.UPC_E,
						(str, format) -> new UPCEWriter().encode(str, format, BASE_SIZE, BASE_SIZE, DEFAULT_HINTS)));
	}
		
	public BarcodeComposer(String text, BarcodeFormat format, String output) {
		super();
		this.text = text;
		this.output = output;
		this.format = format;
	}
	
	public BarcodeComposer(String text, String format, String output) {
		super();
		this.text = text;
		this.output = output;
		if (format != null && FORMATS.containsKey(format)) {
			this.format = FORMATS.get(format);
		}
		else {
			this.format = BarcodeFormat.QR_CODE;
		}
	}
	
	public BarcodeComposer(String text, BarcodeFormat format) {
		this(text, format, "PNG");
	}
		
	public BarcodeComposer(String text, String format) {
		this(text, format, "PNG");
	}
	
	public BarcodeComposer(String text) {
		this(text, BarcodeFormat.QR_CODE, "PNG");
	}

	@Override
	protected Image create(DocumentContext context) {
		if(this.text == null || this.text.length() == 0) {
			return null;
		}
		if(!CREATORS.containsKey(this.format)) {
			return null;
		}

		try {
			String evaluated = context == null ? this.text : context.eval(this.text);
			BitMatrix matrix = CREATORS.get(this.format).create(evaluated, this.format);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(matrix, this.output, os);
			byte [] imageData = os.toByteArray();
			return new Image(ImageDataFactory.create(imageData));
		}
		catch(WriterException | IOException e) {
			LOGGER.error("BARCODE GENERATION ERROR.", e);
			return null;
		}
	}
}
