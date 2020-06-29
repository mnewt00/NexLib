package dev.mnewt00.nexlib.exception;

import dev.mnewt00.nexlib.Common;
import dev.mnewt00.nexlib.NexLib;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Debug {

	/**
	 * Creates a logging file and appends the throwable and the messages with it
	 * @param throwable The throwable/exception
	 * @param messages The messages/addition information
	 */
	public static void save(Throwable throwable, String... messages) {
		if (Bukkit.getServer() == null) return;
		final List<String> lines = new ArrayList<>();
		final String header = Common.getName() + " " + Common.getVersion() + " encountered a " + throwable.getClass().getSimpleName();

		add(lines,
				"> " + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(System.currentTimeMillis()),
				header,
				"Server Version " + Bukkit.getName() + " " + Bukkit.getBukkitVersion() + " and Java " + System.getProperty("java.version"),
				"Plugins: " + StringUtils.join(Bukkit.getPluginManager().getPlugins(), ", "),
				"");

		if (messages != null && !StringUtils.join(messages, "").isEmpty()) {
			add(lines, "Additional Information: ");
			add(lines, StringUtils.join(messages, ", ") + "\n");
		}
		
		

		{

			do {
				add(lines, throwable == null ? "Unknown error" : throwable.getClass().getSimpleName() + " " + throwable.getMessage() != null ? throwable.getMessage() : "Unknown Cause" );

				int count = 0;

				for (final StackTraceElement el : throwable.getStackTrace()) {
					count++;

					final String trace = el.toString();

					if (count > 6 && trace.startsWith("net.minecraft.server"))
						break;

					add(lines, "\t at " + el.toString());
				}
			} while ((throwable = throwable.getCause()) != null);
		}

		add(lines, "");
		File stLo = getOrMakeFile("error.log");

		try {
			final Path path = Paths.get(stLo.toURI());

			try {
				Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);

			} catch (final ClosedByInterruptException ex) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(stLo, true))) {
					for (final String line : lines)
						bw.append(System.lineSeparator() + line);

				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			Common.log(header);
			Common.log("&cCheck the error.log file!");
		} catch (final Exception ex) {
			System.out.println("Failed to write to " + stLo);
			ex.printStackTrace();
		}

	}

	public static File getOrMakeFile(String path) {
		final File file = getFile(path);
		return file.exists() ? file : createFile(path);
	}

	public static File getFile(String path) {
		return new File(NexLib.getPlugin().getName(), path);
	}

	private static File createFile(String path) {
		final File datafolder = NexLib.getPlugin().getDataFolder();
		final int lastIndex = path.lastIndexOf('/');
		final File directory = new File(datafolder, path.substring(0, lastIndex >= 0 ? lastIndex : 0));
		directory.mkdirs();
		final File destination = new File(datafolder, path);
		try {
			destination.createNewFile();

		} catch (final IOException ex) {
			System.out.println("Failed to create a new file " + path);

			ex.printStackTrace();
		}

		return destination;
	}

	private static void add(List<String> list, String... messages) {
		list.addAll(Arrays.asList(messages));
	}
}
