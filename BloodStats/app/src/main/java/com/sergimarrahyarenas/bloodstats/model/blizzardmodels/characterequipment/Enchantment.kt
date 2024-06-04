package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Enchantment(
    val display_string: String,
    val enchantment_id: Int,
    val enchantment_slot: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EnchantmentSlot,
    val source_item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.SourceItem
)