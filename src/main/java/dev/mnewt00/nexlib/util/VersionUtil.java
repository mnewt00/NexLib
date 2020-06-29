package dev.mnewt00.nexlib.util;

import dev.mnewt00.nexlib.Common;
import dev.mnewt00.nexlib.exception.Nexception;
import org.bukkit.Bukkit;

public class VersionUtil {
	private static String sVersion;
	public enum V {
		v1_16(16),
		v1_15(15),
		v1_14(14),
		v1_13(13),
		v1_12(12),
		v1_11(11),
		v1_10(10),
		v1_9(9),
		v1_8(8);

		private final int verN;


		V(int version) {
			this.verN = version;
		}
		protected static V parse(int number) {
			for (final V v : values())
				if (v.verN == number)
					return v;

			throw new Nexception("Invalid version " + number);
		}
	}

	public static String getServerVersion() {
		return sVersion.equals("craftbukkit") ? "" : sVersion;
	}

	static {
		try {
			final String packageN = Bukkit.getServer() == null ? "" : Bukkit.getServer().getClass().getPackage().getName();
			sVersion = packageN.substring(packageN.lastIndexOf('.') + 1);
		} catch (Throwable throwable) {
			Common.error(throwable, null, "Could not correctly detect your server's Minecraft version.", false, true);
		}
	}
}
