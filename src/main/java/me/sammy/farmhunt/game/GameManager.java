package me.sammy.farmhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.sammy.farmhunt.lobby.LobbyManager;

public class GameManager {

  private final LobbyManager lobbyManager;
  private final Set<Player> gamePlayers = new HashSet<>();
  private final Set<Player> hunters = new HashSet<>();
  private final Set<Player> animals = new HashSet<>();
  private final Map<String, Location> maps = new HashMap<>(); // Map name to spawn location
  private final Map<Player, String> mapVotes = new HashMap<>();
  private int minPlayers = 4;
  private String forcedMap;
  private int gameTime = 300;
  private boolean manualTeamAssignment = false;
  private Location waitingSpawn;
  private String tempMapName; // Temporary map name for map creation

  private Game currentGame;
  private final JavaPlugin plugin; // Added this field to hold the plugin reference

  public GameManager(JavaPlugin plugin, LobbyManager lobbyManager) {
    this.waitingSpawn = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    this.lobbyManager = lobbyManager;
    this.plugin = plugin; // Initialize the plugin reference
  }

  public Game getCurrentGame() {
    return currentGame;
  }

  public void addPlayerToGame(Player player) {
    if (currentGame != null && currentGame.getState() != GameState.WAITING && currentGame.getState() != GameState.COUNTDOWN) {
      player.sendMessage("The game has already started. Please wait for the next game.");
      return;
    }

    gamePlayers.add(player);
    lobbyManager.removePlayerFromLobby(player);

    for (Player p : gamePlayers) {
      p.sendMessage("§6[§bFH§6] §7" + player.getName() + " §ahas joined the game (§7" +
              gamePlayers.size() + "§a)!");
    }

    // Give hay item for team selection with meta if manual team assignment
    if (isManualTeamAssignment()) {
      giveTeamSelectionItem(player);
    }

    player.teleport(waitingSpawn);
    player.sendMessage("§6[§bFH§6] §aVote for a map by using §7/vote <mapname>.");
    displayVotingResults(player);

    if (gamePlayers.size() >= minPlayers && currentGame == null) {
      startGame();
    }
  }

  public void removePlayerFromGame(Player player) {
    for (Player p : gamePlayers) {
      p.sendMessage("§6[§bFH§6] §7" + player.getName() + " §ahas left the game (§7" +
              gamePlayers.size() + "§a)!");
    }
    gamePlayers.remove(player);
    if (currentGame != null) {
      currentGame.removePlayer(player);
    }
    getMapVotes().remove(player);
    lobbyManager.addPlayerToLobby(player);
  }

  public boolean isPlayerInGame(Player player) {
    return gamePlayers.contains(player);
  }

  public void startGame() {
    currentGame = new Game(plugin, this, lobbyManager);

    // Transfer players to the new game's sets
    hunters.forEach(currentGame.getHunters()::add);
    animals.forEach(currentGame.getAnimals()::add);

    currentGame.sortTeams(gamePlayers);
    currentGame.startCountdown();
  }

  public void setGameTime(int time) {
    this.gameTime = time;
  }

  public void forceSelectMap(String map) {
    for (Player p : gamePlayers) {
      p.sendMessage("§6[§bFH§6] §aThe map has been force selected to §7" + map);
    }
    this.forcedMap = map;
  }

  public boolean isMapSelected() {
    return forcedMap != null || !mapVotes.isEmpty();
  }

