package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class CharacterEquipment(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Links,
    val character: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Character,
    val equipped_item_sets: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItemSet>,
    val equipped_items: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem>
)