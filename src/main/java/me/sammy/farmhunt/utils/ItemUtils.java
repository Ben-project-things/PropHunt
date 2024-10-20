package me.sammy.farmhunt.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Class that deals with comparing if two items are equal.
 */
public class ItemUtils {

  public static boolean areItemsEqual(ItemStack item1, ItemStack item2) {
    if (item1 == null || item2 == null) {
      return false;
    }

    if (item1.getType() != item2.getType()) {
      return false;
    }

    ItemMeta meta1 = item1.getItemMeta();
    ItemMeta meta2 = item2.getItemMeta();

    if (meta1 == null && meta2 == null) {
      return true;
    }

    if (meta1 == null || meta2 == null) {
      return false;
    }

    return meta1.equals(meta2);
  }
}
