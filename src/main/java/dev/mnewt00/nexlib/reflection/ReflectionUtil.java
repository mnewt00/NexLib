package dev.mnewt00.nexlib.reflection;

import dev.mnewt00.nexlib.Common;
import dev.mnewt00.nexlib.exception.NMSException;
import dev.mnewt00.nexlib.util.ValidUtil;
import dev.mnewt00.nexlib.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.InvocationTargetException;

public final class ReflectionUtil {
	private static final String nmsClass = "net.minecraft.server";
	private static final String cbClass = "org.bukkit.craftbukkit";

	public static Class<?> findClass(String classPath) {
		try {
			return Class.forName(classPath);
		} catch (ClassNotFoundException ex) {
			throw new NMSException("Could not find unknown class " + classPath);
		}
	}

	public static Class<?> getNMSClass(String className) {
		return findClass(nmsClass + "." + VersionUtil.getServerVersion() + "." + className);
	}

	public static Class<?> getCraftBukkitClass(String className) {
		return findClass(cbClass + "." + VersionUtil.getServerVersion() + "." + className);
	}

	protected static SimpleCommandMap getCommandMap() {
		try {
			return (SimpleCommandMap) getCraftBukkitClass("CraftServer").getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
			Common.error(ex, null, "Unable to get CommandMap in Bukkit NMS", false, true);
			return null;
		}
	}

	public static void registerCmd(Command command) {
		CommandMap commandMap = getCommandMap();
		if (commandMap != null)
			commandMap.register(command.getLabel(), command);
		ValidUtil.checkBoolean(command.isRegistered(), "Could not properly register command " + command.getLabel());
	}
}
