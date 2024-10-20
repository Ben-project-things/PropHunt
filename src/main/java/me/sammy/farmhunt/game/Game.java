package me.sammy.farmhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import me.sammy.farmhunt.lobby.LobbyManager;
import me.sammy.farmhunt.lobby.PlayerLoadout;

import java.util.*;

public class Game {

  private final GameManager gameManager;
  private final LobbyManager lobbyManager;
  private final JavaPlugin plugin;

  private final Set<Player> hunters = new HashSet<>();
  private final Set<Player> animals = new HashSet<>();
  private GameState state = GameState.WAITING;
  private BukkitTask countdownTask;
  private BukkitTask gameTask;
  private GameScoreboard gameScoreboard;

  public Game(JavaPlugin plugin, GameManager gameManager, LobbyManager lobbyManager) {
    this.gameManager = gameManager;
    this.lobbyManager = lobbyManager;
    this.plugin = plugin;
    this.gameScoreboard = new GameScoreboard(this.plugin);
  }

  public GameState getState() {
    return state;
  }

  public void removePlayer(Player player) {
    hunters.remove(player);
    animals.remove(player);
  }

  public void broadcastMessage(String message) {
    for (Player player : gameManager.getGamePlayers()) {
      player.sendMessage(ChatColor.GREEN + message);
    }
  }

  public void startCountdown() {
    if (state != GameState.WAITING) {
      return;
    }

    state = GameState.COUNTDOWN;
    for (Player player : gameManager.getGamePlayers()) {
      player.getInventory().clear();
    }

    countdownTask = new BukkitRunnable() {
      int countdown = 10;

      @Override
      public void run() {
        if (countdown <= 0) {
          startGame(gameManager.getWinningMap());
          this.cancel();
        } else {
          broadcastMessage("§6[§bFH§6] §aGame starting in §7" + countdown + " §aseconds...");
          countdown--;
        }
      }
    }.runTaskTimer(this.plugin, 0L, 20L);
  }

