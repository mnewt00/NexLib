package dev.mnewt00.nexlib.util;

import dev.mnewt00.nexlib.exception.Nexception;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidUtil {
	public void checkBoolean(boolean bool) {
		if (!bool) throw new Nexception();
	}

	public void checkBoolean(boolean bool, String errorMessage) {
		if (!bool) throw new Nexception(errorMessage);
	}

	public void notNull(String string, String errorMessage) {
		if (string == null) throw new Nexception(errorMessage);
	}

	public void notNull(String string) {
		if (string == null) throw new Nexception();
	}
}
