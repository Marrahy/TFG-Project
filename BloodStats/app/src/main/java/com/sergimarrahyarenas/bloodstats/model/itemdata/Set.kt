package com.sergimarrahyarenas.bloodstats.model.itemdata

data class Set(
    val display_string: String,
    val effects: List<Effect>,
    val item_set: ItemSet,
    val items: List<ItemX>
)