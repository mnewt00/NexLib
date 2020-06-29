package dev.mnewt00.nexlib;


import dev.mnewt00.nexlib.reflection.ReflectionUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexLib {
	/**
	 * The plugin that is using NexLib
	 */
	@Getter
	private static JavaPlugin plugin;

	/**
	 * Initializes NexLib and sets the plugin
	 * To be called in onEnable
	 *
	 * @param javaPlugin The plugin (instance)
	 */
	public static void initNexLib(JavaPlugin javaPlugin) {
		plugin = javaPlugin;
	}

	/**
	 * A easier way to register a command instead of {@link ReflectionUtil#registerCmd(Command)}
	 * @param command
	 */
	public static void registerCommand(Command command) {
		ReflectionUtil.registerCmd(command);
	}
}
