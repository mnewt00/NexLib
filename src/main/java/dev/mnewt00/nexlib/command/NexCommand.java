package dev.mnewt00.nexlib.command;

import dev.mnewt00.nexlib.Common;
import dev.mnewt00.nexlib.reflection.ReflectionUtil;
import dev.mnewt00.nexlib.util.ValidUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NexCommand extends Command {
	// ------------------------------------------------------------------------------------------------------------
	// Variables
	// ------------------------------------------------------------------------------------------------------------
	private String label;
	private boolean enabled;
	@Getter
	private Integer minimumArgs = 0;
	@Getter
	private static final List<NexCommand> registeredCommands = new ArrayList<>();

	//
	// Temporary Variables
	//
	protected CommandSender sender;
	protected String[] args;

	// ------------------------------------------------------------------------------------------------------------
	// NexCommand / Main
	// ------------------------------------------------------------------------------------------------------------
	protected NexCommand(String label) {
		this(parseLabel(label), parseAliases(label));
	}

	protected NexCommand(String label, final List<String> aliases) {
		super(label);

		if (aliases != null)
			setAliases(aliases);

		setLabel(label);
		registeredCommands.add(this);
	}

	private static String parseLabel(final String label) {
		ValidUtil.notNull(label, "Label cannot be null");

		return label.split("\\|")[0];
	}

	private static List<String> parseAliases(final String label) {
		final String[] aliases = label.split("\\|");

		return aliases.length > 0 ? Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length)) : new ArrayList<>();
	}


	// ------------------------------------------------------------------------------------------------------------
	// NexCommand / Main
	// ------------------------------------------------------------------------------------------------------------

	@Override
	public final boolean execute(CommandSender commandSender, String label, String[] args) {
		this.sender = commandSender;
		this.label = label;
		this.args = args;

		try {
			if (getPermission() != null)
				checkPerm(getPermission());
			if (args.length < getMinimumArgs()) {
				tell("&cIncorrect Usage: /" + label + (!getUsage().startsWith("/") ? " " + Common.removeColor(getUsage()) : ""));
				return true;
			}

			onCommand();
		} catch (Throwable throwable) {
			Common.error(throwable, null, "Failed to run command: " + label, false, true);
		}

		return false;
	}

	protected abstract void onCommand();

	// ------------------------------------------------------------------------------------------------------------
	// Utilities
	// ------------------------------------------------------------------------------------------------------------

	protected boolean hasPerm(String permission) {
		return this.sender.hasPermission(permission);
	}

	protected void checkPerm(String permission, String errorMessage) {
		if (!(this.sender instanceof Player)) return;
		if (!hasPerm(permission)) {
			this.sender.sendMessage(Common.color(errorMessage));
		}
	}

	protected void checkPerm(String permission) {
		checkPerm(permission, "&cNo permission.");
	}

	protected void setMinimumArgs(int amount) {
		ValidUtil.checkBoolean(amount >= 0, "Minimum arguments for command " + label + " needs to be 0 or greater.");
		minimumArgs = amount;
	}

	protected void tell(String... messages) {
		for (String string : messages) {
			if (string.equals("none") || string == null || string.equals("null")) sender.sendMessage("");
			else sender.sendMessage(Common.color(string));
		}
	}

	protected void tell(TextComponent component) {
		if (sender instanceof Player) ((Player) sender).spigot().sendMessage(component);
		else
		Common.log(Common.removeColor(component.toString()));
	}

	protected boolean isConsole() {
		return !(sender instanceof Player);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Overriden methods from org.bukkit.command.Command
	// ------------------------------------------------------------------------------------------------------------


	@Override
	public final String getLabel() {
		return label;
	}

	public final String getBukkitLabel() {
		return super.getLabel();
	}

	@Override
	public boolean setLabel(final String name) {
		label = name;
		return super.setLabel(name);
	}
}
