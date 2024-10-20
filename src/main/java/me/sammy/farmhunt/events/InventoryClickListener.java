package me.sammy.farmhunt.events;

import me.sammy.farmhunt.game.Game;
import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.lobby.LobbyManager;
import me.sammy.farmhunt.lobby.PlayerLoadout;
import me.sammy.farmhunt.lobby.LoadoutSelectionGUI;
import me.sammy.farmhunt.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

  private final LobbyManager lobbyManager;
  private final LoadoutSelectionGUI loadoutSelectionGUI;
  private final GameManager gameManager;

  public InventoryClickListener(LobbyManager lobbyManager, GameManager gameManager) {
    this.lobbyManager = lobbyManager;
    this.loadoutSelectionGUI = new LoadoutSelectionGUI(lobbyManager);
    this.gameManager = gameManager;
  }

  public static final int[] SKILL_SLOTS = {17, 26, 35};
  public static final int[] WEAPON_SLOTS = {17, 26};
  public static final int[] PERK_SLOTS = {17, 26};

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) {
      return;
    }

    Player player = (Player) event.getWhoClicked();
    ItemStack clickedItem = event.getCurrentItem();

    if (clickedItem == null) {
      return;
    }

    if (event.getSlot() == 40) {
      event.setCancelled(true);
      return;
    }

    String inventoryTitle = event.getView().getTitle();
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);

    switch (inventoryTitle) {
      case "Select Loadout":
        handleMainSelectionClick(event, player, clickedItem);
        break;
      case "Hunter Options":
      case "Animal Options":
        handleSubSelectionClick(event, player, clickedItem, inventoryTitle.contains("Hunter"));
        break;
      case "Hunter Perks":
      case "Animal Perks":
        handlePerksSelection(event, player, clickedItem, loadout, inventoryTitle.contains("Hunter"));
        break;
      case "Hunter Skills":
      case "Animal Skills":
        handleSkillsSelection(event, player, clickedItem, loadout, inventoryTitle.contains("Hunter"));
        break;
      case "Hunter Weapons":
      case "Animal Weapons":
        handleWeaponsSelection(event, player, clickedItem, loadout, inventoryTitle.contains("Hunter"));
        break;
      case "Select Your Team":
        handleTeamSelectionClick(event, player, clickedItem);
        break;
      default:
        break;
    }
  }

  private void handleMainSelectionClick(InventoryClickEvent event, Player player, ItemStack clickedItem) {
    switch (clickedItem.getType()) {
      case IRON_SWORD:
        loadoutSelectionGUI.openHunterGUI(player);
        break;
      case GRASS_BLOCK:
        loadoutSelectionGUI.openAnimalGUI(player);
        break;
      default:
        break;
    }
    event.setCancelled(true);
  }

  private void handleSubSelectionClick(InventoryClickEvent event, Player player, ItemStack clickedItem, boolean isHunter) {
    switch (clickedItem.getType()) {
      case YELLOW_TERRACOTTA:
        if (isHunter) {
          loadoutSelectionGUI.openHunterPerksGUI(player);
        } else {
          loadoutSelectionGUI.openAnimalPerksGUI(player);
        }
        break;
      case GREEN_TERRACOTTA:
        if (isHunter) {
          loadoutSelectionGUI.openHunterSkillsGUI(player);
        } else {
          loadoutSelectionGUI.openAnimalSkillsGUI(player);
        }
        break;
      case RED_TERRACOTTA:
        if (isHunter) {
          loadoutSelectionGUI.openHunterWeaponsGUI(player);
        } else {
          loadoutSelectionGUI.openAnimalWeaponsGUI(player);
        }
        break;
      case ARROW:
        loadoutSelectionGUI.openMainGUI(player);
        break;
      default:
        break;
    }
    event.setCancelled(true);
  }

  private void handlePerksSelection(InventoryClickEvent event, Player player, ItemStack clickedItem, PlayerLoadout loadout, boolean isHunter) {
    if (clickedItem.getType() == Material.ARROW) {
      if (isHunter) {
        loadoutSelectionGUI.openHunterGUI(player);
      } else {
        loadoutSelectionGUI.openAnimalGUI(player);
      }
      event.setCancelled(true);
      return;
    }

    if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
      event.setCancelled(true);
      return;
    }

    boolean inPerkSlots = isInSlot(event.getSlot(), PERK_SLOTS);

    if (isHunter) {
      if (inPerkSlots) {
        loadout.removeHunterPerk(clickedItem);
      } else {
        if (loadout.getHunterPerks().contains(clickedItem)) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected this perk.");
          event.setCancelled(true);
          return;
        }
        if (loadout.getHunterPerks().size() >= 2) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of perks. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addHunterPerk(clickedItem);
      }
    } else {
      if (inPerkSlots) {
        loadout.removeAnimalPerk(clickedItem);
      } else {
        if (loadout.getAnimalPerks().contains(clickedItem)) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected this perk.");
          event.setCancelled(true);
          return;
        }
        if (loadout.getAnimalPerks().size() >= 2) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of perks. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addAnimalPerk(clickedItem);
      }
    }

    loadoutSelectionGUI.updatePerksGUI(player, isHunter);
    event.setCancelled(true);
  }

  private void handleSkillsSelection(InventoryClickEvent event, Player player, ItemStack clickedItem, PlayerLoadout loadout, boolean isHunter) {
    if (clickedItem.getType() == Material.ARROW) {
      if (isHunter) {
        loadoutSelectionGUI.openHunterGUI(player);
      } else {
        loadoutSelectionGUI.openAnimalGUI(player);
      }
      event.setCancelled(true);
      return;
    }

    if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
      event.setCancelled(true);
      return;
    }

    boolean inSkillSlots = isInSlot(event.getSlot(), SKILL_SLOTS);

    if (isHunter) {
      if (inSkillSlots) {
        loadout.removeHunterSkill(clickedItem);
      } else {
        if (loadout.getHunterSkills().size() >= 3) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of skills. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addHunterSkill(clickedItem);
      }
    } else {
      if (inSkillSlots) {
        loadout.removeAnimalSkill(clickedItem);
      } else {
        if (loadout.getAnimalSkills().size() >= 3) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of skills. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addAnimalSkill(clickedItem);
      }
    }

    loadoutSelectionGUI.updateSkillsGUI(player, isHunter);
    event.setCancelled(true);
  }

  private void handleWeaponsSelection(InventoryClickEvent event, Player player, ItemStack clickedItem, PlayerLoadout loadout, boolean isHunter) {
    if (clickedItem.getType() == Material.ARROW) {
      if (isHunter) {
        loadoutSelectionGUI.openHunterGUI(player);
      } else {
        loadoutSelectionGUI.openAnimalGUI(player);
      }
      event.setCancelled(true);
      return;
    }

    if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
      event.setCancelled(true);
      return;
    }

    boolean inWeaponSlots = isInSlot(event.getSlot(), WEAPON_SLOTS);

    if (isHunter) {
      if (inWeaponSlots) {
        loadout.removeHunterWeapon(clickedItem);
      } else {
        if (loadout.getHunterWeapons().contains(clickedItem)) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected this weapon.");
          event.setCancelled(true);
          return;
        }
        if (loadout.getHunterWeapons().size() >= 2) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of weapons. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addHunterWeapon(clickedItem);
      }
    } else {
      if (inWeaponSlots) {
        loadout.removeAnimalWeapon(clickedItem);
      } else {
        if (loadout.getAnimalWeapons().contains(clickedItem)) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected this weapon.");
          event.setCancelled(true);
          return;
        }
        if (loadout.getAnimalWeapons().size() >= 2) {
          player.sendMessage("§6[§bFH§6] §cYou have already selected the maximum number of weapons. " +
                  "Click the item on the far right to remove it from your loadout.");
          event.setCancelled(true);
          return;
        }
        loadout.addAnimalWeapon(clickedItem);
      }
    }

    loadoutSelectionGUI.updateWeaponsGUI(player, isHunter);
    event.setCancelled(true);
  }

  private void handleTeamSelectionClick(InventoryClickEvent event, Player player, ItemStack clickedItem) {
    switch (clickedItem.getType()) {
      case IRON_SWORD:
        if (gameManager.getAnimals().contains(player)) {
          gameManager.getAnimals().remove(player);
        }
        if (!gameManager.getHunters().contains(player)) {
          gameManager.getHunters().add(player);
          player.sendMessage("§6[§bFH§6] §aYou have joined the §cHunter §ateam.");
        } else {
          player.sendMessage("§6[§bFH§6] §cYou are already on the Hunter team.");
        }
        break;
      case GRASS_BLOCK:
        if (gameManager.getHunters().contains(player)) {
          gameManager.getHunters().remove(player);
        }
        if (!gameManager.getAnimals().contains(player)) {
          gameManager.getAnimals().add(player);
          player.sendMessage("§6[§bFH§6] §aYou have joined the §aAnimal §ateam.");
        } else {
          player.sendMessage("§6[§bFH§6] §cYou are already on the Animal team.");
        }
        break;
      default:
        break;
    }
    player.closeInventory();
    event.setCancelled(true);
  }

  private boolean isInSlot(int slot, int[] slots) {
    for (int s : slots) {
      if (s == slot) {
        return true;
      }
    }
    return false;
  }
}