  public String getWinningMap() {
    if (forcedMap != null) {
      return forcedMap;
    }
    // Determine the map with the most votes
    Map<String, Integer> voteCount = new HashMap<>();
    for (String map : mapVotes.values()) {
      voteCount.put(map, voteCount.getOrDefault(map, 0) + 1);
    }
    return voteCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalStateException("No map votes found"))
            .getKey();
  }

  public void createMap(String mapName, Player player) {
    if (maps.containsKey(mapName)) {
      player.sendMessage("§6[§bFH§6] §cMap with this name already exists!");
      return;
    }
    tempMapName = mapName;
    player.sendMessage("§6[§bFH§6] §aMap creation started for §7" + mapName + "§a. Use /setspawnloc to set the spawn location or /undomap to cancel.");
  }

  public void setSpawnLocation(Player player) {
    if (tempMapName == null) {
      player.sendMessage("§6[§bFH§6] §cNo map creation in progress. Use /createmap <mapname> first.");
      return;
    }
    Location tempSpawnLocation = player.getLocation();
    maps.put(tempMapName, tempSpawnLocation);
    player.sendMessage("§6[§bFH§6] §aSpawn location set for §7" + tempMapName + "§a.");
    tempMapName = null;
  }

  public void undoMapCreation(Player player) {
    if (tempMapName == null) {
      player.sendMessage("§6[§bFH§6] §cNo map creation in progress to undo.");
      return;
    }
    player.sendMessage("§6[§bFH§6] §aMap creation undone for §7" + tempMapName + "§a.");
    tempMapName = null;
  }

  public void deleteMap(String mapName, Player player) {
    if (!maps.containsKey(mapName)) {
      player.sendMessage("§6[§bFH§6] §cMap not found!");
      return;
    }
    maps.remove(mapName);
    player.sendMessage("§6[§bFH§6] §aMap §7" + mapName + " §adeleted.");
  }

  public void voteMap(Player player, String map) {
    if (!maps.containsKey(map)) {
      player.sendMessage("§6[§bFH§6] §cInvalid map!");
      return;
    }

    if (mapVotes.containsKey(player)) {
      player.sendMessage("§6[§bFH§6] §cYou have already voted.");
      return;
    }

    if (forcedMap != null) {
      player.sendMessage("§6[§bFH§6] §cThe map has already been picked.");
      return;
    }

    mapVotes.put(player, map);
    getCurrentGame().broadcastMessage("§6[§bFH§6] §aVote registered. Use /voteresults to see current standings.");
  }

  public int getMinPlayers() {
    return minPlayers;
  }

  public int getGameTime() {
    return gameTime;
  }

  public Map<String, Location> getMaps() {
    return maps;
  }

  public boolean isManualTeamAssignment() {
    return manualTeamAssignment;
  }

  public void setManualTeamAssignment(boolean manual) {
    this.manualTeamAssignment = manual;
    ItemStack hayItem = new ItemStack(Material.WHEAT);
    ItemMeta hayMeta = hayItem.getItemMeta();
    hayMeta.setDisplayName("§aTeam Selector");
    hayMeta.setLore(Arrays.asList("§7Right-click to choose your team"));
    hayItem.setItemMeta(hayMeta);
    for (Player player : this.gamePlayers) {
      player.getInventory().setItem(8, hayItem);
    }
    if (!manual) {
      // Remove all players from teams when switching to automatic
      hunters.clear();
      animals.clear();

      for (Player player : this.gamePlayers) {
        player.getInventory().remove(Material.WHEAT);
      }
    }
  }

  public void setWaitingSpawn(Location location) {
    this.waitingSpawn = location;
  }

  public Location getWaitingSpawn() {
    return waitingSpawn;
  }

  public Map<Player, String> getMapVotes() {
    return mapVotes;
  }

  public Set<Player> getGamePlayers() {
    return gamePlayers;
  }

  public Set<Player> getHunters() {
    return hunters;
  }

  public Set<Player> getAnimals() {
    return animals;
  }

  public void displayVotingResults(Player player) {
    Map<String, Integer> voteCount = new HashMap<>();

    for (String map : mapVotes.values()) {
      voteCount.put(map, voteCount.getOrDefault(map, 0) + 1);
    }

    player.sendMessage("§6[§bFH§6] §aCurrent voting standings:");
    for (String map : maps.keySet()) {
      int votes = voteCount.getOrDefault(map, 0);
      player.sendMessage("§7" + map + ": §a" + votes + " votes");
    }
  }

  private void giveTeamSelectionItem(Player player) {
    ItemStack hayItem = new ItemStack(Material.WHEAT);
    ItemMeta hayMeta = hayItem.getItemMeta();
    hayMeta.setDisplayName("§aTeam Selector");
    hayMeta.setLore(Arrays.asList("§7Right-click to choose your team"));
    hayItem.setItemMeta(hayMeta);
    player.getInventory().setItem(8, hayItem); // Slot 9 in inventory
  }

  public List<String> getHuntersLore() {
    List<String> lore = new ArrayList<>();
    lore.add("§7Current Hunters:");
    for (Player player : hunters) {
      lore.add("§a" + player.getName());
    }
    return lore;
  }

  public List<String> getAnimalsLore() {
    List<String> lore = new ArrayList<>();
    lore.add("§7Current Animals:");
    for (Player player : animals) {
      lore.add("§a" + player.getName());
    }
    return lore;
  }
}
