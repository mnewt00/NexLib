package dev.mnewt00.nexlib.exception;

public class NMSException extends RuntimeException {
	public NMSException(String message) {
		super(message);
	}

	public NMSException(String message, Exception exception) {
		super(message, exception);
	}
}
