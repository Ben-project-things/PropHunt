package me.sammy.farmhunt.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Class for making the different weapons.
 */
public class Weapons {

  public static ItemStack createWeaponItem(String name, Material material, String... lore) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);
    return item;
  }

  public static ItemStack animalCorkedBat() {
    ItemStack item = createWeaponItem(
            "§6Corked Bat",
            Material.STICK,
            "§7A weapon with a low damage",
            "§7but high knockback output."
    );
    ItemMeta meta = item.getItemMeta();
    meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    item.setItemMeta(meta);
    return item;
  }

  public static ItemStack hunterPointyKnife() {
    ItemStack item = createWeaponItem(
            "§6Pointy Knife",
            Material.WOODEN_SWORD,
            "§7Very effective in Britain."
    );
    ItemMeta meta = item.getItemMeta();
    meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    item.setItemMeta(meta);
    return item;
  }

}
