package dev.mnewt00.nexlib.exception;

import dev.mnewt00.nexlib.Common;

public class Nexception extends RuntimeException {
	public Nexception(Throwable ex) {
		super(ex);
		Common.error(ex,1,"No information provided", false, true);
	}

	public Nexception(String message) {
		super(message);
		Common.error(this,1, message, false, true);
	}

	public Nexception(String message, Throwable ex) {
		super(message, ex);
		Common.error(ex,1, message, false, true);
	}

	public Nexception() {
		Common.error(this,1, "No information provided", false, true);
	}
}
