package me.chenqiang.pdf.utils;

import java.io.IOException;
import java.io.OutputStream;

public class ByteBufferOutputStream extends OutputStream {
	protected static final int INITIAL_SIZE = 16 * 1024; //16k
	protected static final int LINEAR_ENLARGMENT_LIMIT = 64 * 1024 * 1024; //64M

	protected byte[] buffer;
	protected int index = 0;
	protected boolean closed = false;
	protected boolean shared = false;
	protected int capacity;

	public ByteBufferOutputStream() {
		this(INITIAL_SIZE);
	}

	public ByteBufferOutputStream(int capacity) {
		this.buffer = new byte[capacity];
		this.capacity = capacity;
	}

	@Override
	public void write(int b) throws IOException {
		if(this.closed) {
		    throw new IOException ("Stream closed");
		}
		if(this.index == this.buffer.length) {
			this.enlarge(1);
		}
		this.buffer[this.index++] = (byte)b;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(this.closed) {
		    throw new IOException ("Stream closed");
		}
		if(this.index + len >= this.buffer.length) {
			this.enlarge(len);
		}
		System.arraycopy(b, off, this.buffer, this.index, len);
		this.index += len;		
	}

	@Override
	public void close() throws IOException {
		this.closed = true;
	}
	
	public void reset() throws IOException {
		if(this.shared) {
			this.buffer = new byte[this.capacity];
			this.index = 0;
			this.shared = false;
			this.closed = false;
		}
		else {
			this.index = 0;
			this.closed = false;
		}
	}
	
	public void writeTo(OutputStream os) throws IOException {
		os.write(this.buffer, 0, this.index);
	}
	
	public ByteBufferInputStream transfer() {
		this.shared = true;
		return new ByteBufferInputStream(this.buffer, 0, this.index);		
	}

	protected void enlarge(int extra) {
		int expected = this.index + extra;
		int length = this.buffer.length;
		while(length < expected) {
			if(length < LINEAR_ENLARGMENT_LIMIT) {
				length *= 2;
			}
			else {
				length += LINEAR_ENLARGMENT_LIMIT;
			}			
		}
		
		byte[] temp = new byte[length];
		System.arraycopy(this.buffer, 0, temp, 0, this.index);
		this.buffer = temp;
	}
}
