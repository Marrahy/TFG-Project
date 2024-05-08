package com.sergimarrahyarenas.bloodstats.models.characterequipment

data class EquippedItemSet(
    val display_string: String,
    val effects: List<Effect>,
    val item_set: ItemSet,
    val items: List<Item>
)