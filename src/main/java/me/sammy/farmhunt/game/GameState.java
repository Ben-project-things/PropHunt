package me.sammy.farmhunt.game;

/**
 * Represents the states a game can be in, whether waiting in lobby, countdown to starting,
 * currently running, or finished.
 */
public enum GameState {
  WAITING,
  COUNTDOWN,
  RUNNING,
  ENDED
}
