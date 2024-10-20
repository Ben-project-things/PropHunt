package me.sammy.farmhunt.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.lobby.LobbyManager;

public class PlayerQuitListener implements Listener {

  private final LobbyManager lobbyManager;
  private final GameManager gameManager;

  public PlayerQuitListener(LobbyManager lobbyManager, GameManager gameManager) {
    this.lobbyManager = lobbyManager;
    this.gameManager = gameManager;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (lobbyManager.isPlayerInLobby(player)) {
      lobbyManager.removePlayerFromLobby(player);
    } else if (gameManager.isPlayerInGame(player)) {
      gameManager.removePlayerFromGame(player);
    }
  }
}
