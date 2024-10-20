package me.sammy.farmhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;

public class GameScoreboard {

  private final JavaPlugin plugin;
  private final ScoreboardManager manager;
  private final Scoreboard scoreboard;
  private final Objective objective;
  private final Map<Player, BukkitRunnable> tasks = new HashMap<>();

  public GameScoreboard(JavaPlugin plugin) {
    this.plugin = plugin;
    this.manager = Bukkit.getScoreboardManager();
    this.scoreboard = manager.getNewScoreboard();
    this.objective = scoreboard.registerNewObjective("game", "dummy", "§6[§bFH§6]");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
  }

  public void initialize(Player player, String map, int hunters, int animals, int startTime) {
    // Clear previous scores
    scoreboard.getEntries().forEach(scoreboard::resetScores);

    objective.getScore(ChatColor.GREEN + "Map: " + ChatColor.WHITE + map).setScore(4);
    objective.getScore(ChatColor.GREEN + "Time: " + ChatColor.WHITE + startTime).setScore(3);
    objective.getScore(ChatColor.RED + "Hunters: " + ChatColor.WHITE + hunters).setScore(2);
    objective.getScore(ChatColor.AQUA + "Animals: " + ChatColor.WHITE + animals).setScore(1);
    player.setScoreboard(scoreboard);
  }

  public void startUpdating(final Player player, final String map, final int gameTime, int hunters, int animals) {
    if (tasks.containsKey(player)) {
      tasks.get(player).cancel();
    }

    BukkitRunnable task = new BukkitRunnable() {
      int timeLeft = gameTime;

      @Override
      public void run() {
        if (timeLeft <= 0) {
          cancel();
        }
        update(player, map, timeLeft, hunters, animals);
        timeLeft--;
      }
    };

    tasks.put(player, task);
    task.runTaskTimer(plugin, 0L, 20L); // Update every second
  }

  public void stopUpdating(Player player) {
    if (tasks.containsKey(player)) {
      tasks.get(player).cancel();
      tasks.remove(player);
    }
  }

  private void update(Player player, String map, int timeLeft, int hunters, int animals) {
    // Clear previous scores
    scoreboard.getEntries().forEach(scoreboard::resetScores);

    objective.getScore(ChatColor.GREEN + "Map: " + ChatColor.WHITE + map).setScore(4);
    objective.getScore(ChatColor.GREEN + "Time: " + ChatColor.WHITE + formatTime(timeLeft)).setScore(3);
    objective.getScore(ChatColor.RED + "Hunters: " + ChatColor.WHITE + hunters).setScore(2);
    objective.getScore(ChatColor.AQUA + "Animals: " + ChatColor.WHITE + animals).setScore(1);
    player.setScoreboard(scoreboard);
  }

  private String formatTime(int seconds) {
    int minutes = seconds / 60;
    int secs = seconds % 60;
    return String.format("%d:%02d", minutes, secs);
  }
}
