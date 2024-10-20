package me.sammy.farmhunt.lobby;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.sammy.farmhunt.game.GameManager;

/**
 * Class that deals with when a player is in the Lobby.
 */
public class LobbyManager {

  private final Set<Player> lobbyPlayers = new HashSet<>();
  private final Map<Player, PlayerLoadout> playerLoadouts = new HashMap<>();
  private Location lobbySpawn;

  public LobbyManager() {
    this.lobbySpawn = new Location(Bukkit.getWorld("world"), 0, 0, 0);
  }

  public void addPlayerToLobby(Player player) {
    player.teleport(lobbySpawn);
    player.setGameMode(GameMode.ADVENTURE);

    Inventory inventory = player.getInventory();
    inventory.clear();
    ItemStack compass = new ItemStack(Material.COMPASS);
    ItemMeta meta = compass.getItemMeta();
    meta.setDisplayName("§aLoadout Selector");
    compass.setItemMeta(meta);
    inventory.addItem(compass);

    if (!playerLoadouts.containsKey(player)) {
      playerLoadouts.put(player, new PlayerLoadout());
    }

    lobbyPlayers.add(player);
    player.sendMessage("§6[§bFH§6] §aYou are in the main lobby. Type /joingame to join a game.");
  }

  public void removePlayerFromLobby(Player player) {
    lobbyPlayers.remove(player);
  }

  public boolean isPlayerInLobby(Player player) {
    return lobbyPlayers.contains(player);
  }

  public PlayerLoadout getPlayerLoadout(Player player) {
    return playerLoadouts.get(player);
  }

  public void openLoadoutGUI(Player player) {
    new LoadoutSelectionGUI(this).openMainGUI(player);
  }

  public void setLobbySpawn(Location location) {
    this.lobbySpawn = location;
  }
}
