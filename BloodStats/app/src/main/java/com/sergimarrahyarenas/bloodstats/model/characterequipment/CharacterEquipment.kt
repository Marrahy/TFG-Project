package com.sergimarrahyarenas.bloodstats.model.characterequipment

data class CharacterEquipment(
    val _links: Links,
    val character: Character,
    val equipped_item_sets: List<EquippedItemSet>,
    val equipped_items: List<EquippedItem>
)