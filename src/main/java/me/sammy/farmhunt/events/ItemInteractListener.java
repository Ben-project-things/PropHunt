package me.sammy.farmhunt.events;

import me.sammy.farmhunt.lobby.LobbyManager;
import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.game.TeamSelectionGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

public class ItemInteractListener implements Listener {

  private final LobbyManager lobbyManager;
  private final GameManager gameManager;
  private final TeamSelectionGUI teamSelectionGUI;

  public ItemInteractListener(LobbyManager lobbyManager, GameManager gameManager) {
    this.lobbyManager = lobbyManager;
    this.gameManager = gameManager;
    this.teamSelectionGUI = new TeamSelectionGUI(gameManager); // Initialize TeamSelectionGUI
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      Player player = event.getPlayer();
      ItemStack item = event.getItem();

      if (item == null || item.getType() == Material.AIR) {
        return;
      }

      if (item.getType() == Material.WHEAT) { // Check if the item is wheat
        teamSelectionGUI.openTeamSelectionGUI(player); // Open the Team Selection GUI
        return;
      }

      if (item.getType() == Material.COMPASS) {
        lobbyManager.openLoadoutGUI(player);
      }
    }
  }

  private void handleGameInteraction(PlayerInteractEvent event, Player player, ItemStack item) {
    if (item.getType() == Material.COBBLESTONE) {
      //TODO Handle game-specific item interaction
      player.sendMessage("You interacted with a game item!");
    }
    //TODO Add more game item interactions here
  }
}
