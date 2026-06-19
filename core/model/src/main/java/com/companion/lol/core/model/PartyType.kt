package com.companion.lol.core.model

enum class PartyType(val server: String, val label: String) {
  MANA("Mana", label = "Mana"),
  CRIMSON_RUSH("Crimson_Rush", label = "Crimson Rush"),
  FURY("Fury", label = "Fury"),
  ENERGY("Energy", label = "Energy"),
  HEAT("Heat", label = "Heat"),
  SHIELD("Shield", label = "Shield"),
  FEROCITY("Ferocity", label = "Ferocity"),
  RAGE("Rage", label = "Rage"),
  FLOW("Flow", label = "Flow"),
  COURAGE("Courage", label = "Courage"),
  BLOOD_WELL("Blood_Well", label = "Blood Well"),
  GRIT("Grit", label = "Grit"),
  NONE("None", label = "None");

  companion object {
    fun from(server: String?) = entries.firstOrNull { it.server == server } ?: NONE
  }
}
