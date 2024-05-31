package com.sergimarrahyarenas.bloodstats.model.characterequipment

data class Set(
    val display_string: String,
    val effects: List<ItemSetEffect>,
    val item_set: ItemSet,
    val items: List<Item>
)