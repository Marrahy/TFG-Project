package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata

data class Set(
    val display_string: String,
    val effects: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Effect>,
    val item_set: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemSet,
    val items: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemX>
)