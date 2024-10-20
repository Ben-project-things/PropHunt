package me.sammy.farmhunt.events;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.sammy.farmhunt.lobby.LobbyManager;

/**
 * Class that deals with the logic of players joining the server.
 */
public class PlayerJoinListener implements Listener {

  private final LobbyManager lobbyManager;

  public PlayerJoinListener(LobbyManager lobbyManager) {
    this.lobbyManager = lobbyManager;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    lobbyManager.addPlayerToLobby(player);
  }

  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent event) {
    if (event.getEntity() instanceof Player) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
    event.setCancelled(true);
  }
}
