package me.chenqiang.pdf.composer;

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
import com.itextpdf.layout.element.Image;

import me.chenqiang.pdf.component.StringParameterPlaceholder;
import me.chenqiang.pdf.component.StringStub;
import me.chenqiang.pdf.utils.Substitution;

public final class BarcodeComposer extends BasicImageComposer<BarcodeComposer> 
implements StringParameterPlaceholder, StringStub{
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeComposer.class);
	protected String text;
	protected String output = "PNG";
	protected BarcodeFormat format = BarcodeFormat.QR_CODE;
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
	
	public BarcodeComposer() {
		super();
	}
	

	protected BarcodeComposer(BarcodeComposer origin) {
		super(origin);
		this.text = origin.text;
		this.output = origin.output;
		this.format = origin.format;
	}

	public BarcodeComposer setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public void setParameter(String parameter) {
		this.setText(parameter);
	}

	public BarcodeComposer setFormat(String format) {
		if (format != null && FORMATS.containsKey(format)) {
			this.format = FORMATS.get(format);
		} else {
			this.format = BarcodeFormat.QR_CODE;
		}
		return this;
	}

	protected byte[] getImageData(BitMatrix matrix) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(matrix, this.output, os);
			return os.toByteArray();
		} catch (IOException e) {
			LOGGER.error(IMAGE_ERROR, e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected Image create() {
		try {
			if(this.text != null && this.text.length() != 0 && CREATORS.containsKey(this.format)) {
				BitMatrix matrix = CREATORS.get(this.format).create(this.text, this.format);
				if (matrix == null) {
					this.imageData = null;
				} else {
					this.setImageData(this.getImageData(matrix));
				}
			}
			else {
				this.imageData = null;
			}
		} catch (WriterException we) {
			LOGGER.error(IMAGE_ERROR, we);
		}
		return super.create();
	}
	
	@Override
	public BarcodeComposer copy() {
		return new BarcodeComposer(this);
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.text = Substitution.substitute(this.text, params);
	}	
}
