package me.sammy.farmhunt.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.entity.Player;

import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.lobby.LobbyManager;

public class PlayerDropListener implements Listener {

  private final LobbyManager lobbyManager;
  private final GameManager gameManager;

  public PlayerDropListener(LobbyManager lobbyManager, GameManager gameManager) {
    this.lobbyManager = lobbyManager;
    this.gameManager = gameManager;
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    if (lobbyManager.isPlayerInLobby(player) || gameManager.isPlayerInGame(player)) {
      event.setCancelled(true);
    }
  }
}