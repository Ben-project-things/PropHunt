package me.sammy.farmhunt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sammy.farmhunt.game.Game;
import me.sammy.farmhunt.game.GameManager;
import me.sammy.farmhunt.lobby.LobbyManager;

/**
 * Class that deals with all the commands related to games.
 */
public class GameCommands implements CommandExecutor {

  private final GameManager gameManager;
  private final LobbyManager lobbyManager;

  public GameCommands(GameManager gameManager, LobbyManager lobbyManager) {
    this.gameManager = gameManager;
    this.lobbyManager = lobbyManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;

      switch (label.toLowerCase()) {
        case "joingame":
          if (gameManager.getGamePlayers().contains(player)) {
            player.sendMessage("§6[§bFH§6] §cYou are already in a game!.");
          } else {
            gameManager.addPlayerToGame(player);
          }
          return true;

        case "startgame":
          if (gameManager.getGamePlayers().contains(player)) {
            if (player.isOp()) {
              if (!gameManager.isMapSelected()) {
                player.sendMessage("§6[§bFH§6] §cNo map has been selected. Use /vote or /forcemap.");
                return true;
              }
              gameManager.startGame();
            } else {
              player.sendMessage("§6[§bFH§6] §cYou do not have permission to start the game.");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to force start it.");
          }
          return true;

        case "endgame":
          if (gameManager.getGamePlayers().contains(player)) {
            if (player.isOp()) {
              Game game = gameManager.getCurrentGame();
              if (game != null) {
                game.endGame();
              }
              else {
                player.sendMessage("§6[§bFH§6] §cThere is no game to end.");
              }
            } else {
              player.sendMessage("§6[§bFH§6] §cYou do not have permission to end the game.");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to force end it.");
          }
          return true;

        case "vote":
          if (gameManager.getGamePlayers().contains(player)) {
            if (args.length == 1) {
              gameManager.voteMap(player, args[0]);
            } else {
              player.sendMessage("§6[§bFH§6] §aUsage: §7/vote <mapname>");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to vote.");
          }
          return true;

        case "setgametime":
          if (gameManager.getGamePlayers().contains(player)) {
            if (player.isOp() && args.length == 1) {
              try {
                int time = Integer.parseInt(args[0]);
                gameManager.setGameTime(time);
                player.sendMessage("§6[§bFH§6] §aGame time set to §7" + time + " §aseconds.");
              } catch (NumberFormatException e) {
                player.sendMessage("§6[§bFH§6] §cInvalid time format.");
              }
            } else {
              player.sendMessage("§6[§bFH§6] §aUsage: §7/setgametime <seconds>");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to change the time of the game.");
          }
          return true;

        case "forcemap":
          if (gameManager.getGamePlayers().contains(player)) {
            if (player.isOp() && args.length == 1) {
              gameManager.forceSelectMap(args[0]);
            } else {
              player.sendMessage("§6[§bFH§6] §aUsage: §7/forcemap <mapname>");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to choose a map.");
          }
          return true;

        case "leavegame":
          if (gameManager.isPlayerInGame(player)) {
            gameManager.removePlayerFromGame(player);
          } else {
            player.sendMessage("§6[§bFH§6] §cYou aren't in a game.");
          }
          return true;

        case "setteamassignment":
          if (gameManager.getGamePlayers().contains(player)) {
            if (player.isOp() && args.length == 1) {
              boolean manual = args[0].equalsIgnoreCase("manual");
              gameManager.setManualTeamAssignment(manual);
              player.sendMessage("§6[§bFH§6] §aTeam assignment set to §7" + (manual ? "manual" : "automatic"));
            } else {
              player.sendMessage("§6[§bFH§6] §aUsage: §a/setteamassignment §7<manual|automatic>");
            }
          } else {
            player.sendMessage("§6[§bFH§6] §cYou must be in a game to choose team assignment.");
          }
          return true;

        case "createmap":
          if (player.isOp()) {
            if (args.length != 1) {
              player.sendMessage("§6[§bFH§6] §7Usage: /createmap <mapname>");
              return true;
            }
            gameManager.createMap(args[0], player);
            return true;
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to create a map.");
          }
          return true;

        case "undomap":
          if (player.isOp()) {
            gameManager.undoMapCreation(player);
            return true;
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to undo map creation.");
          }
          return true;

        case "deletemap":
          if (player.isOp()) {
            if (args.length != 1) {
              player.sendMessage("§6[§bFH§6] §7Usage: /deletemap <mapname>");
              return true;
            }
            gameManager.deleteMap(args[0], player);
            return true;
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to delete maps.");
          }
          return true;

        case "setlobbyspawn":
          if (player.isOp()) {
            lobbyManager.setLobbySpawn(player.getLocation());
            player.sendMessage("§6[§bFH§6] §aLobby spawn location set.");
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to use this command.");
          }
          return true;

        case "setwaitingspawn":
          if (player.isOp()) {
            gameManager.setWaitingSpawn(player.getLocation());
            player.sendMessage("§6[§bFH§6] §aWaiting spawn location set.");
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to use this command.");
          }
          return true;

        case "setspawnloc":
          if (player.isOp()) {
            gameManager.setSpawnLocation(player);
            return true;
          } else {
            player.sendMessage("§6[§bFH§6] §cYou do not have permission to set spawn locations.");
          }
          return true;

        case "help":
          displayHelp(player);
          return true;

        default:
          return false;
      }
    }

    return false;
  }

  private void displayHelp(Player player) {
    player.sendMessage("§6[§bFH§6] §aAvailable Commands:");

    player.sendMessage("§a/joingame §7- Join the game.");
    player.sendMessage("§a/vote §7<mapname> - Vote for a map.");
    player.sendMessage("§a/leavegame §7- Leave the game.");

    if (player.isOp()) {
      player.sendMessage("§6[§bFH§6] §cOperator Commands:");
      player.sendMessage("§a/startgame §7- Start the game.");
      player.sendMessage("§a/endgame §7- Ends the game.");
      player.sendMessage("§a/setgametime §7<seconds> - Set the game time.");
      player.sendMessage("§a/forcemap §7<mapname> - Force select a map.");
      player.sendMessage("§a/setteamassignment §7<manual|automatic> - Set team assignment mode.");
      player.sendMessage("§a/createmap §7<mapname> - Create a new map.");
      player.sendMessage("§a/setspawnloc §7- Set the spawn location for the map.");
      player.sendMessage("§a/undomap §7- Undo map creation.");
      player.sendMessage("§a/deletemap §7<mapname> - Delete a map.");
      player.sendMessage("§a/setlobbyspawn - §7Sets the spawn for the lobby.");
      player.sendMessage("§a/setwaitingspawn - §7Sets the spawn for the waiting room.");
    }
  }
}
