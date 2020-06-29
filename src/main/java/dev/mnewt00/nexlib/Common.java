package dev.mnewt00.nexlib;

import dev.mnewt00.nexlib.exception.Debug;
import lombok.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Common {

	// ------------------------------------------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------------------------------------------

	private static final Plugin plugin = NexLib.getPlugin();

	/**
	 * The console sender asserting if its not null
	 */
	private static final CommandSender _CONSOLE = Bukkit.getServer() != null ? Bukkit.getServer().getConsoleSender() : null;

	/**
	 * The prefix used for logging. Do setLogPrefix(String) to change it
	 */
	@Getter @Setter
	private static String logPrefix = "&7[&f" + getName() + "&f]&f ";

	// ------------------------------------------------------------------------------------------------------------
	// Plugin
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the plugin's name
	 * @return The plugin's name
	 */
	public static String getName() {
		return plugin.getDescription().getName();
	}

	/**
	 * Gets the plugin's version
	 * @return The plugin's version
	 */
	public static String getVersion() {
		return plugin.getDescription().getVersion();
	}

	/**
	 * Gets the plugin's authors' names
	 * @return A list of strings containing the authors names
	 */
	public static List<String> getAuthors() {
		return plugin.getDescription().getAuthors();
	}

	/**
	 * Returns all the authors names joined together seperated by ','
	 * @return The joined string
	 */
	public static String getAuthorsJoined() {
		StringBuilder s = new StringBuilder();
		for (String a : plugin.getDescription().getAuthors())
			s.append(a).append(", ");
		return s.toString();
	}

	// ------------------------------------------------------------------------------------------------------------
	// Broadcasting
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Sends a colorized message to all the players online on the server.
	 * @param log - Whether to log it to console or not
	 * @param messages - The list of messages to broadcast. You can use "null" to send a blank message.
	 */
	public static void broadcast(final boolean log, final String... messages) {
		for (final String m : messages) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (m == null || m.equals("") || m.equals("null")) player.sendMessage("");
				player.sendMessage(color(m));
			}
			if (log) {
				if (m == null || m.equals("") || m.equals("null")) _CONSOLE.sendMessage("");
				log(m);
			}
		}
	}

	/**
	 * Broadcasts a message to the whole server to those who have the specified permission
	 * @param log Whether or not to log the message to console
	 * @param permission The permission required to recieve the message
	 * @param messages The message(s) to broadcast
	 */
	public static void broadcastPerm(final boolean log, final String permission, final String... messages) {
		for (final String m : messages) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!player.hasPermission(permission)) return;
				if (m == null || m.equals("") || m.equals("null")) player.sendMessage("");
				player.sendMessage(color(m));
			}
			if (log) {
				if (m == null || m.equals("") || m.equals("null")) _CONSOLE.sendMessage("");
				log(m);
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// Colorizing messages
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Returns a message that has been colorized using the standard colorcode '&'
	 * @param message String to colorize
	 * @return Colored string
	 */
	public static String color(final String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Removes all colorcodes '&' from a string.
	 * @param message String to remove colorcodes from
	 * @return String that has colorcodes removed
	 */
	public static String removeColor(final String message) {
		return message.replaceAll("(" + ChatColor.COLOR_CHAR + "|&)([0-9a-fk-or])", "");
	}

	// ------------------------------------------------------------------------------------------------------------
	// Logging
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * logs a message to console with full colorcode support.
	 * @param message The message to log to console
	 */
	public static void log(final String message) {
		if (_CONSOLE == null)
			System.out.println(removeColor(message));
		else
			_CONSOLE.sendMessage(color(message));
	}

	/**
	 * Logs a bunch of messages to console
	 * @param messages The messages to log to console
	 */
	public static void log(final String... messages) {
		for (String message : messages) {
			log(message);
		}
	}

	/**
	 * Logs a message to console with a prefix
	 * @param message The message to log to console
	 */
	public static void logP(final String message) {
		if (_CONSOLE == null)
			System.out.println(logPrefix + removeColor(message));
		else
			_CONSOLE.sendMessage(logPrefix + color(message));
	}


	/**
	 * Sends an error to console and has support for file logging and disabling the plugin
	 * @param throwable The throwable
	 * @param supportID The ID used for support purposes e.g 14
	 * @param info The additional information to send into console and pass to the file logging
	 * @param disablePlugin Whether or not to disable the plugin
	 * @param fileLog Whether or not to use Debug.save to save the error to a error.log file
	 */
	public static void error(Throwable throwable, final Integer supportID, @NonNull final String info, boolean disablePlugin, boolean fileLog) {
		StringBuilder message = new StringBuilder();
		message.append("&cAn error has occurred in ").append(getName()).append(".\n &cPlease contact support if you are unable to resolve this issue.\n");
		message.append("Information:&f ");
		message.append(info);

		if (supportID != null) {
			message.append("\n&cSupport ID:&f " + supportID);
		}

		log(message.toString());
		if (fileLog) Debug.save(throwable, info);
		if (disablePlugin) disablePlugin();
	}

	// ------------------------------------------------------------------------------------------------------------
	// Bukkit Tasks
	// ------------------------------------------------------------------------------------------------------------

	public static int repeatTask(Runnable task, long startDelay, long interval) {
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, startDelay, interval);
	}

	public static int repeatTask(Runnable task, long interval) {
		return repeatTask(task,0L, interval);
	}

	@Deprecated
	public static int repeatAsyncTask(Runnable task, long startDelay, long interval) {
		return Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, task, startDelay, interval);
	}

	@Deprecated
	public static int repeatAsyncTask(Runnable task, long interval) {
		return repeatAsyncTask(task, 0L, interval);
	}

	public static int delayTask(Runnable task, long delay) {
		return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
	}

	@Deprecated
	public static int delayAsyncTask(Runnable task, long delay) {
		return Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, task, delay);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Bukkit
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Disables the plugin when ran
	 */
	public static void disablePlugin() {
		NexLib.getPlugin().getPluginLoader().disablePlugin(NexLib.getPlugin());
	}

	public static Player findPlayer(String name) {
		return Bukkit.getPlayer(name);
	}

	public static Player findPlayer(UUID uuid) {
		return Bukkit.getPlayer(uuid);
	}
}
