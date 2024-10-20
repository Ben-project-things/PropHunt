package me.sammy.farmhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TeamSelectionGUI implements Listener {

  private final GameManager gameManager;

  public TeamSelectionGUI(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  public void openTeamSelectionGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, 27, "Select Your Team");

    ItemStack hunterItem = new ItemStack(Material.IRON_SWORD);
    ItemMeta hunterMeta = hunterItem.getItemMeta();
    hunterMeta.setDisplayName("§cHunter");
    hunterMeta.setLore(gameManager.getHuntersLore());
    hunterMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    hunterItem.setItemMeta(hunterMeta);
    inv.setItem(11, hunterItem);

    ItemStack animalItem = new ItemStack(Material.GRASS_BLOCK);
    ItemMeta animalMeta = animalItem.getItemMeta();
    animalMeta.setDisplayName("§aAnimal");
    animalMeta.setLore(gameManager.getAnimalsLore());
    animalItem.setItemMeta(animalMeta);
    inv.setItem(15, animalItem);

    player.openInventory(inv);
  }
}
