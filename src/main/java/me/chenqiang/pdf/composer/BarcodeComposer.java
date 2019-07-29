package me.chenqiang.pdf.composer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
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

import me.chenqiang.pdf.configurability.StringParameterPlaceholder;
import me.chenqiang.pdf.configurability.Substitution;

public final class BarcodeComposer extends BasicImageComposer<BarcodeComposer> implements StringParameterPlaceholder {
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeComposer.class);
	protected String message;
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
		BitMatrix create() throws WriterException;
	}

	protected final Map<BarcodeFormat, Creator> creators;

	public BarcodeComposer() {
		this.creators = Map.<BarcodeFormat, Creator>ofEntries(
				Map.entry(BarcodeFormat.QR_CODE,
						() -> new QRCodeWriter().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.PDF_417,
						() -> new PDF417Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.EAN_13,
						() -> new EAN13Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.EAN_8,
						() -> new EAN8Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.CODABAR,
						() -> new CodaBarWriter().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.CODE_39,
						() -> new Code39Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.CODE_93,
						() -> new Code93Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.CODE_128,
						() -> new Code128Writer().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.UPC_A,
						() -> new UPCAWriter().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)),
				Map.entry(BarcodeFormat.UPC_E,
						() -> new UPCEWriter().encode(this.message, this.format, BASE_SIZE, BASE_SIZE)));
	}

	public BarcodeComposer setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public void setParameter(String parameter) {
		this.setMessage(parameter);
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
			if (this.creators.containsKey(this.format)) {
				BitMatrix matrix = this.creators.get(this.format).create();
				if (matrix == null) {
					this.setImageData((byte[]) null);
				} else {
					this.setImageData(this.getImageData(matrix));
				}
			}
		} catch (WriterException we) {
			LOGGER.error(IMAGE_ERROR, we);
		}
		return super.create();
	}

	@Override
	public void substitute(Map<String, String> params) {
		this.setMessage(Substitution.substitute(this.message, params));
	}

}
