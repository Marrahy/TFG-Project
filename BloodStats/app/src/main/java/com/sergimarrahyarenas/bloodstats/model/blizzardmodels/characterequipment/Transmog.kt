package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Transmog(
    val display_string: String,
    val item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.ItemX,
    val item_modified_appearance_id: Int
)