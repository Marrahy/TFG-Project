package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Set(
    val display_string: String,
    val effects: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.ItemSetEffect>,
    val item_set: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.ItemSet,
    val items: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Item>
)