package me.sammy.farmhunt.events;

import me.sammy.farmhunt.game.Game;
import me.sammy.farmhunt.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class PlayerDeathListener implements Listener {

  private final GameManager gameManager;

  public PlayerDeathListener(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    Game game = gameManager.getCurrentGame();

    if (game != null && game.isPlayerInGame(player)) {
      Player killer = player.getKiller();
      game.onPlayerKilled(player, killer);
    }
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    Game game = gameManager.getCurrentGame();

    if (game != null && game.isPlayerInGame(player)) {
      Location spawnLocation = gameManager.getMaps().get(gameManager.getWinningMap());
      event.setRespawnLocation(spawnLocation);
    }
  }
}
