package dev.mnewt00.nexlib.material;

import dev.mnewt00.nexlib.Common;
import dev.mnewt00.nexlib.annotation.Unsafe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class ItemConstructor {
	public static ItemStack construct(ItemStack itemStack, String name, String... lore) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(Common.color(name));
		itemMeta.setLore(Arrays.asList(lore));
		if (lore != null)
			itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getXItem(String itemName) {
		final ItemStack itemStack = XMaterial.matchXMaterial(itemName).map(XMaterial::parseItem).orElse(null);
		if (itemStack == null) {
			Common.error(null, null, "Could not find Material/Item " + itemName, false, false);
		}
		return itemStack;
	}

	@Unsafe
	public static ItemStack enchantUnsafe(ItemStack itemStack, Enchantment enchantment, Integer level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return itemStack;
	}
	public static ItemStack enchant(ItemStack itemStack, Enchantment enchantment, Integer level) {
		itemStack.addEnchantment(enchantment, level);
		return itemStack;
	}
}
