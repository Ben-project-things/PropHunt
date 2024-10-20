package me.sammy.farmhunt.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;

public class FallDamageListener implements Listener {

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
        event.setCancelled(true); // Cancel fall damage
      }
    }
  }
}