  public void startGame(String chosenMap) {
    state = GameState.RUNNING;
    broadcastMessage("§6[§bFH§6] §aThe game is starting on §7" + chosenMap + "§a!");

    // Teleport animals to the game map
    Location spawnLocation = gameManager.getMaps().get(chosenMap);
    if (spawnLocation == null) {
      broadcastMessage("§6[§bFH§6] §cInvalid map location. Game cannot start.");
      endGame();
      return;
    }

    for (Player player : animals) {
      player.teleport(spawnLocation);
      player.sendMessage("§6[§bFH§6] §aYou have spawned as an animal, you have 15 seconds to hide.");
      assignLoadout(player);
    }

    for (Player player : hunters) {
      player.sendMessage("§6[§bFH§6] §cYou have spawned as a hunter, you will be released in 15 seconds.");
      assignLoadout(player);
    }

    // Start the actual game countdown for the scoreboard
    for (Player player : gameManager.getGamePlayers()) {
      gameScoreboard.initialize(player, gameManager.getWinningMap(), hunters.size(),
              animals.size(), gameManager.getGameTime());
    }

    // Start a delayed task to teleport hunters after 15 seconds
    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
      for (Player player : hunters) {
        player.teleport(spawnLocation);
      }

      // Start the actual game timer
      startGameTimer();

    }, 15 * 20L);

    // Separate countdown for title display
    countdownTask = new BukkitRunnable() {
      int countdown = 15;

      @Override
      public void run() {
        if (countdown <= 0) {
          this.cancel();
        } else {
          for (Player player : gameManager.getGamePlayers()) {
            player.sendTitle("§e" + countdown, "", 0, 20, 0);
          }
          countdown--;
        }
      }
    }.runTaskTimer(this.plugin, 0L, 20L);
  }

  private void startGameTimer() {
    broadcastMessage("§6[§bFH§6] §aThe game has begun!");

    // Start updating the scoreboard
    for (Player player : gameManager.getGamePlayers()) {
      gameScoreboard.startUpdating(player, gameManager.getWinningMap(), gameManager.getGameTime(), hunters.size(), animals.size());
    }

    // Schedule game end
    gameTask = new BukkitRunnable() {
      @Override
      public void run() {
        endGame();
      }
    }.runTaskLater(this.plugin, gameManager.getGameTime() * 20L);
  }

  private void assignLoadout(Player player) {
    PlayerLoadout loadout = lobbyManager.getPlayerLoadout(player);
    List<List<ItemStack>> items = new ArrayList<>();

    if (animals.contains(player)) {
      items = loadout.getAnimalLoadout();
      ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
      ItemMeta blazeMeta = blazeRod.getItemMeta();
      blazeMeta.setDisplayName("§eTransform Wand");
      blazeMeta.addEnchant(Enchantment.MENDING, 1, true);
      blazeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      blazeRod.setItemMeta(blazeMeta);
      player.getInventory().setItem(0, blazeRod); // Transform Wand
    } else if (hunters.contains(player)) {
      items = loadout.getHunterLoadout();
    }

    setLoadout(player, items, animals.contains(player));
  }

  private void setLoadout(Player player, List<List<ItemStack>> items, boolean isAnimal) {
    int[] weaponSlots = isAnimal ? new int[]{1, 2} : new int[]{0, 1};
    int[] skillSlots = {3, 4, 5};
    int[] perkSlots = {7, 8};

    // Fill weapon slots
    int weaponIndex = 0;
    for (ItemStack weapon : items.get(0)) {
      if (weaponIndex < weaponSlots.length) {
        player.getInventory().setItem(weaponSlots[weaponIndex++], weapon);
      }
    }

    // Fill remaining weapon slots with air
    while (weaponIndex < weaponSlots.length) {
      player.getInventory().setItem(weaponSlots[weaponIndex++], new ItemStack(Material.AIR));
    }

    // Fill skill slots
    int skillIndex = 0;
    for (ItemStack skill : items.get(1)) {
      if (skillIndex < skillSlots.length) {
        player.getInventory().setItem(skillSlots[skillIndex++], skill);
      }
    }

    // Fill remaining skill slots with air
    while (skillIndex < skillSlots.length) {
      player.getInventory().setItem(skillSlots[skillIndex++], new ItemStack(Material.AIR));
    }

    // Fill perk slots
    int perkIndex = 0;
    for (ItemStack perk : items.get(2)) {
      if (perkIndex < perkSlots.length) {
        player.getInventory().setItem(perkSlots[perkIndex++], perk);
      }
    }

    // Fill remaining perk slots with air
    while (perkIndex < perkSlots.length) {
      player.getInventory().setItem(perkSlots[perkIndex++], new ItemStack(Material.AIR));
    }
  }

  public void endGame() {
    state = GameState.ENDED;
    broadcastMessage("§6[§bFH§6] §aThe game has ended!");

    // Clear game data and reset
    for (Player player : gameManager.getGamePlayers()) {
      player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
      lobbyManager.addPlayerToLobby(player);
      gameScoreboard.stopUpdating(player);
    }
    gameManager.getGamePlayers().clear();
    gameManager.getHunters().clear();
    gameManager.getAnimals().clear();
    gameManager.getMapVotes().clear();
    gameManager.forceSelectMap(null); // Reset forced map
    state = GameState.WAITING;

    // Cancel ongoing tasks
    if (countdownTask != null && !countdownTask.isCancelled()) {
      countdownTask.cancel();
    }
    if (gameTask != null && !gameTask.isCancelled()) {
      gameTask.cancel();
    }
  }

  public void sortTeams(Set<Player> gamePlayers) {
    List<Player> players = new ArrayList<>(gamePlayers);
    Collections.shuffle(players);
    for (Player player : players) {
      if (!hunters.contains(player) && !animals.contains(player)) {
        if (gameManager.isManualTeamAssignment()) {
          player.sendMessage("§6[§bFH§6] §cYou were not assigned to a team. Assigning you randomly.");
        }
        if (hunters.size() < animals.size() / 3 || hunters.isEmpty()) {
          hunters.add(player);
        } else {
          animals.add(player);
        }
      }
    }
  }

  public boolean isPlayerInGame(Player player) {
    return gameManager.getGamePlayers().contains(player);
  }

  public Set<Player> getHunters() {
    return hunters;
  }

  public Set<Player> getAnimals() {
    return animals;
  }

  public boolean isAnimal(Player player) {
    return animals.contains(player);
  }

  public boolean isHunter(Player player) {
    return hunters.contains(player);
  }

  public void onPlayerKilled(Player player, Player killer) {
    Location spawnLocation = gameManager.getMaps().get(gameManager.getWinningMap());

    if (animals.contains(killer) && hunters.contains(player)) {
      broadcastMessage("§6[§bFH§6] §7" + player.getName() + " §awas killed when §7" + killer.getName() + " §afought back!");

      player.teleport(spawnLocation);
      player.setGameMode(GameMode.SPECTATOR);
      new BukkitRunnable() {
        int countdown = 3;

        @Override
        public void run() {
          if (countdown <= 0) {
            player.teleport(spawnLocation);
            player.setGameMode(GameMode.ADVENTURE);
            this.cancel();
          } else {
            player.sendTitle("§e" + countdown, "", 0, 20, 0);
            countdown--;
          }
        }
      }.runTaskTimer(plugin, 0L, 20L); // 1 second interval
    } else {
      animals.remove(player);
      hunters.add(player);
      broadcastMessage("§6[§bFH§6] §7" + player.getName() + " §ahas been turned into a hunter by §7" + killer.getName() + "§a!");

      player.setGameMode(GameMode.SPECTATOR);
      new BukkitRunnable() {
        int countdown = 3;

        @Override
        public void run() {
          if (countdown <= 0) {
            player.teleport(spawnLocation);
            assignLoadout(player);
            player.setGameMode(GameMode.ADVENTURE);
            this.cancel();
          } else {
            player.sendTitle("§e" + countdown, "", 0, 20, 0);
            countdown--;
          }
        }
      }.runTaskTimer(plugin, 0L, 20L); // 1 second interval
    }
  }
}
