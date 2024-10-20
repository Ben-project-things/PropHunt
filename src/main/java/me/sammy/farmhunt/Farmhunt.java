package me.sammy.farmhunt;

import org.bukkit.plugin.java.JavaPlugin;

import me.sammy.farmhunt.commands.GameCommands;
import me.sammy.farmhunt.events.FallDamageListener;
import me.sammy.farmhunt.events.InventoryClickListener;
import me.sammy.farmhunt.events.ItemInteractListener;
import me.sammy.farmhunt.events.PlayerDeathListener;
import me.sammy.farmhunt.events.PlayerDropListener;
import me.sammy.farmhunt.events.PlayerHitListener;
import me.sammy.farmhunt.events.PlayerJoinListener;
import me.sammy.farmhunt.events.PlayerQuitListener;
import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.lobby.LobbyManager;

/**
 * Represents the initialization
 */
public final class Farmhunt extends JavaPlugin {

  private GameManager gameManager;
  private LobbyManager lobbyManager;


  @Override
    public void onEnable() {
      lobbyManager = new LobbyManager();
      gameManager = new GameManager(this, lobbyManager);

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(lobbyManager), this);
        getServer().getPluginManager().registerEvents(
                new PlayerQuitListener(lobbyManager, gameManager), this);
        getServer().getPluginManager().registerEvents(
                new ItemInteractListener(lobbyManager, gameManager), this);
        getServer().getPluginManager().registerEvents(
                new InventoryClickListener(lobbyManager, gameManager), this);
        getServer().getPluginManager().registerEvents(
                new PlayerDropListener(lobbyManager, gameManager), this);
        getServer().getPluginManager().registerEvents(
            new PlayerDeathListener(gameManager), this);
        getServer().getPluginManager().registerEvents(
            new PlayerHitListener(gameManager), this);
        getServer().getPluginManager().registerEvents(
                new FallDamageListener(), this);

    // Register commands
    registerCommands(new String[]{"joingame", "startgame", "endgame", "vote", "setgametime",
            "forcemap", "leavegame", "setteamassignment", "help", "createmap",
            "setspawnloc", "undomap", "deletemap", "setlobbyspawn", "setwaitingspawn"},
            new GameCommands(gameManager, lobbyManager));

      getLogger().info("Farm hunt plugin has been enabled!");
    }

    @Override
    public void onDisable() {

      // Server clean up stuff
      getServer().getOnlinePlayers().forEach(player -> lobbyManager.removePlayerFromLobby(player));

      getLogger().info("Farm hunt plugin has been disabled!");
    }

  /**
   * Method that streamlines setting up and registering commands.
   * @param commands are the commands to register
   * @param executor is the gameCommands executor
   */
  private void registerCommands(String[] commands, GameCommands executor) {
    for (String command : commands) {
      if (getCommand(command) != null) {
        getCommand(command).setExecutor(executor);
      } else {
        getLogger().warning("Command " + command + " is not defined in plugin.yml");
      }
    }
  }
}
