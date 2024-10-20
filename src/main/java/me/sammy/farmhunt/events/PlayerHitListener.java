package me.sammy.farmhunt.events;

import me.sammy.farmhunt.game.Game;
import me.sammy.farmhunt.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitListener implements Listener {

  private final GameManager gameManager;

  public PlayerHitListener(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @EventHandler
  public void onPlayerHit(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
      return;
    }

    Player damaged = (Player) event.getEntity();
    Player damager = (Player) event.getDamager();
    Game game = gameManager.getCurrentGame();

    if (game == null || !game.isPlayerInGame(damaged) || !game.isPlayerInGame(damager)) {
      event.setCancelled(true);
      return;
    }

    if ((game.isAnimal(damager) && game.isAnimal(damaged)) || (game.isHunter(damager) && game.isHunter(damaged))) {
      event.setCancelled(true);
    }
  }
}
