package com.sergimarrahyarenas.bloodstats.models.characterequipment

data class Set(
    val display_string: String,
    val effects: List<ItemSetEffect>,
    val item_set: ItemSet,
    val items: List<Item>
)