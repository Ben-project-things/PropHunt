package me.sammy.farmhunt.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Class for making the different perks.
 */
public class Perks {

  public static ItemStack createPerkItem(String name, Material material, String... lore) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);
    return item;
  }

  public static ItemStack animalQuickEscapePerk() {
    return createPerkItem(
            "§bQuick Escape",
            Material.LIGHT_BLUE_DYE,
            "§7This perk allows you to",
            "§7quickly escape from danger",
            "§7with a burst of speed when hit."
    );
  }

  public static ItemStack animalHBFSPerk() {
    return createPerkItem(
            "§eHarder, Better, Faster, Stronger",
            Material.YELLOW_DYE,
            "§7This perk gives you a permanent",
            "§7strength boost everytime you kill",
            "§7a hunter."
    );
  }

  public static ItemStack hunterLockedInPerk() {
    return createPerkItem(
            "§4Locked In",
            Material.RED_DYE,
            "§7This perk gives you a speed",
            "§7boost everytime you hit",
            "§7an animal."
    );
  }

  public static ItemStack hunterNBA() {
    return createPerkItem(
            "§cAir Jordan",
            Material.WHITE_DYE,
            "§7This perk gives you a pair",
            "§7of Air Jordan, giving you permanent",
            "§7Jump boost."
    );
  }
}
