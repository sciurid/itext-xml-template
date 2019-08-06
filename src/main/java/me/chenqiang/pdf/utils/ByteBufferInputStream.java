package me.chenqiang.pdf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteBufferInputStream extends InputStream {
	protected byte[] buffer;
	protected int index;
	protected int offset;
	protected int limit;
	protected int mark;
	protected boolean closed;

	public ByteBufferInputStream(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	public ByteBufferInputStream(byte[] buffer, int offset, int limit) {
		if (buffer == null) {
			throw new NullPointerException();
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException(offset);
		}
		if (limit > buffer.length) {
			throw new IndexOutOfBoundsException(limit);
		}
		this.buffer = buffer;
		this.index = 0;
		this.mark = 0;
		this.closed = false;
		this.offset = offset;
		this.limit = limit;
	}

	@Override
	public int read() throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		if (this.index == this.buffer.length) {
			return -1;
		} else {
			return this.buffer[this.index++] & 0xff;
		}
	}

	@Override
	public int read(byte[] b, int c, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		}
		if ((c < 0) || (c + len > b.length) || (len < 0)) {
			throw new IndexOutOfBoundsException();
		}
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		if (this.index >= this.limit) {
			return -1; // EOF
		}
		if (len > this.limit - this.index) {
			len = this.limit - this.index;
		}
		System.arraycopy(this.buffer, this.index, b, c, len);
		this.index += len;
		return len;
	}

	@Override
	public byte[] readAllBytes() throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		if(this.limit == this.index) {
			return new byte[0];
		}
		int remaining = this.limit - this.index;
		byte [] res = new byte[remaining];
		System.arraycopy(this.buffer, this.index, res, 0, remaining);
		this.index = this.limit;
		return res;
	}

	@Override
	public byte[] readNBytes(int len) throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		if(this.limit == this.index) {
			return new byte[0];
		}
		if(this.index + len > this.limit) {
			len = this.limit - this.index;
		}
		
		byte [] res = new byte[len];
		System.arraycopy(this.buffer, this.index, res, 0, len);
		this.index += len;
		return res;
	}

	@Override
	public int readNBytes(byte[] b, int off, int len) throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		return this.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		if(n > 0) {
			this.index += n;
			if(this.index > this.limit) {
				this.index = this.limit;
				return this.limit - this.index;
			}
			else {
				return n;
			}
		}
		else {
			return 0;
		}		
	}

	@Override
	public int available() throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		return this.buffer.length - this.index;
	}

	@Override
	public void close() throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		}
		this.closed = true;
	}

	@Override
	public synchronized void mark(int readlimit) {
		this.mark = this.index;
	}

	@Override
	public synchronized void reset() throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		} 
		
		this.index = this.mark;
		
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public long transferTo(OutputStream out) throws IOException {
		if (this.closed) {
			throw new IOException("Stream closed");
		} 
		int len = this.limit - this.index;
		out.write(this.buffer, this.index, len);
		this.index = this.limit;
		return len;
	}

}
