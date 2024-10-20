package me.sammy.farmhunt.lobby;

import me.sammy.farmhunt.items.Perks;
import me.sammy.farmhunt.items.Skills;
import me.sammy.farmhunt.items.Weapons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class LoadoutSelectionGUI {

  private static final int GUI_SIZE = 54;
  public static final int[] SKILL_SLOTS = {17, 26, 35};
  public static final int[] WEAPON_SLOTS = {17, 26};
  public static final int[] PERK_SLOTS = {17, 26};

  private final LobbyManager lobbyManager;

  public LoadoutSelectionGUI(LobbyManager lobbyManager) {
    this.lobbyManager = lobbyManager;
  }

  public void openMainGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Select Loadout");

    ItemStack hunterItem = new ItemStack(Material.IRON_SWORD);
    ItemMeta hunterMeta = hunterItem.getItemMeta();
    hunterMeta.setDisplayName("§cHunter Loadout");
    hunterMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    hunterItem.setItemMeta(hunterMeta);
    inv.setItem(21, hunterItem);

    ItemStack hiderItem = new ItemStack(Material.GRASS_BLOCK);
    ItemMeta hiderMeta = hiderItem.getItemMeta();
    hiderMeta.setDisplayName("§aAnimal Loadout");
    hiderItem.setItemMeta(hiderMeta);
    inv.setItem(23, hiderItem);

    player.openInventory(inv);
  }

  public void openHunterGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Hunter Options");

    ItemStack perksItem = new ItemStack(Material.YELLOW_TERRACOTTA);
    ItemMeta perksMeta = perksItem.getItemMeta();
    perksMeta.setDisplayName("§ePerks");
    perksItem.setItemMeta(perksMeta);
    inv.setItem(21, perksItem);

    ItemStack skillsItem = new ItemStack(Material.GREEN_TERRACOTTA);
    ItemMeta skillsMeta = skillsItem.getItemMeta();
    skillsMeta.setDisplayName("§aSkills");
    skillsItem.setItemMeta(skillsMeta);
    inv.setItem(22, skillsItem);

    ItemStack weaponsItem = new ItemStack(Material.RED_TERRACOTTA);
    ItemMeta weaponsMeta = weaponsItem.getItemMeta();
    weaponsMeta.setDisplayName("§cWeapons");
    weaponsItem.setItemMeta(weaponsMeta);
    inv.setItem(23, weaponsItem);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openAnimalGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Animal Options");

    ItemStack perksItem = new ItemStack(Material.YELLOW_TERRACOTTA);
    ItemMeta perksMeta = perksItem.getItemMeta();
    perksMeta.setDisplayName("§ePerks");
    perksItem.setItemMeta(perksMeta);
    inv.setItem(21, perksItem);

    ItemStack skillsItem = new ItemStack(Material.GREEN_TERRACOTTA);
    ItemMeta skillsMeta = skillsItem.getItemMeta();
    skillsMeta.setDisplayName("§aSkills");
    skillsItem.setItemMeta(skillsMeta);
    inv.setItem(22, skillsItem);

    ItemStack animalsItem = new ItemStack(Material.RED_TERRACOTTA);
    ItemMeta animalsMeta = animalsItem.getItemMeta();
    animalsMeta.setDisplayName("§cWeapons");
    animalsItem.setItemMeta(animalsMeta);
    inv.setItem(23, animalsItem);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openHunterPerksGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Hunter Perks");

    for (int slot : PERK_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Perks.hunterLockedInPerk());
    inv.setItem(1, Perks.hunterNBA());

    addLoadoutToPerk(inv, player, true);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openHunterSkillsGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Hunter Skills");

    for (int slot : SKILL_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Skills.hunterChickenRadar());

    addLoadoutToSkills(inv, player, true);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openHunterWeaponsGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Hunter Weapons");

    for (int slot : WEAPON_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Weapons.hunterPointyKnife());

    addLoadoutToWeapons(inv, player, true);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openAnimalPerksGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Animal Perks");

    for (int slot : PERK_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Perks.animalQuickEscapePerk());
    inv.setItem(1, Perks.animalHBFSPerk());

    addLoadoutToPerk(inv, player, false);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openAnimalSkillsGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Animal Skills");

    for (int slot : SKILL_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Skills.animalSmokeBomb());
    inv.setItem(1, Skills.animalDelayedWarpSkill());

    addLoadoutToSkills(inv, player, false);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void openAnimalWeaponsGUI(Player player) {
    Inventory inv = Bukkit.createInventory(null, GUI_SIZE, "Animal Weapons");

    for (int slot : WEAPON_SLOTS) {
      inv.setItem(slot, createPlaceholderItem());
    }

    inv.setItem(0, Weapons.animalCorkedBat());

    addLoadoutToWeapons(inv, player, false);

    ItemStack backArrow = new ItemStack(Material.ARROW);
    ItemMeta backMeta = backArrow.getItemMeta();
    backMeta.setDisplayName("§7Back");
    backArrow.setItemMeta(backMeta);
    inv.setItem(49, backArrow);

    player.openInventory(inv);
  }

  public void updatePerksGUI(Player player, boolean isHunter) {
    if (isHunter) {
      openHunterPerksGUI(player);
    } else {
      openAnimalPerksGUI(player);
    }
  }

  public void updateSkillsGUI(Player player, boolean isHunter) {
    if (isHunter) {
      openHunterSkillsGUI(player);
    } else {
      openAnimalSkillsGUI(player);
    }
  }

  public void updateWeaponsGUI(Player player, boolean isHunter) {
    if (isHunter) {
      openHunterWeaponsGUI(player);
    } else {
      openAnimalWeaponsGUI(player);
    }
  }

  private void addLoadoutToGUI(Inventory inv, Player player, boolean isHunter) {
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);

    if (isHunter) {
      for (int i = 0; i < loadout.getHunterSkills().size(); i++) {
        inv.setItem(SKILL_SLOTS[i], loadout.getHunterSkills().get(i));
      }
      for (int i = 0; i < loadout.getHunterWeapons().size(); i++) {
        inv.setItem(WEAPON_SLOTS[i], loadout.getHunterWeapons().get(i));
      }
      for (int i = 0; i < loadout.getHunterPerks().size(); i++) {
        inv.setItem(PERK_SLOTS[i], loadout.getHunterPerks().get(i));
      }
    } else {
      for (int i = 0; i < loadout.getAnimalSkills().size(); i++) {
        inv.setItem(SKILL_SLOTS[i], loadout.getAnimalSkills().get(i));
      }
      for (int i = 0; i < loadout.getAnimalWeapons().size(); i++) {
        inv.setItem(WEAPON_SLOTS[i], loadout.getAnimalWeapons().get(i));
      }
      for (int i = 0; i < loadout.getAnimalPerks().size(); i++) {
        inv.setItem(PERK_SLOTS[i], loadout.getAnimalPerks().get(i));
      }
    }
  }

  private void addLoadoutToPerk(Inventory inv, Player player, boolean isHunter) {
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);
    if (isHunter) {
      for (int i = 0; i < loadout.getHunterPerks().size(); i++) {
        inv.setItem(PERK_SLOTS[i], loadout.getHunterPerks().get(i));
      }
    } else {
      for (int i = 0; i < loadout.getAnimalPerks().size(); i++) {
        inv.setItem(PERK_SLOTS[i], loadout.getAnimalPerks().get(i));
      }
    }
  }

  private void addLoadoutToSkills(Inventory inv, Player player, boolean isHunter) {
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);
    if (isHunter) {
      for (int i = 0; i < loadout.getHunterSkills().size(); i++) {
        inv.setItem(SKILL_SLOTS[i], loadout.getHunterSkills().get(i));
      }
    } else {
      for (int i = 0; i < loadout.getAnimalSkills().size(); i++) {
        inv.setItem(SKILL_SLOTS[i], loadout.getAnimalSkills().get(i));
      }
    }
  }

  private void addLoadoutToWeapons(Inventory inv, Player player, boolean isHunter) {
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);
    if (isHunter) {
      for (int i = 0; i < loadout.getHunterWeapons().size(); i++) {
        inv.setItem(WEAPON_SLOTS[i], loadout.getHunterWeapons().get(i));
      }
    } else {
      for (int i = 0; i < loadout.getAnimalWeapons().size(); i++) {
        inv.setItem(WEAPON_SLOTS[i], loadout.getAnimalWeapons().get(i));
      }
    }
  }

  private ItemStack createPlaceholderItem() {
    ItemStack placeholder = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    ItemMeta meta = placeholder.getItemMeta();
    meta.setDisplayName("§cSelect a skill/weapon/perk");
    placeholder.setItemMeta(meta);
    return placeholder;
  }
}
