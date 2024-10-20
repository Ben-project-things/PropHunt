package me.sammy.farmhunt.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Class for making the different skills.
 */
public class Skills {

  public static ItemStack createSkillItem(String name, Material material, String... lore) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);
    return item;
  }

  public static ItemStack animalDelayedWarpSkill() {
    return createSkillItem(
            "§5Delayed Warp",
            Material.END_PORTAL_FRAME,
            "§7This skill waits 5 seconds",
            "§7before warping you back",
            "§7to where you were 5 seconds before."
    );
  }

  public static ItemStack animalSmokeBomb() {
    return createSkillItem(
            "§8Smoke Bomb",
            Material.FIREWORK_STAR,
            "§7This skill allows you to",
            "§7throw a smoke bomb."
    );
  }

  public static ItemStack hunterChickenRadar() {
    return createSkillItem(
            "§8Chicken Radar",
            Material.CHICKEN_SPAWN_EGG,
            "§7This skill allows you to",
            "§7throw a chicken which will cluck",
            "§7when hiders are nearby."
    );
  }


}
