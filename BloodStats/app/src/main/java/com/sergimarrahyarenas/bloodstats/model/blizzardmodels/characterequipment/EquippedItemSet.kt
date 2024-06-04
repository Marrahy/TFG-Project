package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class EquippedItemSet(
    val display_string: String,
    val effects: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Effect>,
    val item_set: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.ItemSet,
    val items: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Item>
)