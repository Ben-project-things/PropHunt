package me.sammy.farmhunt.lobby;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import me.sammy.farmhunt.utils.ItemUtils;

/**
 * Data structure that deals with player loadouts and what they currently have selected.
 */
public class PlayerLoadout {
  private final List<ItemStack> hunterPerks;
  private final List<ItemStack> hunterSkills;
  private final List<ItemStack> hunterWeapons;
  private final List<ItemStack> animalPerks;
  private final List<ItemStack> animalSkills;
  private final List<ItemStack> animalWeapons;

  public PlayerLoadout() {
    this.hunterPerks = new ArrayList<>();
    this.hunterSkills = new ArrayList<>();
    this.hunterWeapons = new ArrayList<>();
    this.animalPerks = new ArrayList<>();
    this.animalSkills = new ArrayList<>();
    this.animalWeapons = new ArrayList<>();
  }

  public void addHunterPerk(ItemStack perk) {
    hunterPerks.add(perk);
  }

  public void addHunterSkill(ItemStack skill) {
    if (hunterSkills.size() < 3) {  // Limit to 3 skills
      hunterSkills.add(skill);
    }
  }

  public void addHunterWeapon(ItemStack weapon) {
    if (hunterWeapons.size() < 2) {  // Limit to 2 weapons
      hunterWeapons.add(weapon);
    }
  }

  public void addAnimalPerk(ItemStack perk) {
    animalPerks.add(perk);
  }

  public void addAnimalSkill(ItemStack skill) {
    if (animalSkills.size() < 3) {  // Limit to 3 skills
      animalSkills.add(skill);
    }
  }

  public void addAnimalWeapon(ItemStack weapon) {
    if (animalWeapons.size() < 2) {  // Limit to 2 weapons
      animalWeapons.add(weapon);
    }
  }

  public List<ItemStack> getHunterSkills() {
    return this.hunterSkills;
  }

  public List<ItemStack> getHunterWeapons() {
    return this.hunterWeapons;
  }

  public List<ItemStack> getHunterPerks() {
    return this.hunterPerks;
  }

  public List<ItemStack> getAnimalSkills() {
    return this.animalSkills;
  }

  public List<ItemStack> getAnimalWeapons() {
    return this.animalWeapons;
  }

  public List<ItemStack> getAnimalPerks() {
    return this.animalPerks;
  }

  public List<List<ItemStack>> getHunterLoadout() {
    List<List<ItemStack>> loadout = new ArrayList<>();
    loadout.add(hunterWeapons);
    loadout.add(hunterSkills);
    loadout.add(hunterPerks);
    return loadout;
  }

  public List<List<ItemStack>> getAnimalLoadout() {
    List<List<ItemStack>> loadout = new ArrayList<>();
    loadout.add(animalWeapons);
    loadout.add(animalSkills);
    loadout.add(animalPerks);
    return loadout;
  }

  public void removeHunterPerk(ItemStack perk) {
    removeItemFromList(hunterPerks, perk);
  }

  public void removeHunterSkill(ItemStack skill) {
    removeItemFromList(hunterSkills, skill);
  }

  public void removeHunterWeapon(ItemStack weapon) {
    removeItemFromList(hunterWeapons, weapon);
  }

  public void removeAnimalPerk(ItemStack perk) {
    removeItemFromList(animalPerks, perk);
  }

  public void removeAnimalSkill(ItemStack skill) {
    removeItemFromList(animalSkills, skill);
  }

  public void removeAnimalWeapon(ItemStack weapon) {
    removeItemFromList(animalWeapons, weapon);
  }

  private void removeItemFromList(List<ItemStack> list, ItemStack item) {
    for (int i = 0; i < list.size(); i++) {
      if (ItemUtils.areItemsEqual(list.get(i), item)) {
        list.remove(i);
        break;
      }
    }
  }
}
